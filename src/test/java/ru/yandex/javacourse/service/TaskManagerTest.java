import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void epicShouldNotContainNonExistentSubtasks() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtask(subtask.getId());
        assertFalse(epic.getSubtaskIds().contains(subtask.getId()));
    }

    @Test
    void taskFieldsChangeShouldNotAffectManagerData() {
        Task task = new Task("Original", "Description");
        taskManager.createTask(task);

        task.setName("Modified");
        task.setDescription("Modified description");

        Task savedTask = taskManager.getTask(task.getId());
        assertEquals("Original", savedTask.getName());
        assertEquals("Description", savedTask.getDescription());
    }
}