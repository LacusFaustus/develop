// src/main/java/ru/yandex/praktikum/service/HistoryManager.java
package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}