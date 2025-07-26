package ru.yandex.javacourse.model;

public class Subtask extends Task {
<<<<<<< HEAD
    private int epicId;  // Изменили с String на int

=======
    private int epicId;

    // Конструктор для создания новых подзадач (без ID)
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

<<<<<<< HEAD
=======
    // Конструктор для подзадач с существующим ID (например, при загрузке из хранилища)
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

<<<<<<< HEAD
    public int getEpicId() {  // Возвращаем int
        return epicId;
    }

    public void setEpicId(int epicId) {  // Принимаем int
=======
    // Геттер и сеттер для epicId
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
<<<<<<< HEAD
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
=======
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}