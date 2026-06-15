package com.tenco.web_socket_step.polling;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollingChatService {

    private final PollingChatRepository pollingChatRepository;

    public void save(String message, String sender) {
        PollingChat pollingChat = PollingChat
                .builder()
                .message(message)
                .sender(sender)
                .build();

        pollingChatRepository.save(pollingChat);
    }

    @Transactional(readOnly = true)
    public List<PollingChat> findAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return pollingChatRepository.findAll(sort);
    }
}
