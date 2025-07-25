package main.java.ru.yandex.javacourse.service;

import java.util.List;
import main.java.ru.yandex.javacourse.model.Task;

public interface HistoryManager
{
    void add(Task task);
    List<Task> getHistory();
}