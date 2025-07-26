package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

<<<<<<< HEAD
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
=======
        // 1. Создаем задачи
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(4, "Подзадача 2", "Описание 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask(5, "Подзадача 3", "Описание 3", Status.NEW, epic1.getId());
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        // Создаем в менеджере
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createEpic(epic2);

        // 2. Запрашиваем задачи в разном порядке
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask1.getId()); // Дубликат
        taskManager.getEpicById(epic1.getId()); // Дубликат

        // 3. Проверяем историю
        System.out.println("История после запросов:");
        taskManager.getHistory().forEach(System.out::println);

        // 4. Удаляем задачу из истории
        taskManager.deleteTaskById(task1.getId());
        System.out.println("\nИстория после удаления задачи 1:");
        taskManager.getHistory().forEach(System.out::println);

        // 5. Удаляем эпик с подзадачами
        taskManager.deleteEpicById(epic1.getId());
        System.out.println("\nИстория после удаления эпика 1:");
        taskManager.getHistory().forEach(System.out::println);
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }
}