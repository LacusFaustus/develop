package ru.yandex.javacourse.praktikum.util;

import ru.yandex.javacourse.praktikum.service.FileBackedTaskManager;
import ru.yandex.javacourse.praktikum.service.HistoryManager;
import ru.yandex.javacourse.praktikum.service.InMemoryHistoryManager;
import ru.yandex.javacourse.praktikum.service.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}