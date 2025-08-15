package com.budgetbuddy;

import com.budgetbuddy.model.Expense;
import com.budgetbuddy.service.ExpenseService;
import com.budgetbuddy.util.ConsoleUI;
import com.budgetbuddy.util.Ansi;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ConsoleUI.banner("BudgetBuddy (Java CLI)");
        ExpenseService service = new ExpenseService();

        while (true) {
            ConsoleUI.menu("\n" + Ansi.BOLD + "1) Add  2) Edit  3) Delete  4) List  5) Summary  6) Export CSV  7) Import CSV  0) Exit" + Ansi.RESET);
            String choice = prompt(Ansi.color("Choose", Ansi.GREEN));
            switch (choice) {
                case "1":
                    addExpense(service);
                    break;
                case "2":
                    editExpense(service);
                    break;
                case "3":
                    deleteExpense(service);
                    break;
                case "4":
                    listExpenses(service);
                    break;
                case "5":
                    monthlySummary(service);
                    break;
                case "6":
                    exportCsv(service);
                    break;
                case "7":
                    importCsv(service);
                    break;
                case "0":
                    service.save();
                    ConsoleUI.ok("Bye!");
                    return;
                default:
                    ConsoleUI.warn("Invalid choice");
            }
        }
    }

    private static String prompt(String label) {
        System.out.print(label + Ansi.color(": ", Ansi.DIM));
        return sc.nextLine().trim();
    }

    private static void addExpense(ExpenseService service) {
        try {
            LocalDate date = LocalDate.parse(prompt("Date (YYYY-MM-DD)"));
            String category = prompt("Category");
            String note = prompt("Note");
            double amount = Double.parseDouble(prompt("Amount"));
            Expense e = service.add(date, category, note, amount);
            ConsoleUI.ok("Added " + Ansi.color(e.toString(), Ansi.CYAN));
        } catch (Exception e) {
            ConsoleUI.err("Failed to add expense: " + e.getMessage());
        }
    }

    private static void editExpense(ExpenseService service) {
        String id = prompt("Expense ID");
        Expense existing = service.findById(id);
        if (existing == null) {
            ConsoleUI.warn("Not found");
            return;
        }
        ConsoleUI.info("Editing " + Ansi.color(existing.toString(), Ansi.CYAN));
        String dateStr = prompt("Date (YYYY-MM-DD, blank to keep)");
        String category = prompt("Category (blank to keep)");
        String note = prompt("Note (blank to keep)");
        String amountStr = prompt("Amount (blank to keep)");

        try {
            LocalDate date = dateStr.isEmpty() ? existing.getDate() : LocalDate.parse(dateStr);
            String cat = category.isEmpty() ? existing.getCategory() : category;
            String nt = note.isEmpty() ? existing.getNote() : note;
            Double amt = amountStr.isEmpty() ? existing.getAmount() : Double.parseDouble(amountStr);

            Expense updated = service.update(id, date, cat, nt, amt);
            ConsoleUI.ok("Updated " + Ansi.color(updated.toString(), Ansi.CYAN));
        } catch (Exception ex) {
            ConsoleUI.err("Failed to edit: " + ex.getMessage());
        }
    }

    private static void deleteExpense(ExpenseService service) {
        String id = prompt("Expense ID");
        boolean ok = service.delete(id);
        if (ok) ConsoleUI.ok("Deleted " + Ansi.color(id, Ansi.MAGENTA));
        else ConsoleUI.warn("Not found");
    }

    private static void listExpenses(ExpenseService service) {
        String ym = prompt("Filter by YYYY-MM (blank for all)");
        String category = prompt("Filter by category (blank for all)");
        List<Expense> list;
        if (!ym.isEmpty() || !category.isEmpty()) {
            YearMonth yearMonth = ym.isEmpty() ? null : YearMonth.parse(ym);
            list = service.find(yearMonth, category.isEmpty() ? null : category);
        } else {
            list = service.all();
        }
        if (list.isEmpty()) {
            ConsoleUI.warn("(empty)");
        } else {
            ConsoleUI.info("Showing " + list.size() + " item(s):");
            for (Expense e : list) {
                System.out.println("  " + Ansi.color(e.toString(), Ansi.CYAN));
            }
        }
    }

    private static void monthlySummary(ExpenseService service) {
        try {
            String ym = prompt("Month (YYYY-MM)");
            YearMonth yearMonth = YearMonth.parse(ym);
            Map<String, Double> sums = service.summaryByCategory(yearMonth);
            double total = 0;
            System.out.println(Ansi.BOLD + "Summary for " + ym + Ansi.RESET);
            for (var e : sums.entrySet()) {
                System.out.printf("  %s : %s%.2f%s%n",
                        Ansi.color(String.format("%-14s", e.getKey()), Ansi.YELLOW),
                        Ansi.GREEN, e.getValue(), Ansi.RESET);
                total += e.getValue();
            }
            System.out.printf(Ansi.BOLD + "  Total: %s%.2f%s%n", Ansi.GREEN, total, Ansi.RESET);
        } catch (Exception ex) {
            ConsoleUI.err("Invalid month format. Use YYYY-MM");
        }
    }

    private static void exportCsv(ExpenseService service) {
        String pathStr = prompt("CSV path to write (e.g., /sdcard/export.csv)");
        Path path = Path.of(pathStr);
        try {
            com.budgetbuddy.util.CsvUtil.exportCsv(service.all(), path);
            ConsoleUI.ok("Exported to " + Ansi.color(path.toString(), Ansi.BLUE));
        } catch (Exception e) {
            ConsoleUI.err("Export failed: " + e.getMessage());
        }
    }

    private static void importCsv(ExpenseService service) {
        String pathStr = prompt("CSV path to read");
        Path path = Path.of(pathStr);
        try {
            int n = com.budgetbuddy.util.CsvUtil.importCsv(service, path);
            ConsoleUI.ok("Imported rows: " + Ansi.color(String.valueOf(n), Ansi.GREEN));
        } catch (Exception e) {
            ConsoleUI.err("Import failed: " + e.getMessage());
        }
    }
}
