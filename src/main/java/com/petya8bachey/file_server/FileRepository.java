package com.petya8bachey.file_server;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для доступа к файлам в базе данных.
 * Интерфейс расширяет JpaRepository для работы с сущностью File, где имя файла является строковым идентификатором.
 * @author petya8bachey
 * @version 1.0
 */
@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, String> {
}
