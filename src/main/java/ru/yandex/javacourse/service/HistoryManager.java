package ru.yandex.javacourse.service;

import java.util.List;
import ru.yandex.javacourse.model.Task;

public interface HistoryManager
{
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
    void clear();
}