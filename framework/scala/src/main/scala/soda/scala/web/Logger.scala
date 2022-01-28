package soda.scala.web

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {

  def info(message: String): Unit = {
    log("INFO", message)
  }

  def error(message: String): Unit = {
    log("ERROR", message)
  }

  def exception(message: String, ex: Throwable): Unit = {
    error(s"$message\n${Utils.toString(ex)}")
  }

  private def formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def log(level: String, message: String): Unit = {
    val dt = LocalDateTime.now().format(formatter)
    val threadName = Thread.currentThread().getName
    println(s"$dt [$threadName] [$level] $message")
  }

}
