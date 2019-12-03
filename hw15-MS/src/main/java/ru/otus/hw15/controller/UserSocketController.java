package ru.otus.hw15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw15.entity.User;
import ru.otus.hw15.front.FrontendService;

@Controller
public class UserSocketController {


    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @Autowired
    public UserSocketController(FrontendService frontendService, SimpMessagingTemplate template) {
        this.frontendService = frontendService;
        this.template = template;
    }

    @MessageMapping("/users.add")
    public void users(@Payload User user) {
        frontendService.saveUser(user, saved -> template.convertAndSend("/topic/users", saved));
    }
}
