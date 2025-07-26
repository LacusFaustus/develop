package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание и демонстрация работы менеджера задач
        Task task = new Task("Task 1", "Description");
        int taskId = taskManager.createTask(task);

        Epic epic = new Epic("Epic 1", "Epic description");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Sub desc", epicId);
        taskManager.createSubtask(subtask);

        // Вывод истории
        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        System.out.println("History: " + taskManager.getHistory());
    }
}