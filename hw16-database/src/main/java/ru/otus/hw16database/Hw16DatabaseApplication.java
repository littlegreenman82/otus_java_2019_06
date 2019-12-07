package ru.otus.hw16database;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16database.msocket.DatabaseSocketServer;

@SpringBootApplication
public class Hw16DatabaseApplication implements ApplicationRunner {

    private final DatabaseSocketServer socketDatabaseServer;

    public Hw16DatabaseApplication(DatabaseSocketServer socketDatabaseServer) {
        this.socketDatabaseServer = socketDatabaseServer;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Hw16DatabaseApplication.class, args);
    }
    
    @Override
    public void run(ApplicationArguments args) {
        socketDatabaseServer.serve();
    }
}
