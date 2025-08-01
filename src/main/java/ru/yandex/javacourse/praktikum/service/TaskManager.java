// src/main/java/ru/yandex/praktikum/service/TaskManager.java
package ru.yandex.javacourse.praktikum.service;

import ru.yandex.javacourse.praktikum.model.*;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    int createTask(Task task);
    int createEpic(Epic epic);
    int createSubtask(Subtask subtask);

    void updateTask(Task updatedTask);
    void updateEpic(Epic updatedEpic);
    void updateSubtask(Subtask updatedSubtask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksByEpicId(int epicId);
    List<Task> getHistory();
}