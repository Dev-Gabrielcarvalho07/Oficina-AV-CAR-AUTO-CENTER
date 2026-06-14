package br.com.avcar.config;

import com.google.gson.*;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class JsonConfig {

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class,
            (JsonSerializer<LocalDate>)   (src, t, ctx) -> new JsonPrimitive(src.toString()))
        .registerTypeAdapter(LocalDate.class,
            (JsonDeserializer<LocalDate>) (json, t, ctx) -> LocalDate.parse(json.getAsString()))
        .registerTypeAdapter(LocalTime.class,
            (JsonSerializer<LocalTime>)   (src, t, ctx) -> new JsonPrimitive(src.toString()))
        .registerTypeAdapter(LocalTime.class,
            (JsonDeserializer<LocalTime>) (json, t, ctx) -> LocalTime.parse(json.getAsString()))
        .create();

    private JsonConfig() {}

    public static JsonMapper mapper() {
        return new JsonMapper() {
            @Override
            public String toJsonString(Object obj, Type type) {
                return GSON.toJson(obj, type);
            }

            @Override
            public <T> T fromJsonString(String json, Type targetType) {
                return GSON.fromJson(json, targetType);
            }
        };
    }
}
