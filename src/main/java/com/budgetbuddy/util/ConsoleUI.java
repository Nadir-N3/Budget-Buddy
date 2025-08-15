package com.budgetbuddy.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ConsoleUI {
    private ConsoleUI() {}

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void banner(String title) {
        String line = "─".repeat(Math.max(24, title.length() + 8));
        System.out.println(Ansi.color("┌" + line + "┐", Ansi.CYAN));
        String pad = " ".repeat((line.length() - title.length()) / 2);
        System.out.println(Ansi.color("│", Ansi.CYAN) + pad + Ansi.color(title, Ansi.BOLD) + pad + (title.length()%2==0?"":" ") + Ansi.color("│", Ansi.CYAN));
        System.out.println(Ansi.color("└" + line + "┘", Ansi.CYAN));
    }

    public static void menu(String items) {
        System.out.println(Ansi.color(items, Ansi.BLUE));
    }

    public static void info(String msg) {
        System.out.println(ts() + " " + Ansi.color("INFO ", Ansi.BG_BLUE, Ansi.WHITE) + " " + msg);
    }

    public static void ok(String msg) {
        System.out.println(ts() + " " + Ansi.color(" OK  ", Ansi.BG_GREEN, Ansi.BLACK) + " " + msg);
    }

    public static void warn(String msg) {
        System.out.println(ts() + " " + Ansi.color("WARN ", Ansi.BG_YELLOW, Ansi.BLACK) + " " + msg);
    }

    public static void err(String msg) {
        System.out.println(ts() + " " + Ansi.color("ERR  ", Ansi.BG_RED, Ansi.WHITE) + " " + msg);
    }

    private static String ts() {
        return Ansi.color("[" + LocalDateTime.now().format(TS) + "]", Ansi.DIM);
    }
}
