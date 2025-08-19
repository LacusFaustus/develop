package ru.yandex.javacourse.util;

import java.io.File;
import ru.yandex.javacourse.service.FileBackedTaskManager;
import ru.yandex.javacourse.service.HistoryManager;
import ru.yandex.javacourse.service.InMemoryHistoryManager;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.TaskManager;

/**
 * Утилитный класс для получения стандартных реализаций менеджеров задач.
 */
public final class Managers {

    /**
     * Закрытый конструктор для предотвращения создания экземпляров утилитного класса.
     */
    private Managers() {
        throw new UnsupportedOperationException("Это утилитный класс и не может быть инстанциирован");
    }

    /**
     * Возвращает реализацию TaskManager по умолчанию (в памяти).
     *
     * @return экземпляр InMemoryTaskManager
     */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /**
     * Возвращает реализацию HistoryManager по умолчанию.
     *
     * @return экземпляр InMemoryHistoryManager
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /**
     * Возвращает FileBackedTaskManager, работающий с указанным файлом.
     *
     * @param file файл для хранения данных
     * @return экземпляр FileBackedTaskManager
     */
    public static FileBackedTaskManager getFileBackedTaskManager(final File file) {
        return new FileBackedTaskManager(file);
    }
}