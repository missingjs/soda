package soda.groovy.web

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private static void log(String level, String message) {
        var dt = LocalDateTime.now().format(formatter)
        var threadName = Thread.currentThread().getName()
        System.err.println("$dt [$threadName] [$level] $message")
    }

    static void info(String message) {
        log("INFO", message)
    }

    static void error(String message) {
        log("ERROR", message)
    }

    static void exception(String message, Throwable ex) {
        error("$message\n${WebUtils.toString(ex)}")
    }

}
