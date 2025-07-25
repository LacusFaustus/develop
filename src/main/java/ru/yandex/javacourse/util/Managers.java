package main.java.ru.yandex.javacourse.util;

import main.java.ru.yandex.javacourse.service.HistoryManager;
import main.java.ru.yandex.javacourse.service.InMemoryHistoryManager;
import main.java.ru.yandex.javacourse.service.InMemoryTaskManager;
import main.java.ru.yandex.javacourse.service.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}