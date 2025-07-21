package ru.yandex.javacourse.util;

import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.HistoryManager;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    @Override
    public void add(Task task) {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return List.of();
    }
}
