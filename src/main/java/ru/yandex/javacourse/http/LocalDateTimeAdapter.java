package ru.yandex.javacourse.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер для сериализации и десериализации LocalDateTime в JSON.
 */
public final class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    /** Форматтер для преобразования даты и времени. */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Сериализует LocalDateTime в JSON.
     *
     * @param out писатель JSON
     * @param value значение для сериализации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void write(final JsonWriter out, final LocalDateTime value)
            throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(FORMATTER.format(value));
        }
    }

    /**
     * Десериализует LocalDateTime из JSON.
     *
     * @param in читатель JSON
     * @return десериализованный объект LocalDateTime
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public LocalDateTime read(final JsonReader in) throws IOException {
        String dateString = in.nextString();
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateString, FORMATTER);
    }
}