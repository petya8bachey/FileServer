package com.petya8bachey.file_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableAsync
public class FileServerApplication implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(FileServerApplication.class);

    @Autowired
    private UserSimulation simulation;
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting File Server");
        CompletableFuture<Void> users = simulation.simulateMultipleUsers(100);
        users.get();
        logger.info("Done");
    }
}
