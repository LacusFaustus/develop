package ru.yandex.javacourse.model;

import java.util.Objects;

public class Task {
<<<<<<< HEAD
    private int id;  // Изменили с String на int
    private String name;
    private String description;
=======
    private String name;
    private String description;
    private int id;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    private Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

<<<<<<< HEAD
    // Геттеры и сеттеры остаются без изменений
=======
    // Геттеры и сеттеры
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

<<<<<<< HEAD
    public int getId() {  // Возвращаем int
        return id;
    }

    public void setId(int id) {  // Принимаем int
=======
    public int getId() {
        return id;
    }

    public void setId(int id) {
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

<<<<<<< HEAD
=======
    // equals, hashCode и toString
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status;
    }

    @Override
    public int hashCode() {
<<<<<<< HEAD
        return Objects.hash(id, name, description, status);
=======
        return Objects.hash(name, description, id, status);
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public String toString() {
        return "Task{" +
<<<<<<< HEAD
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
=======
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
                ", status=" + status +
                '}';
    }
}