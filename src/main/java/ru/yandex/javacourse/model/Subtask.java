package ru.yandex.javacourse.model;

public class Subtask extends Task
{
    private int epicId;

    // Конструктор для создания новой подзадачи
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    // Конструктор для обновления существующей подзадачи
    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId()
    {
        return epicId;
    }

    public void setEpicId(int epicId)
    {
        this.epicId = epicId;
    }

    @Override
    public String toString()
    {
        return "Subtask{"
                + "name='" + getName() + '\''
                + ", description='" + getDescription() + '\''
                + ", id=" + getId()
                + ", status=" + getStatus()
                + ", epicId=" + epicId
                + '}';
    }
}