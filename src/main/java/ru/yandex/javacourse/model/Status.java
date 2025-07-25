package ru.yandex.javacourse.model;

/**
 * Перечисление возможных статусов задачи.
 */
public enum Status {
    NEW,        // Задача только создана
    IN_PROGRESS, // Задача в процессе выполнения
    DONE        // Задача завершена
}