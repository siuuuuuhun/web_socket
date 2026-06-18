package com.tenco.web_socket_step.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // IoC
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화하여 STOMP 프로토콜 기반에 통신을 설정합니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트는 웹소켓 연결하기 위해서 경로 ws://localhost8080/ws-stomp 연결을 시도하게 됨
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*");
        // CORS(교차 출처 허용) : 현재는 모든 도메인에서 요청 및 응답 허용
        // 실무 운영 환경에서는 특정 도메인을 등록해서 사용합니다.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 1. 우체통 설정 - 발행 경로 설정
        // 클라이언트가 서버에게 편지를 보낼 때(송신) 붙이는 시작 주소(접두사)입니다.
        // 예) 클라이언트가 /pub/chat/message 로 메시지를 던지면 @MessageMapping() 컨트롤러가
        // 받아서 알아서 처리합니다.
        registry.setApplicationDestinationPrefixes("/pub");

        // 2. 개인 우편함 설정 - 구독 설정
        // 클라이언트가 서버로부터 편지를 받기 위해(수신) 대기하는 시작 주소(접두사) 입니다.
        // enableSimpleBroker : 외부 브로커(카프카, 래빗MQ 등) 연동 없이
        // 스프링 부트 메모리 안에 가볍고 빠른 내장 우체국 분류 센터를 만듭니다.
        // 클라이언트가 /sub/chat/{roomId} 를 구독해두면 서버가 해당 주소를 찾아서 알아서 메시지를 보내줌
        registry.enableSimpleBroker("/sub");

    }
}
