package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Managers.getDefaultHistory();

        // Создаем задачи
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Создаем эпики с подзадачами
        Epic epic1 = new Epic("Epic with subtasks", "Description");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic1.getId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description", epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Epic without subtasks", "Description");
        taskManager.createEpic(epic2);

        // Запрашиваем задачи в разном порядке
        taskManager.getTaskById(task1.getId());
        printHistory(taskManager.getHistory());

        taskManager.getEpicById(epic1.getId());
        printHistory(taskManager.getHistory());

        taskManager.getSubtaskById(subtask2.getId());
        printHistory(taskManager.getHistory());

        taskManager.getTaskById(task1.getId()); // Повторный запрос
        printHistory(taskManager.getHistory());

        taskManager.getTaskById(task2.getId());
        printHistory(taskManager.getHistory());

        // Удаляем задачу из истории
        taskManager.deleteTaskById(task1.getId());
        printHistory(taskManager.getHistory());

        // Удаляем эпик с подзадачами
        taskManager.deleteEpicById(epic1.getId());
        printHistory(taskManager.getHistory());
    }

    private static void printHistory(List<Task> history) {
        System.out.println("История просмотров:");
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println();
    }
}