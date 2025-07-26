package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

public class Main {
        public static void main(String[] args) {
                TaskManager manager = Managers.getDefault();

                // Create tasks
                Task task1 = new Task("Task 1", "Description 1");
                Task task2 = new Task("Task 2", "Description 2");
                Epic epic1 = new Epic("Epic 1", "Epic description");

                // Add to manager
                int taskId1 = manager.createTask(task1);
                int taskId2 = manager.createTask(task2);
                int epicId1 = manager.createEpic(epic1);

                // Create subtasks
                Subtask subtask1 = new Subtask("Subtask 1", "Subtask desc", epicId1);
                Subtask subtask2 = new Subtask("Subtask 2", "Subtask desc", epicId1);

                // Add subtasks
                manager.createSubtask(subtask1);
                manager.createSubtask(subtask2);

                // Print history
                System.out.println("History:");
                manager.getHistory().forEach(System.out::println);
        }
}