package com.budgetbuddy.model;

import java.time.LocalDate;
import java.util.UUID;

public class Expense {
    private String id;
    private LocalDate date;
    private String category;
    private String note;
    private double amount;

    public Expense() {}

    public Expense(LocalDate date, String category, String note, double amount) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.category = category;
        this.note = note;
        this.amount = amount;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public String getNote() { return note; }
    public double getAmount() { return amount; }

    public void setId(String id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setCategory(String category) { this.category = category; }
    public void setNote(String note) { this.note = note; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %-12s | %-20s | %.2f",
                id.substring(0, 8), date, category, note, amount);
    }
}
