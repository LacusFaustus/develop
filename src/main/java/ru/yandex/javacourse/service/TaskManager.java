package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.*;

import java.util.List;

/**
 * Интерфейс для управления задачами, эпиками и подзадачами.
 */
public interface TaskManager {
    // Управление задачами
    List<Task> getAllTasks();
    Task getTaskById(int id);
    int createTask(Task task);
    void updateTask(Task updatedTask);
    void deleteTaskById(int id);
    void deleteAllTasks();

    // Управление эпиками
    List<Epic> getAllEpics();
    Epic getEpicById(int id);
    int createEpic(Epic epic);
    void updateEpic(Epic updatedEpic);
    void deleteEpicById(int id);
    void deleteAllEpics();

    // Управление подзадачами
    List<Subtask> getAllSubtasks();
    Subtask getSubtaskById(int id);
    int createSubtask(Subtask subtask);
    void updateSubtask(Subtask updatedSubtask);
    void deleteSubtaskById(int id);
    void deleteAllSubtasks();

    // Дополнительные методы
    List<Subtask> getSubtasksByEpicId(int epicId);
    List<Task> getHistory();
}