// src/main/java/ru/yandex/praktikum/model/Subtask.java
package ru.yandex.javacourse.praktikum.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public Subtask(Subtask other) {
        super(other);
        this.epicId = other.epicId;
    }

    public Subtask(String name, String description, int epicId) {
        this(0, name, description, Status.NEW, epicId);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d",
                id, type, name, status, description, epicId);
    }
}