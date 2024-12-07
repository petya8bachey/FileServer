package com.petya8bachey.file_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FileServerApplication implements CommandLineRunner {

    @Autowired
    private UserSimulation simulation;
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        simulation.simulateMultipleUsers(100);
    }
}
