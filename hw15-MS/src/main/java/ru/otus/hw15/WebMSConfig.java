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

@Configuration
public class WebMSConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private final UserService userService;

    @Autowired
    public WebMSConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public FrontendService frontendService(MessageSystem messageSystem) {
        var frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        var frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);

        frontendMsClient.addHandler(MessageType.SAVE_USER, new GetUserDataResponseHandler(frontendService));

        messageSystem.addClient(frontendMsClient);

        return frontendService;
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem) {
        var databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        databaseMsClient.addHandler(MessageType.SAVE_USER, new GetUserDataRequestHandler(userService));

        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
