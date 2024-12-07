package com.petya8bachey.file_server;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Random;

@Component
@EnableAsync
public class UserSimulation {

    @Autowired
    private FileService fileService;

    private List<String> fileNames;
    private final int numberOfFiles = 10;

    private final Random random = new Random();

    @Async
    public void simulateUserWritingAndReading(String fileName) {
        if (random.nextBoolean()) {
            String contentToAdd = Integer.toString(random.nextInt());
            fileService.setContentToFile(fileName, contentToAdd);
        } else {
            fileService.getFile(fileName);
        }
        CompletableFuture.completedFuture(null);
    }

    public void simulateMultipleUsers(int numberOfUsers) {
        for(String fileName : fileNames) {
            fileService.saveFile(new File(fileName, "Random: "));
        }

        for (int i = 0; i < numberOfUsers; i++) {
            simulateUserWritingAndReading(fileNames.get(random.nextInt(numberOfFiles)));
        }
    }

    @PostConstruct
    public void init() {
        fileNames = new ArrayList<>();
        for (int i = 0; i < numberOfFiles; i++) {
            fileNames.add("file_" + i);
        }
    }
}
