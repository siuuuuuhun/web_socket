package com.tenco.web_socket_step.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StompChatService {

    private final StompChatRepository stompChatRepository;

    // 스프링에서 제공하는 메시지 전용 템플릿
    // 특정 주소(/sub/...)를 구독(Subscribe) 중인 클라이언트에게 서버가 직접 메시지를 푸시하게 돕습니다.
    private final SimpMessagingTemplate messagingTemplate;

    // 저장 --> 방송

    @Transactional
    public void saveAndBroadCast(Long roomId, String message, String sender) {

        StompChat stompChat = StompChat.builder()
                .roomId(roomId)
                .message(message)
                .sender(sender)
                .build();

        stompChatRepository.save(stompChat);

        // 2. 메시지 브로드캐스트 (동적 주소 배달)
        // "홍길동" : "메시지"
        String formattedMessage = sender + ": " + message;
        // 예) roomId 5라면, "/sub/chat/5" 를 구독중인 클라이언트 들에게 메시지가 초고속으로 전달됩니다.
        messagingTemplate.convertAndSend("/sub/chat/" + roomId, formattedMessage);
    }

    // 특정 채팅방의 과거 대화 내역 전체 조회
    public List<StompChat> findMessageByRoomId(Long roomId) {
        return stompChatRepository.findByRoomId(roomId);
    }
}
