package ru.yandex.javacourse.util;

<<<<<<< HEAD
import ru.yandex.javacourse.service.*;
=======
import ru.yandex.javacourse.service.HistoryManager;
import ru.yandex.javacourse.service.InMemoryHistoryManager;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.TaskManager;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}