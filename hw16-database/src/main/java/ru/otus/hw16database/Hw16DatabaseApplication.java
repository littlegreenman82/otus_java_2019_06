package ru.otus.hw16database;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16database.msocket.Server;

@SpringBootApplication
public class Hw16DatabaseApplication implements ApplicationRunner {

    private final Server socketServer;
    
    public Hw16DatabaseApplication(Server socketServer) {
        this.socketServer = socketServer;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Hw16DatabaseApplication.class, args);
    }
    
    @Override
    public void run(ApplicationArguments args) {
        socketServer.serve();
    }
}
