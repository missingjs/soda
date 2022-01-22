package soda.scala.unittest

import scala.io.StdIn.readLine

class TestWork {
  def run() = {
    var input = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    println(input)
  }
}
