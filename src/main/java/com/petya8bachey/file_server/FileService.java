package com.petya8bachey.file_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.Random;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private static final Logger logger = Logger.getLogger(FileService.class.getName());
    private final Random random = new Random();

    private void simulateDatabaseDelay() {
        try {
            Thread.sleep(100 + random.nextInt(200));
        } catch (InterruptedException e) {
            logger.info("Thread interrupted");
            throw new RuntimeException(e);
        }
    }

    @Async
    public void getFile(String name) {
        logger.info("Attempting to retrieve file with name: " + name);
        simulateDatabaseDelay();
        fileRepository.findById(name);
    }

    @Async
    public void deleteFile(String name) {
        logger.info("Attempting to delete file with name: " + name);
        simulateDatabaseDelay();
        if (fileRepository.findById(name).isPresent()) {
            fileRepository.deleteById(name);
            logger.info("File deleted: " + name);
        } else {
            logger.info("File not found: " + name);
        }
    }

    @Async
    public void setContentToFile(String name, String content) {
        logger.info("Attempting to add content to file: " + name);
        simulateDatabaseDelay();
        Optional<File> file = fileRepository.findById(name);
        if (file.isPresent()) {
            file.get().setContent(content);
            fileRepository.save(file.get());
            logger.info("Content added to file: " + name);
        } else {
            logger.info("File not found: " + name);
        }
    }

    @Async
    public void saveFile(File file) {
        logger.info("Attempting to save file : " + file.getName());
        simulateDatabaseDelay();
        fileRepository.save(file);
        logger.info("File saved: " + file.getName());
    }
}
