package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
        public static void main(String[] args) {
                TaskManager manager = Managers.getDefault();

                // Create tasks with time
                Task task1 = new Task("Task 1", "Description 1");
                task1.setStartTime(LocalDateTime.now());
                task1.setDuration(Duration.ofHours(2));

                Task task2 = new Task("Task 2", "Description 2");
                task2.setStartTime(LocalDateTime.now().plusHours(3));
                task2.setDuration(Duration.ofHours(1));

                Epic epic1 = new Epic("Epic 1", "Epic description");

                // Add to manager
                int taskId1 = manager.createTask(task1);
                int taskId2 = manager.createTask(task2);
                int epicId1 = manager.createEpic(epic1);

                // Create subtasks
                Subtask subtask1 = new Subtask("Subtask 1", "Subtask desc", epicId1);
                subtask1.setStartTime(LocalDateTime.now().plusHours(5));
                subtask1.setDuration(Duration.ofHours(1));

                Subtask subtask2 = new Subtask("Subtask 2", "Subtask desc", epicId1);
                subtask2.setStartTime(LocalDateTime.now().plusHours(7));
                subtask2.setDuration(Duration.ofHours(2));

                // Add subtasks
                manager.createSubtask(subtask1);
                manager.createSubtask(subtask2);

                // Print prioritized tasks
                System.out.println("Prioritized tasks:");
                manager.getPrioritizedTasks().forEach(System.out::println);

                // Print history
                System.out.println("\nHistory:");
                manager.getHistory().forEach(System.out::println);
        }
}