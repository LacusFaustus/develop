// src/main/java/ru/yandex/praktikum/Main.java
package ru.yandex.praktikum;

import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.service.TaskManager;
import ru.yandex.praktikum.util.Managers;

public class Main {
        public static void main(String[] args) {
                TaskManager manager = Managers.getDefault();

                Task task1 = new Task("Task 1", "Description 1");
                Task task2 = new Task("Task 2", "Description 2");
                Epic epic1 = new Epic("Epic 1", "Epic description");

                int taskId1 = manager.createTask(task1);
                int taskId2 = manager.createTask(task2);
                int epicId1 = manager.createEpic(epic1);

                Subtask subtask1 = new Subtask("Subtask 1", "Subtask desc", epicId1);
                Subtask subtask2 = new Subtask("Subtask 2", "Subtask desc", epicId1);

                manager.createSubtask(subtask1);
                manager.createSubtask(subtask2);

                System.out.println("History:");
                manager.getHistory().forEach(System.out::println);
        }
}