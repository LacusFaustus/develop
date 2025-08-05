package ru.yandex.javacourse.service;

public class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {
    @Override
    protected void initManager() {
        manager = new InMemoryTaskManager();
    }
}