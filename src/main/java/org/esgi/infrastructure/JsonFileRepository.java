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

public class JsonFileRepository {
    private final String path;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

    public JsonFileRepository(String path) {
        this.path = path;
    }

    public List<Task> load() throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), formatter))
                .create();

        JsonReader reader = new JsonReader(new FileReader(path));
        Task[] tasks = gson.fromJson(reader, Task[].class);
        return tasks == null ? null : new ArrayList<>(List.of(tasks));
    }

    public void save(List<Task> tasks) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (o, type, jsonSerializationContext) -> new JsonPrimitive(o.format(formatter)))
                .setPrettyPrinting()
                .create();
        FileWriter fileWriter = new FileWriter(path);
        gson.toJson(tasks, fileWriter);
        fileWriter.close();
    }
}
