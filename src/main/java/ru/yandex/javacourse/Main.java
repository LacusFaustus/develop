package ru.yandex.javacourse;

import java.time.Duration;
import java.time.LocalDateTime;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

public class Main
{
    public static void main(String[] args)
    {
        TaskManager manager = Managers.getDefault();

        // Create tasks with time
        Task task1 = new Task("Task 1", "Description 1", Status.NEW,
                LocalDateTime.now(), Duration.ofHours(2));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2", Status.NEW,
                LocalDateTime.now().plusHours(3), Duration.ofHours(1));
        manager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic description");
        int epicId1 = manager.createEpic(epic1);

        // Create subtasks
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask desc", epicId1,
                Status.NEW, LocalDateTime.now().plusHours(5), Duration.ofHours(1));
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Subtask desc", epicId1,
                Status.NEW, LocalDateTime.now().plusHours(7), Duration.ofHours(2));
        manager.createSubtask(subtask2);

        // Print prioritized tasks
        System.out.println("Prioritized tasks:");
        manager.getPrioritizedTasks().forEach(System.out::println);

        // Print history
        System.out.println("\nHistory:");
        manager.getHistory().forEach(System.out::println);
    }
}