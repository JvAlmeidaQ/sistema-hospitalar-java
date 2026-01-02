package br.ufjf.dcc025.model.repository;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GsonConfiguration {

    public static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                // Adaptador para LocalDate
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))

                // Adaptador para LocalDateTime
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))

                // Adaptador para LocalTime
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, context) ->
                        LocalTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_TIME))

                .create();
    }
}