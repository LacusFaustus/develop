package ru.yandex.javacourse.service;

import java.util.List;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

public interface TaskManager
{
    List<Task> getAllTasks();
    Task getTaskById(int id);
    int createTask(Task task);
    void updateTask(Task updatedTask);
    void deleteTaskById(int id);
    void deleteAllTasks();

    List<Epic> getAllEpics();
    Epic getEpicById(int id);
    int createEpic(Epic epic);
    void updateEpic(Epic updatedEpic);
    void deleteEpicById(int id);
    void deleteAllEpics();

    List<Subtask> getAllSubtasks();
    Subtask getSubtaskById(int id);
    int createSubtask(Subtask subtask);
    void updateSubtask(Subtask updatedSubtask);
    void deleteSubtaskById(int id);
    void deleteAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);
    List<Task> getHistory();
}