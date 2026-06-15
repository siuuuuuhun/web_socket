package com.tenco.web_socket_step.polling;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "polling_chat_tb")
@Entity
public class PollingChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String sender;

    @Builder
    public PollingChat(Long id, String message, String sender) {
        this.id = id;
        this.message = message;
        this.sender = sender;
    }
}
