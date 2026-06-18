package com.tenco.web_socket_step.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final StompChatService stompChatService;

    // http://localhost8080/stomp/chat/1
    @GetMapping("/stomp/chat/{roomId}")
    public String index(@PathVariable("roomId") Long roomId, Model model) {

        model.addAttribute("roomId", roomId); // 프론트 엔드에서 방 번호를 쓸 수 있도록 전달
        model.addAttribute("chatList", stompChatService.findMessageByRoomId(roomId)); // 프론트 엔드에서 방 번호를 쓸 수 있도록 전달
        return "stomp/index";
    }

    // 메시지 수신(publish 처리) 및 라우팅
    // 클라이언트 "/pub/chat/message/{roomId} 경로로 메시지를 던지면 이 메서드가 실행됨
    // RequestMapping와 대응됨
    // @DestinationVatiable : STOMP 목적지 경로에 포함된 동적 변수 (roomId) 를 가로챔
    // @PathVariable과 대응됨
    @MessageMapping("chat/message/{roomId}")
    public void receiveMessage(@DestinationVariable("roomId") Long roomId, Map<String, String> payload) {
        // DTO 대신 Map을 사용해서 JSON 데이터를 받을 수 있습니다. (Jackson 이 자동 변환해줌)
        String sender = payload.get("sender");
        String message = payload.get("message");

        stompChatService.saveAndBroadCast(roomId, sender, message);
    }

    // SEND
    // destination : /pub/chat/message/5 <-- 끝 5를 낚아챔
    // content-length : 41
    // content-type: application/json
    //
     // {"sender": "홍길동", "message": "안녕하세요"}
}
