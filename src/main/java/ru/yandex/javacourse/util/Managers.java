package ru.yandex.javacourse.util;

import ru.yandex.javacourse.service.HistoryManager;
import ru.yandex.javacourse.service.TaskManager;
import ru.yandex.javacourse.service.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new ru.yandex.javacourse.util.InMemoryHistoryManager();
    }
}