package com.petya8bachey.file_server;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Класс, представляющий файл с именем и содержимым.
 * Этот класс является сущностью JPA и используется для хранения информации о файле в базе данных.
 * @author petya8bachey
 * @version 1.0
 */
@Entity
public class File {

    /**
     * Имя файла, которое является уникальным идентификатором файла в базе данных.
     */
    @Id
    private String name;

    /**
     * Содержимое файла, представляемое строкой.
     */
    private String content;

    /**
     * Конструктор для создания файла с заданным именем.
     * Содержимое файла будет пустым.
     *
     * @param name имя файла
     */
    public File(String name) {
        this.name = name;
        this.content = "";
    }

    /**
     * Конструктор для создания файла с заданным именем и содержимым.
     *
     * @param name имя файла
     * @param content содержимое файла
     */
    public File(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * Конструктор без параметров для использования в JPA.
     * Необходимо для создания экземпляров класса при извлечении данных из базы данных.
     */
    public File() {
    }

    /**
     * Возвращает имя файла.
     *
     * @return имя файла
     */
    public synchronized String getName() {
        return name;
    }

    /**
     * Возвращает содержимое файла.
     * Метод синхронизирован для обеспечения безопасного доступа из многозадачной среды.
     *
     * @return содержимое файла
     */
    public synchronized String getContent() {
        return content;
    }

    /**
     * Устанавливает новое содержимое для файла.
     * Метод синхронизирован для обеспечения безопасного доступа из многозадачной среды.
     *
     * @param content новое содержимое файла
     */
    public synchronized void setContent(String content) {
        this.content = content;
    }
}
