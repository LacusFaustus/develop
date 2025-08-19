package ru.yandex.javacourse.service;

import java.util.List;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

/**
 * Интерфейс менеджера задач.
 * Определяет основные операции для работы с задачами, эпиками и подзадачами.
 */
public interface TaskManager {

    /**
     * Возвращает список всех задач.
     *
     * @return список задач
     */
    List<Task> getAllTasks();

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id идентификатор задачи
     * @return задача или null, если не найдена
     */
    Task getTaskById(int id);

    /**
     * Создает новую задачу.
     *
     * @param task задача для создания
     * @return идентификатор созданной задачи
     */
    int createTask(Task task);

    /**
     * Обновляет существующую задачу.
     *
     * @param updatedTask обновленная версия задачи
     */
    void updateTask(Task updatedTask);

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи для удаления
     */
    void deleteTaskById(int id);

    /**
     * Удаляет все задачи.
     */
    void deleteAllTasks();

    /**
     * Возвращает список всех эпиков.
     *
     * @return список эпиков
     */
    List<Epic> getAllEpics();

    /**
     * Возвращает эпик по идентификатору.
     *
     * @param id идентификатор эпика
     * @return эпик или null, если не найден
     */
    Epic getEpicById(int id);

    /**
     * Создает новый эпик.
     *
     * @param epic эпик для создания
     * @return идентификатор созданного эпика
     */
    int createEpic(Epic epic);

    /**
     * Обновляет существующий эпик.
     *
     * @param updatedEpic обновленная версия эпика
     */
    void updateEpic(Epic updatedEpic);

    /**
     * Удаляет эпик по идентификатору.
     *
     * @param id идентификатор эпика для удаления
     */
    void deleteEpicById(int id);

    /**
     * Удаляет все эпики.
     */
    void deleteAllEpics();

    /**
     * Возвращает список всех подзадач.
     *
     * @return список подзадач
     */
    List<Subtask> getAllSubtasks();

    /**
     * Возвращает подзадачу по идентификатору.
     *
     * @param id идентификатор подзадачи
     * @return подзадача или null, если не найдена
     */
    Subtask getSubtaskById(int id);

    /**
     * Создает новую подзадачу.
     *
     * @param subtask подзадача для создания
     * @return идентификатор созданной подзадачи
     */
    int createSubtask(Subtask subtask) throws InterruptedException;

    /**
     * Обновляет существующую подзадачу.
     *
     * @param updatedSubtask обновленная версия подзадачи
     */
    void updateSubtask(Subtask updatedSubtask);

    /**
     * Удаляет подзадачу по идентификатору.
     *
     * @param id идентификатор подзадачи для удаления
     */
    void deleteSubtaskById(int id);

    /**
     * Удаляет все подзадачи.
     */
    void deleteAllSubtasks();

    /**
     * Возвращает список подзадач для указанного эпика.
     *
     * @param epicId идентификатор эпика
     * @return список подзадач эпика
     */
    List<Subtask> getSubtasksByEpicId(int epicId);

    /**
     * Возвращает список задач в порядке приоритета.
     *
     * @return список задач, отсортированный по времени начала
     */
    List<Task> getPrioritizedTasks();

    /**
     * Возвращает историю просмотров задач.
     *
     * @return список последних просмотренных задач
     */
    List<Task> getHistory();
}