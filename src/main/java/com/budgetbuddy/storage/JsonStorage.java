package com.budgetbuddy.storage;

import com.budgetbuddy.model.Expense;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {
    private final Path file;
    private final Gson gson;

    public JsonStorage(Path file) {
        this.file = file;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
                .setPrettyPrinting()
                .create();
    }

    public List<Expense> load() {
        try {
            if (!Files.exists(file)) return new ArrayList<>();
            try (Reader r = Files.newBufferedReader(file)) {
                Type listType = new TypeToken<List<Expense>>(){}.getType();
                List<Expense> list = gson.fromJson(r, listType);
                return list == null ? new ArrayList<>() : list;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + e.getMessage(), e);
        }
    }

    public void save(List<Expense> expenses) {
        try {
            if (!Files.exists(file.getParent())) Files.createDirectories(file.getParent());
            try (Writer w = Files.newBufferedWriter(file)) {
                gson.toJson(expenses, w);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save: " + e.getMessage(), e);
        }
    }

    public Path getFile() { return file; }
}
