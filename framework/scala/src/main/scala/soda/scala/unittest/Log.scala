package soda.scala.unittest

object Log {
  def info(msg: String): Unit = {
    System.err.println(s"[INFO] $msg")
  }
}
