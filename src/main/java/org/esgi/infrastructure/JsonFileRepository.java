package org.esgi.infrastructure;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.esgi.domain.models.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonFileRepository {
    private final String path;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

    public JsonFileRepository(String path) {
        this.path = path;
    }

    public List<Task> load() throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

                    @Override
                    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), formatter);
                    }
                })
                .registerTypeAdapter(Optional.class, new JsonDeserializer<Optional<LocalDateTime>>() {
                    @Override
                    public Optional<LocalDateTime> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        if (jsonElement.isJsonNull()) {
                            return Optional.empty();
                        }
                        return Optional.of(LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), formatter));
                    }
                })
                .serializeNulls()
                .create();

        JsonReader reader = new JsonReader(new FileReader(path));
        Task[] tasks = gson.fromJson(reader, Task[].class);
        return tasks == null ? null : new ArrayList<>(List.of(tasks));
    }

    public void save(List<Task> tasks) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime o, Type type, JsonSerializationContext jsonSerializationContext) {
                        return new JsonPrimitive(o.format(formatter));
                    }
                })
                .registerTypeAdapter(Optional.class, new JsonSerializer<Optional<LocalDateTime>>() {
                    @Override
                    public JsonElement serialize(Optional<LocalDateTime> o, Type type, JsonSerializationContext jsonSerializationContext) {
                        if (o.isEmpty()) {
                            return JsonNull.INSTANCE;
                        }
                        return new JsonPrimitive(o.get().format(formatter));
                    }
                })
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        FileWriter fileWriter = new FileWriter(path);
        gson.toJson(tasks, fileWriter);
        fileWriter.close();
    }
}
