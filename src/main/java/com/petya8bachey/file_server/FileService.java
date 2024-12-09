package com.petya8bachey.file_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

@Service
@EnableAsync
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

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
    public CompletableFuture<File> getFile(String name) {
        lock.readLock().lock();
        File file;
        try {
            logger.info("Attempting to retrieve file with name: " + name);
            simulateDatabaseDelay();
            file = fileRepository.findById(name).orElse(null);
            logger.info("File retrieved: " + name);
        } finally {
            lock.readLock().unlock();
        }
        return CompletableFuture.completedFuture(file);
    }

    @Async
    public CompletableFuture<Void> deleteFile(String name) {
        lock.writeLock().lock();
        try {
            logger.info("Attempting to delete file with name: " + name);
            simulateDatabaseDelay();
            if (fileRepository.findById(name).isPresent()) {
                fileRepository.deleteById(name);
                logger.info("File deleted: " + name);
            } else {
                logger.info("File not found: " + name);
            }
        } finally {
            lock.writeLock().unlock();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> setContentToFile(String name, String content) {
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> saveFile(File file) {
        lock.writeLock().lock();
        try {
            logger.info("Attempting to save file: " + file.getName());
            simulateDatabaseDelay();
            fileRepository.save(file);
            logger.info("File saved: " + file.getName());
        } finally {
            lock.writeLock().unlock();
        }
        return CompletableFuture.completedFuture(null);
    }
}
