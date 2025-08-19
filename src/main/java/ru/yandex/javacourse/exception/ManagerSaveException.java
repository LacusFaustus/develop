package ru.yandex.javacourse.exception;

/**
 * Исключение, выбрасываемое при ошибках сохранения в менеджере задач.
 */
public class ManagerSaveException extends RuntimeException {

    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public ManagerSaveException(final String message) {
        super(message);
    }

    /**
     * Создает исключение с указанным сообщением об ошибке и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public ManagerSaveException(final String message, final Throwable cause) {
        super(message, cause);
    }
}