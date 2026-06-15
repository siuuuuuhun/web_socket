package com.tenco.web_socket_step.polling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PollingController {

    private final PollingChatService pollingChatService;

    @GetMapping("/")
    public String index(Model model) {
        System.out.println(">>> Polling 페이지가 새로고침 되었습니다 <<<");
        model.addAttribute("chatList", pollingChatService.findAll());

        return "polling/index";
    }

    @PostMapping("/polling/chat")
    public String save(@RequestParam(name = "message") String message,
                       @RequestParam(name = "sender", defaultValue = "익명") String sender) {
        pollingChatService.save(message, sender);
        return "redirect:/";
    }
}
