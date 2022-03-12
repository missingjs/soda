package soda.groovy.web

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void log(String level, String message) {
        var dt = LocalDateTime.now().format(formatter);
        var threadName = Thread.currentThread().getName();
        System.err.printf("%s [%s] [%s] %s%n", dt, threadName, level, message);
    }

    public static void info(String message) {
        log("INFO", message);
    }

    public static void infof(String format, Object... args) {
        info(String.format(format, args));
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void errorf(String format, Object... args) {
        error(String.format(format, args));
    }

    public static void exception(String message, Throwable ex) {
        errorf("%s\n%s", message, toString(ex));
    }

    private static String toString(Throwable ex) {
        var out = new ByteArrayOutputStream();
        var pw = new PrintStream(out);
        ex.printStackTrace(pw);
        pw.flush();
        return out.toString(StandardCharsets.UTF_8);
    }

}
