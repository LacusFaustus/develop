package ru.yandex.javacourse.util;

import java.io.File;

import ru.yandex.javacourse.service.FileBackedTaskManager;
import ru.yandex.javacourse.service.HistoryManager;
import ru.yandex.javacourse.service.InMemoryHistoryManager;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.TaskManager;

public class Managers {
    public static TaskManager getDefault ( )
    {
        return new InMemoryTaskManager ( );
    }

    public static HistoryManager getDefaultHistory ( )
    {
        return new InMemoryHistoryManager ( );
    }

    public static FileBackedTaskManager getFileBackedTaskManager ( File file )
    {
        return new FileBackedTaskManager ( file );
    }
}