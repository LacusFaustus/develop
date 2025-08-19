package ru.yandex.javacourse.model;

/**
 * Перечисление типов задач.
 */
public enum TaskType {
    /** Обычная задача. */
    TASK,

    /** Эпик, содержащий подзадачи. */
    EPIC,

    /** Подзадача, относящаяся к эпику. */
    SUBTASK
}