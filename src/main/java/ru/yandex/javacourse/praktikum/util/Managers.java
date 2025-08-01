// src/main/java/ru/yandex/praktikum/util/Managers.java
package ru.yandex.praktikum.util;

import ru.yandex.praktikum.service.FileBackedTaskManager;
import ru.yandex.praktikum.service.HistoryManager;
import ru.yandex.praktikum.service.InMemoryHistoryManager;
import ru.yandex.praktikum.service.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}