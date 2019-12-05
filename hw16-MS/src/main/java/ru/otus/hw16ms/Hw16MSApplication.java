package ru.otus.hw16ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16ms.msocket.Server;

@SpringBootApplication
public class Hw16MSApplication implements ApplicationRunner {
    
    private final Server socketServer;
    
    @Autowired
    public Hw16MSApplication(Server socketServer) {
        this.socketServer = socketServer;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Hw16MSApplication.class, args);
    }
    
    @Override
    public void run(ApplicationArguments args) {
        socketServer.serve();
    }
}
