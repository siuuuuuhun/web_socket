package com.tenco.web_socket_step.stomp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StompChatRepository extends JpaRepository<StompChat, Long> {
    // findAll() 전체 조회

    // 특정 방 번호(roomId) 에 해당하는 채팅 내역만 조회하는 메서드
    // 쿼리 메서드 (메서드 작성 시 이름 규칙만 잘 지키면 자동으로 쿼리를 만들어주는 JPA기능 중 하나)
    // @Query("SELECT c FROM StompChat c WHERE c.roomId = :roomId ORDER BY c.id ASC")
    List<StompChat> findByRoomId(Long roomId);
}
