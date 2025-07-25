package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

import java.util.List;

/**
 * Главный класс приложения для демонстрации работы менеджера задач.
 */
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание и демонстрация работы с задачами
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        // Создание эпика с подзадачами
        Epic epic1 = new Epic("Epic with subtasks", "Description");
        int epic1Id = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic1Id);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic1Id);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // Демонстрация истории просмотров
        taskManager.getTaskById(task1Id);
        taskManager.getEpicById(epic1Id);
        printHistory(taskManager.getHistory());

        // Изменение статусов и проверка обновления эпика
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        System.out.println("Epic status after subtask update: " +
                taskManager.getEpicById(epic1Id).getStatus());
    }

    private static void printHistory(List<Task> history) {
        System.out.println("History:");
        history.forEach(System.out::println);
        System.out.println();
    }
}