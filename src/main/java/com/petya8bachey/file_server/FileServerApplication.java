package com.petya8bachey.file_server;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CompletableFuture;

/**
 * Основной класс приложения для сервера файлов.
 * Этот класс использует Spring Boot для запуска приложения и симуляции пользователей.
 * @author petya8bachey
 * @version 1.0
 */
@SpringBootApplication
@EnableAsync
public class FileServerApplication implements CommandLineRunner {

    /**
     * Логгер для регистрации информации о работе приложения.
     */
    Logger logger = LoggerFactory.getLogger(FileServerApplication.class);

    /**
     * Сервис, отвечающий за симуляцию пользователей.
     * Он инжектируется с помощью Spring.
     */
    @Autowired
    private UserSimulation simulation;

    /**
     * Главный метод, точка входа в приложение.
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    /**
     * Метод, который выполняется сразу после запуска приложения.
     * Здесь симулируется работа с 100 пользователями.
     *
     * @param args аргументы командной строки
     */
    @Override
    @SneakyThrows
    public void run(String... args) {
        logger.info("Starting File Server");

        // Запуск симуляции пользователей
        CompletableFuture<Void> users = simulation.simulateMultipleUsers(100);

        // Ожидание завершения симуляции
        users.get();

        logger.info("Done");

        // Завершение работы приложения
        System.exit(0);
    }
}
