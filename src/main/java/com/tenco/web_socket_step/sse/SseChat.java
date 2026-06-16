package com.tenco.web_socket_step.sse;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "sse_chat_tb")
@Entity
public class SseChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String sender;

    @Builder
    public SseChat(Long id, String message, String sender) {
        this.id = id;
        this.message = message;
        this.sender = sender;
    }

}
