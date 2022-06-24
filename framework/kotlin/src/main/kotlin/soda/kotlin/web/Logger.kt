package soda.kotlin.web

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal object Logger {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun log(level: String, message: String) {
        val dt = LocalDateTime.now().format(formatter)
        val threadName = Thread.currentThread().name
        System.err.println("$dt [$threadName] [$level] $message")
    }

    fun info(message: String) = log("INFO", message)

    fun error(message: String) = log("ERROR", message)

    fun exception(message: String, ex: Throwable) {
        error("$message\n${WebUtils.toString(ex)}")
    }

}