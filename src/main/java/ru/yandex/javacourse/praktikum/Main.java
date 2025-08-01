package ru.yandex.javacourse.praktikum;

import ru.yandex.javacourse.praktikum.model.Epic;
import ru.yandex.javacourse.praktikum.model.Subtask;
import ru.yandex.javacourse.praktikum.model.Task;
import ru.yandex.javacourse.praktikum.service.TaskManager;
import ru.yandex.javacourse.praktikum.util.Managers;

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

                int subtaskId1 = manager.createSubtask(subtask1);
                int subtaskId2 = manager.createSubtask(subtask2);

                // Просмотр задач для заполнения истории
                manager.getTaskById(taskId1);
                manager.getEpicById(epicId1);
                manager.getSubtaskById(subtaskId1);
                manager.getTaskById(taskId2);
                manager.getSubtaskById(subtaskId2);

                System.out.println("History:");
                manager.getHistory().forEach(System.out::println);
        }
}