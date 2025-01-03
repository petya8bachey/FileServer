
# README

## Описание проекта

Этот проект представляет собой сервер для работы с файлами, реализованный с использованием Spring Boot и JPA. Сервер позволяет нескольким пользователям одновременно читать и записывать данные в файлы, а также их удалять. Для синхронизации доступа используется механизм блокировок.

## Структура репозитория

В репозитории присутствуют следующие файлы:

1. `File.java` — сущность для хранения информации о файле.
2. `FileRepository.java` — интерфейс репозитория для взаимодействия с базой данных.
3. `FileServerApplication.java` — главный класс приложения, запускающий сервер.
4. `FileService.java` — сервис для выполнения операций с файлами.
5. `UserSimulation.java` — компонент для моделирования работы пользователей с файлами.

## Основные технологии

- **Spring Boot** — фреймворк для создания приложений на языке Java.
- **JPA (Jakarta Persistence API)** — стандарт для работы с базами данных.
- **Spring Data JPA** — интеграция JPA с Spring для упрощения работы с репозиториями.
- **Spring AOP (Asynchronous)** — асинхронная обработка запросов с помощью аннотаций.

## Основные особенности

1. **Асинхронность**: все операции с файлами выполняются асинхронно, что позволяет эффективно обрабатывать множество запросов одновременно.
2. **Блокировки**: используется механизм блокировок (ReadWriteLock) для обеспечения безопасности при одновременном доступе к файлам.
3. **Моделирование пользователей**: с помощью компонента `UserSimulation` моделируется работа нескольких пользователей, которые могут одновременно читать, записывать или удалять файлы.

## Примечания

- В проекте используются блокировки для защиты от конфликтов при параллельных запросах.
- Все операции, связанные с файлам, выполняются асинхронно, что позволяет повышать производительность при обработке множества запросов.
- Для тестирования была реализована симуляция поведения нескольких пользователей, что помогает проверить систему под нагрузкой.