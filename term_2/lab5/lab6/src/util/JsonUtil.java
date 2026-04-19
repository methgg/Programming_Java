package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import exceptions.ErrorMessages;
import models.MusicBand;

/**
 * Класс для записи в файл и чтения из файла формата Json
 */
public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public static LinkedHashMap<Long, MusicBand> readFromFile(String filename) {
        LinkedHashMap<Long, MusicBand> map = new LinkedHashMap<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] bytes = bis.readAllBytes();
            String json = new String(bytes);

            Type type = new TypeToken<LinkedHashMap<Long, MusicBand>>(){}.getType();
            map = gson.fromJson(json, type);

        } catch (FileNotFoundException e) {
            System.out.println(ErrorMessages.fileNotFound(filename));
        } catch (IOException e) {
            System.out.println(ErrorMessages.fileReadError(e.getMessage()));
        } catch (JsonSyntaxException e) {
            System.out.println(ErrorMessages.jsonSyntaxError(e.getMessage()));
        }

        return map != null ? map : new LinkedHashMap<>();
    }

    public static void writeToFile(String filename, LinkedHashMap<Long, MusicBand> map) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
             OutputStreamWriter writer = new OutputStreamWriter(bos, StandardCharsets.UTF_8)) {
            gson.toJson(map, writer);
        } catch (IOException e) {
            System.out.println(ErrorMessages.fileSaveError(e.getMessage()));
        }
    }
    public static Gson getGson() {
        return gson;
    }
}
