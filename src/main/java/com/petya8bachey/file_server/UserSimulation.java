package com.petya8bachey.file_server;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Класс для симуляции поведения пользователей, выполняющих операции чтения и записи файлов.
 * Этот класс используется для тестирования и демонстрации асинхронных операций с файлами.
 * В основе этого подхода лежит классическая задача о читателях-писателях,
 * которая является одной из важнейших задач параллельного программирования.
 * Задача заключается в том, чтобы несколько читателей могли параллельно читать данные,
 * но при этом только один писатель может изменять данные, что требует синхронизации доступа.
 *
 * @author petya8bachey
 * @version 1.0
 */
@Component
@EnableAsync
public class UserSimulation {

    /**
     * Сервис для работы с файлами, включая чтение и запись.
     */
    @Autowired
    private FileService fileService;

    /**
     * Список имен файлов, которые используются в симуляции.
     * Имена файлов генерируются в методе {@link #init()}.
     */
    private List<String> fileNames;

    /**
     * Количество файлов для симуляции. Значение считывается из конфигурационного файла {@code application.properties}.
     */
    @Value("${file.simulation.numberOfFiles:10}")
    private int numberOfFiles;

    /**
     * Генератор случайных чисел, используемый для случайного выбора файлов и операций.
     */
    private final Random random = new Random();

    /**
     * Логгер для записи информации о симуляции.
     */
    private final Logger logger = LoggerFactory.getLogger(UserSimulation.class);

    /**
     * Симулирует операцию записи или чтения файла пользователем.
     * Пользователь случайным образом либо записывает данные в файл, либо читает файл.
     * Для обеспечения корректной работы с файлами используется блокировка
     * с использованием механизма читателей-писателей.
     *
     * @param fileName имя файла для операции
     * @return CompletableFuture, завершающийся без результата
     */
    @SneakyThrows
    @Async
    public CompletableFuture<Void> simulateUserWritingAndReading(String fileName) {
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

    /**
     * Симулирует действия нескольких пользователей, работающих с файлами.
     * Каждый пользователь выполняет случайную операцию записи или чтения с файлами.
     * Для предотвращения конфликтов между чтением и записью используются механизмы синхронизации.
     *
     * @param numberOfUsers количество пользователей, которых нужно симулировать
     * @return CompletableFuture, завершающийся без результата
     */
    @SneakyThrows
    public CompletableFuture<Void> simulateMultipleUsers(int numberOfUsers) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        logger.info("Simulating {} users start", numberOfUsers);

        // Сначала сохраняем файлы с исходным содержимым
        for (String fileName : fileNames) {
            futures.add(fileService.saveFile(new File(fileName, "Random: ")));
        }

        // Симулируем действия пользователей
        for (int i = 0; i < numberOfUsers; i++) {
            futures.add(simulateUserWritingAndReading(fileNames.get(random.nextInt(numberOfFiles))));
        }

        logger.info("Simulating {} users complete", numberOfUsers);

        // Ждем завершения всех операций
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Метод, который инициализирует имена файлов, которые будут использоваться в симуляции.
     * Этот метод вызывается автоматически после создания компонента.
     */
    @PostConstruct
    public void init() {
        fileNames = new ArrayList<>();
        for (int i = 0; i < numberOfFiles; i++) {
            fileNames.add("file_" + i);
        }
        logger.info("File names: {}", fileNames);
    }
}
