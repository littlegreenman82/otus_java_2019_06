package ru.otus.hw16ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw16ms.messagesystem.*;
import ru.otus.hw16ms.msocket.Client;

@Configuration
public class WebMSConfig {
    private final Client clientSocketForFrontend;
    private final Client clientSocketForDatabase;
    
    @Value("${service.frontend.name}")
    private String frontendServiceName;
    @Value("${service.frontend.host}")
    private String frontendServiceHost;
    @Value("${service.frontend.port}")
    private int frontendServicePort;
    
    @Value("${service.db.name}")
    private String dbServiceName;
    @Value("${service.db.host}")
    private String dbServiceHost;
    @Value("${service.db.port}")
    private int dbServicePort;
    
    @Autowired
    public WebMSConfig(Client clientSocketForFrontend, Client clientSocketForDatabase) {
        this.clientSocketForFrontend = clientSocketForFrontend;
        this.clientSocketForDatabase = clientSocketForDatabase;
    }
    
    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MsClient frontendService(MessageSystem messageSystem) {
        clientSocketForFrontend.setConnectionHost(frontendServiceHost);
        clientSocketForFrontend.setConnectionPort(frontendServicePort);
        
        var frontendMsClient = new MsClientImpl(frontendServiceName, clientSocketForFrontend);
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem) {
        clientSocketForDatabase.setConnectionHost(dbServiceHost);
        clientSocketForDatabase.setConnectionPort(dbServicePort);

        var databaseMsClient = new MsClientImpl(dbServiceName, clientSocketForDatabase);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
