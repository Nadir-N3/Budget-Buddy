package com.budgetbuddy.service;

import com.budgetbuddy.model.Expense;
import com.budgetbuddy.storage.JsonStorage;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseService {
    private final JsonStorage storage;
    private final List<Expense> expenses;

    public ExpenseService() {
        Path file = Path.of(System.getProperty("user.home"), ".budgetbuddy", "expenses.json");
        this.storage = new JsonStorage(file);
        this.expenses = new ArrayList<>(storage.load());
    }

    public Expense add(LocalDate date, String category, String note, double amount) {
        Expense e = new Expense(date, category, note, amount);
        expenses.add(e);
        save();
        return e;
    }

    public Expense update(String id, LocalDate date, String category, String note, double amount) {
        Expense e = findById(id);
        if (e == null) return null;
        e.setDate(date);
        e.setCategory(category);
        e.setNote(note);
        e.setAmount(amount);
        save();
        return e;
    }

    public boolean delete(String id) {
        boolean removed = expenses.removeIf(x -> x.getId().equals(id));
        if (removed) save();
        return removed;
    }

    public Expense findById(String id) {
        for (Expense e : expenses) {
            if (e.getId().equals(id) || e.getId().startsWith(id)) return e;
        }
        return null;
    }

    public List<Expense> all() {
        return Collections.unmodifiableList(expenses);
    }

    public List<Expense> find(YearMonth ym, String category) {
        return expenses.stream()
                .filter(e -> ym == null || YearMonth.from(e.getDate()).equals(ym))
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Expense::getDate).thenComparing(Expense::getId))
                .collect(Collectors.toList());
    }

    public Map<String, Double> summaryByCategory(YearMonth ym) {
        return find(ym, null).stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        LinkedHashMap::new,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public void save() { storage.save(expenses); }
}
