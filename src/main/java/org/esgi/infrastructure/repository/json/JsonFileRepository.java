package org.esgi.infrastructure.repository.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.esgi.domain.models.Task;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public List<JsonTask> load() throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, jsonDeserializationContext) -> LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), formatter))
                .serializeNulls()
                .create();

        JsonReader reader = new JsonReader(new FileReader(path));
        JsonTask[] tasks = gson.fromJson(reader, JsonTask[].class);
        return tasks == null ? List.of() : new ArrayList<>(List.of(tasks));
    }

    public void save(List<JsonTask> tasks) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (o, type, jsonSerializationContext) -> new JsonPrimitive(o.format(formatter)))
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        FileWriter fileWriter = new FileWriter(path);
        gson.toJson(tasks, fileWriter);
        fileWriter.close();
    }
}
