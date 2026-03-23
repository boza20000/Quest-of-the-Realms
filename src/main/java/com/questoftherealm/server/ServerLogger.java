package com.questoftherealm.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class ServerLogger {

    private static final String LOG_FILE = "log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ServerLogger INSTANCE = new ServerLogger();
    private final ReentrantLock lock = new ReentrantLock();

    private ServerLogger() {
        try {
            File f = new File(LOG_FILE);
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("[ServerLogger] Could not create log file: " + e.getMessage());
        }
    }

    public static ServerLogger get() {
        return INSTANCE;
    }


    public void info(String message) {
        write("INFO ", message, null);
    }

    public void warn(String message) {
        write("WARN ", message, null);
    }

    public void warn(String message, Throwable t) {
        write("WARN ", message, t);
    }

    public void error(String message) {
        write("ERROR", message, null);
    }

    public void error(String message, Throwable t) {
        write("ERROR", message, t);
    }

    private void write(String level, String message, Throwable t) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String thread = Thread.currentThread().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(timestamp).append("] ")
          .append("[").append(level).append("] ")
          .append("[").append(thread).append("] ")
          .append(message);

        lock.lock();
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(sb);
            if (t != null) {
                t.printStackTrace(pw);
            }
            pw.println();
        } catch (IOException e) {
            System.err.println("[ServerLogger] Failed to write to log: " + e.getMessage());
        } finally {
            lock.unlock();
        }

        System.err.println(sb);
        if (t != null) {
            t.printStackTrace(System.err);
        }
    }
}

