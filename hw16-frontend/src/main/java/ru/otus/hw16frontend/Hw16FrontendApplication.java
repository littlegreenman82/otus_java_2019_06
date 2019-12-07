package ru.otus.hw16frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16frontend.msocket.FrontendSocketServer;

@SpringBootApplication
public class Hw16FrontendApplication implements ApplicationRunner {

    private final FrontendSocketServer server;
    
    @Autowired
    public Hw16FrontendApplication(FrontendSocketServer server) {
        this.server = server;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Hw16FrontendApplication.class, args);
    }
    
    @Override
    public void run(ApplicationArguments args) {
        server.serve();
    }
}
