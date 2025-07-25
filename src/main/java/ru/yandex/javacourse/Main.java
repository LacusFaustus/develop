package main.java.ru.yandex.javacourse;

import main.java.ru.yandex.javacourse.model.*;
import main.java.ru.yandex.javacourse.service.TaskManager;
import main.java.ru.yandex.javacourse.util.Managers;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создаем задачи
        Task task = new Task("Помыть посуду", "Помыть всю посуду на кухне");
        int taskId = manager.createTask(task);

        // Создаем эпик
        Epic epic = new Epic("Переезд", "Организация переезда в другой город");
        int epicId = manager.createEpic(epic);

        // Создаем подзадачи
        Subtask subtask1 = new Subtask("Собрать коробки", "Купить и собрать коробки", epicId);
        int subtask1Id = manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Упаковать вещи", "Аккуратно упаковать все вещи", epicId);
        int subtask2Id = manager.createSubtask(subtask2);

        // Выводим информацию
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
        System.out.println("Подзадачи эпика: " + manager.getSubtasksByEpicId(epicId));
        System.out.println("История: " + manager.getHistory());
    }
}