package ru.otus.hw16ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16ms.msocket.MsSocketServer;

@SpringBootApplication
public class Hw16MSApplication implements ApplicationRunner {

    private final MsSocketServer msSocketServer;
    
    @Autowired
    public Hw16MSApplication(MsSocketServer msSocketServer) {
        this.msSocketServer = msSocketServer;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Hw16MSApplication.class, args);
    }
    
    @Override
    public void run(ApplicationArguments args) {
        msSocketServer.serve();
    }
}
