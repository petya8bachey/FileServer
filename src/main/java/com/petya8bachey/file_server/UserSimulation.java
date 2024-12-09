package com.petya8bachey.file_server;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
public class UserSimulation {

    @Autowired
    private FileService fileService;

    private List<String> fileNames;
    private final int numberOfFiles = 10;

    private final Random random = new Random();
    Logger logger = LoggerFactory.getLogger(UserSimulation.class);

    @Async
    public CompletableFuture<Void> simulateUserWritingAndReading(String fileName) throws Exception {
        if (random.nextBoolean()) {
            String contentToAdd = Integer.toString(random.nextInt());
            CompletableFuture<Void> future = fileService.setContentToFile(fileName, contentToAdd);
            future.get();
        } else {
            CompletableFuture<File> future = fileService.getFile(fileName);
            future.get();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> simulateMultipleUsers(int numberOfUsers) throws Exception {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        logger.info("Simulating {} users start", numberOfUsers);
        for (String fileName : fileNames) {
            futures.add(fileService.saveFile(new File(fileName, "Random: ")));
        }

        for (int i = 0; i < numberOfUsers; i++) {
            futures.add(simulateUserWritingAndReading(fileNames.get(random.nextInt(numberOfFiles))));
        }

        logger.info("Simulating {} users complete", numberOfUsers);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return CompletableFuture.completedFuture(null);
    }

    @PostConstruct
    public void init() {
        fileNames = new ArrayList<>();
        for (int i = 0; i < numberOfFiles; i++) {
            fileNames.add("file_" + i);
        }
        logger.info("File names: {}", fileNames);
    }
}
