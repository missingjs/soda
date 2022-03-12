package soda.groovy.web

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void log(String level, String message) {
        var dt = LocalDateTime.now().format(formatter);
        var threadName = Thread.currentThread().getName();
        System.err.printf("%s [%s] [%s] %s%n", dt, threadName, level, message);
    }

    static void info(String message) {
        log("INFO", message)
    }

    static void infof(String format, Object... args) {
        info(String.format(format, args))
    }

    static void error(String message) {
        log("ERROR", message)
    }

    static void errorf(String format, Object... args) {
        error(String.format(format, args))
    }

    static void exception(String message, Throwable ex) {
        errorf("%s\n%s", message, WebUtils.toString(ex))
    }

}
