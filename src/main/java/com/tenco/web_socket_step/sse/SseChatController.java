package com.tenco.web_socket_step.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class SseChatController {

    private final SseChatService sseChatService;

    // 1. 채팅방 화면 (기본 채팅 글 조회)
    @GetMapping("/sse/chat")
    public String index(Model model) {
        model.addAttribute("chatList", sseChatService.findAll());
        return "sse/index";
    }

    // produces : 서버측에서 이러한 데이터를 생산한다.
    // 일반적인 코드를 작성할 때 기본적으로 다 생략 했었음
    // produces = MediaType.TEXT_HTML_VALUE - HTML 파일 형식 (뷰 리졸)
    // produces = MediaType.APPLICATION_JSON_VALUE

    // HTTP/1.1 OK
    // Content-Type: text/event-stream

    // 2. 클라이언트 이 주소를 구독 함 (sse 구독 이벤트 처리)
    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody // 뷰가 아니라 객체(Emitter)를 반환해야 함
    public SseEmitter connect() {
        // clientId 는 서버측에서만 관리하는 값이다. (js측은 전혀 몰라도됨)
        return sseChatService.createConnection(UUID.randomUUID().toString());
    }


    @PostMapping("/sse/send")
    public String sendMessage(@RequestParam(name = "sender") String sender,
                              @RequestParam(name = "message") String message) {

        sseChatService.addMessage(message, sender);

        return "redirect:/sse/chat";
    }
}
