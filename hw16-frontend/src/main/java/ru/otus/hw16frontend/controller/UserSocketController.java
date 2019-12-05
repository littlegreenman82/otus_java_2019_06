package ru.otus.hw16frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw16frontend.dto.UserDTO;
import ru.otus.hw16frontend.front.FrontendService;

@Controller
public class UserSocketController {
    private Logger logger = LoggerFactory.getLogger(UserSocketController.class);
    
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @Autowired
    public UserSocketController(FrontendService frontendService, SimpMessagingTemplate template) {
        this.frontendService = frontendService;
        this.template = template;
    }

    @MessageMapping("/users.add")
    public void users(@Payload UserDTO user) throws JsonProcessingException {
        final var objectMapper = new ObjectMapper();
        final var userJson = objectMapper.writeValueAsString(user);
        frontendService.saveUser(userJson, saved -> template.convertAndSend("/topic/users", saved));
    }
}
