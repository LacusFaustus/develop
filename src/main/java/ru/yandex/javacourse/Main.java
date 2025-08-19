package ru.yandex.javacourse;

import java.time.Duration;
import java.time.LocalDateTime;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.util.Managers;

/**
 * Главный класс приложения для демонстрации работы менеджера задач.
 */
public final class Main {

    /** Количество часов для смещения второй задачи. */
    private static final int TASK2_HOURS_OFFSET = 3;
    /** Количество часов для смещения первой подзадачи. */
    private static final int SUBTASK1_HOURS_OFFSET = 5;
    /** Количество часов для смещения второй подзадачи. */
    private static final int SUBTASK2_HOURS_OFFSET = 7;
    /** Длительность задачи 1 (в часах). */
    private static final int TASK1_DURATION_HOURS = 2;
    /** Длительность задачи 2 (в часах). */
    private static final int TASK2_DURATION_HOURS = 1;
    /** Длительность подзадачи 2 (в часах). */
    private static final int SUBTASK2_DURATION_HOURS = 2;

    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private Main() {
        throw new UnsupportedOperationException("Это утилитный класс");
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(final String[] args) throws InterruptedException {
        TaskManager manager = Managers.getDefault();

        // Create tasks with time
        Task task1 = new Task("Task 1", "Description 1", Status.NEW,
                LocalDateTime.now(), Duration.ofHours(TASK1_DURATION_HOURS));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2", Status.NEW,
                LocalDateTime.now().plusHours(TASK2_HOURS_OFFSET),
                Duration.ofHours(TASK2_DURATION_HOURS));
        manager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic description");
        int epicId1 = manager.createEpic(epic1);

        // Create subtasks
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask desc", epicId1,
                Status.NEW, LocalDateTime.now().plusHours(SUBTASK1_HOURS_OFFSET),
                Duration.ofHours(TASK2_DURATION_HOURS));
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Subtask desc", epicId1,
                Status.NEW, LocalDateTime.now().plusHours(SUBTASK2_HOURS_OFFSET),
                Duration.ofHours(SUBTASK2_DURATION_HOURS));
        manager.createSubtask(subtask2);

        // Print prioritized tasks
        System.out.println("Prioritized tasks:");
        manager.getPrioritizedTasks().forEach(System.out::println);

        // Print history
        System.out.println("\nHistory:");
        manager.getHistory().forEach(System.out::println);
    }
}