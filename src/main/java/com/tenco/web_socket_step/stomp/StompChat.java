package com.tenco.web_socket_step.stomp;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "stomp_chat_tb")
@Entity
public class StompChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String sender;
    @Column(nullable = false)
    private Long roomId;

    @Builder
    public StompChat(Long id, Long roomId, String message, String sender) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.roomId = roomId;
    }

}