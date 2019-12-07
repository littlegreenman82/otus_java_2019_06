package ru.otus.hw16ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw16common.messagesystem.MessageSystem;
import ru.otus.hw16common.messagesystem.MessageSystemImpl;
import ru.otus.hw16common.messagesystem.MsClient;
import ru.otus.hw16ms.msclient.MsClientImpl;
import ru.otus.hw16ms.msocket.MsSocketClient;

@Configuration
public class WebMSConfig {
    private final MsSocketClient clientSocketForFrontend;
    private final MsSocketClient msSocketClientForDatabase;
    
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
    public WebMSConfig(MsSocketClient msSocketClientForFrontend, MsSocketClient msSocketClientForDatabase) {
        this.clientSocketForFrontend = msSocketClientForFrontend;
        this.msSocketClientForDatabase = msSocketClientForDatabase;
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
        msSocketClientForDatabase.setConnectionHost(dbServiceHost);
        msSocketClientForDatabase.setConnectionPort(dbServicePort);

        var databaseMsClient = new MsClientImpl(dbServiceName, msSocketClientForDatabase);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
