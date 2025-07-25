package main.java.ru.yandex.javacourse;

import java.util.List;
import main.java.ru.yandex.javacourse.model.Epic;
import main.java.ru.yandex.javacourse.model.Subtask;
import main.java.ru.yandex.javacourse.model.Task;
import main.java.ru.yandex.javacourse.service.TaskManager;
import main.java.ru.yandex.javacourse.util.Managers;

public class Main
{
    public static void main(String[] args)
    {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.createSubtask(subtask);

        List<Task> tasks = taskManager.getAllTasks();
        System.out.println(tasks);
    }
}