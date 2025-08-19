package ru.yandex.javacourse.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;

/**
 * Адаптер для сериализации и десериализации Duration в JSON.
 * Преобразует Duration в количество минут (long) и обратно.
 */
public final class DurationAdapter extends TypeAdapter<Duration> {

    /**
     * Сериализует Duration в JSON как количество минут.
     *
     * @param out писатель JSON
     * @param value значение Duration для сериализации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void write(final JsonWriter out, final Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toMinutes());
        }
    }

    /**
     * Десериализует Duration из JSON (из количества минут).
     *
     * @param in читатель JSON
     * @return десериализованный объект Duration
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public Duration read(final JsonReader in) throws IOException {
        long minutes = in.nextLong();
        return Duration.ofMinutes(minutes);
    }
}