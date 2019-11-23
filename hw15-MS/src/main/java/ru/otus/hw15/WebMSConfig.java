package ru.otus.hw15;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw15.front.FrontendService;
import ru.otus.hw15.front.FrontendServiceImpl;
import ru.otus.hw15.front.handlers.GetUserDataResponseHandler;
import ru.otus.hw15.messagesystem.*;
import ru.otus.hw15.service.GetUserDataRequestHandler;
import ru.otus.hw15.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class WebMSConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private final UserService userService;

    @Autowired
    public WebMSConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MsClient frontendMsClient() {
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public FrontendService frontendService() {
        return new FrontendServiceImpl(frontendMsClient(), DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean
    public MsClient databaseMsClient() {
        return new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem());
    }

    @PostConstruct
    private void postConstruct() {
        frontendMsClient().addHandler(MessageType.SAVE_USER, new GetUserDataResponseHandler(frontendService()));
        databaseMsClient().addHandler(MessageType.SAVE_USER, new GetUserDataRequestHandler(userService));

        messageSystem().addClient(frontendMsClient());
        messageSystem().addClient(databaseMsClient());

    }

    @PreDestroy
    private void preDestroy() {
        try {
            messageSystem().dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
