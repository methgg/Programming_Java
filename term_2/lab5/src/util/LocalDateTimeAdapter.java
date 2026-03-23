package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gson-конвертер для сериализации/десериализации {@link LocalDateTime}.
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(formatter)); // yyyy-MM-ddTHH:mm:ss
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String str = in.nextString();
        if (str.isEmpty()) return null;

        // Поддержка формата yyyy-MM-dd без времени
        if (str.length() == 10) { // "yyyy-MM-dd"
            return LocalDateTime.parse(str + "T00:00:00", formatter);
        }

        return LocalDateTime.parse(str, formatter);
    }
}