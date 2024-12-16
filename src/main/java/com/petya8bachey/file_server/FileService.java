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

/**
 * Сервисный класс для работы с файлами, предоставляющий асинхронные операции.
 * Использует FileRepository для доступа к данным о файлах.
 * @author petya8bachey
 * @version 1.0
 */
@Service
@EnableAsync
public class FileService {

    /**
     * Репозиторий для работы с данными о файлах.
     * Используется для выполнения операций с файлами в базе данных.
     */
    @Autowired
    private FileRepository fileRepository;

    /**
     * Механизм синхронизации для предотвращения конфликтов при параллельных операциях.
     * Используется для блокировки чтения и записи.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Логгер для записи информации об операциях с файлами.
     * Используется для логирования запросов и результатов операций.
     */
    private static final Logger logger = Logger.getLogger(FileService.class.getName());

    /**
     * Объект для генерации случайных чисел.
     * Используется для симуляции задержки доступа к базе данных.
     */
    private final Random random = new Random();

    /**
     * Метод для имитации задержки доступа к базе данных.
     */
    private void simulateDatabaseDelay() {
        try {
            Thread.sleep(100 + random.nextInt(200)); // Имитация случайной задержки
        } catch (InterruptedException e) {
            logger.info("Thread interrupted");
            throw new RuntimeException(e);
        }
    }

    /**
     * Асинхронно получает файл по его имени.
     *
     * @param name имя файла для поиска
     * @return CompletableFuture, содержащий найденный файл или null, если файл не найден
     */
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

    /**
     * Асинхронно удаляет файл по его имени.
     *
     * @param name имя файла для удаления
     * @return CompletableFuture, завершающийся без результата
     */
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

    /**
     * Асинхронно устанавливает содержимое файла по его имени.
     *
     * @param name    имя файла, для которого устанавливается содержимое
     * @param content новое содержимое файла
     * @return CompletableFuture, завершающийся без результата
     */
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

    /**
     * Асинхронно сохраняет файл в базу данных.
     *
     * @param file файл для сохранения
     * @return CompletableFuture, завершающийся без результата
     */
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
