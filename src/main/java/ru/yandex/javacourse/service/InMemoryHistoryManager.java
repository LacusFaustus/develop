package main.java.ru.yandex.javacourse.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import main.java.ru.yandex.javacourse.model.Task;

public class InMemoryHistoryManager implements HistoryManager
{
    private final List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task)
    {
        if (task != null)
        {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory()
    {
        return new ArrayList<>(history);
    }
}