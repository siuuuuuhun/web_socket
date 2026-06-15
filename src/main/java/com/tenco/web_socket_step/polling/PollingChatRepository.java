package com.tenco.web_socket_step.polling;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PollingChatRepository extends JpaRepository<PollingChat, Long> {
}
