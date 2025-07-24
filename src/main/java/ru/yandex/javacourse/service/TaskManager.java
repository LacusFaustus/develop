package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.*;
import java.util.List;

public interface TaskManager {
    // Task operations
    List<Task> getAllTasks();
    Task getTaskById(int id);
    int createTask(Task task);
    void updateTask(Task updatedTask);
    void deleteTaskById(int id);
    void deleteAllTasks();

    // Epic operations
    List<Epic> getAllEpics();
    Epic getEpicById(int id);
    int createEpic(Epic epic);
    void updateEpic(Epic updatedEpic);
    void deleteEpicById(int id);
    void deleteAllEpics();

    // Subtask operations
    List<Subtask> getAllSubtasks();
    Subtask getSubtaskById(int id);
    int createSubtask(Subtask subtask);
    void updateSubtask(Subtask updatedSubtask);
    void deleteSubtaskById(int id);
    void deleteAllSubtasks();

    // Additional methods
    List<Subtask> getSubtasksByEpicId(int epicId);
    List<Task> getHistory();
}