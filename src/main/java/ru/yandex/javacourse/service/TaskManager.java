package ru.yandex.javacourse.service;

<<<<<<< HEAD
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import java.util.List;

public interface TaskManager {
=======
import java.util.List;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

public interface TaskManager
{
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
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