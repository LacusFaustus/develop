package ru.yandex.javacourse.service;

import java.util.List;
import ru.yandex.javacourse.model.Task;

/**
 * Интерфейс для управления историей просмотров задач.
 * Позволяет добавлять, удалять и получать задачи из истории просмотров.
 */
public interface HistoryManager {

    /**
     * Добавляет задачу в историю просмотров.
     *
     * @param task задача для добавления в историю
     */
    void add(Task task);

    /**
     * Удаляет задачу из истории просмотров по идентификатору.
     *
     * @param id идентификатор задачи для удаления
     */
    void remove(int id);

    /**
     * Возвращает список задач из истории просмотров.
     *
     * @return список задач в порядке их просмотра
     */
    List<Task> getHistory();

    /**
     * Очищает историю просмотров.
     */
    void clear();
}