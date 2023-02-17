package org.esgi.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.esgi.domain.models.Task;

import java.io.*;
import java.util.List;

public class JsonFileRepository {
    private String path;

    public JsonFileRepository(String path) {
        this.path = path;
    }

    public List<Task> load() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
        return gson.fromJson(reader, List.class);
    }

    public void save(List<Task> tasks) throws IOException {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        gson.toJson(tasks, new FileWriter(path));
    }
}
