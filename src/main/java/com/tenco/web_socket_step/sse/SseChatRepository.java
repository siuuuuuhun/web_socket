package com.tenco.web_socket_step.sse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SseChatRepository extends JpaRepository<SseChat, Long> {
}
