package com.budgetbuddy.util;

import com.budgetbuddy.model.Expense;
import com.budgetbuddy.service.ExpenseService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class CsvUtil {
    public static void exportCsv(List<Expense> expenses, Path path) {
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            bw.write("id,date,category,note,amount\n");
            for (Expense e : expenses) {
                String line = String.join(",",
                        escape(e.getId()),
                        e.getDate().toString(),
                        escape(e.getCategory()),
                        escape(e.getNote()),
                        String.valueOf(e.getAmount()));
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Export CSV failed: " + ex.getMessage(), ex);
        }
    }

    public static int importCsv(ExpenseService service, Path path) {
        int count = 0;
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length < 5) continue;
                LocalDate date = LocalDate.parse(parts[1]);
                String category = parts[2];
                String note = parts[3];
                double amount = Double.parseDouble(parts[4]);
                service.add(date, category, note, amount);
                count++;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Import CSV failed: " + ex.getMessage(), ex);
        }
        return count;
    }

    private static String escape(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static String[] parseCsvLine(String line) {
        java.util.List<String> out = new java.util.ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"'); i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(sb.toString()); sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        out.add(sb.toString());
        return out.toArray(new String[0]);
    }
}
