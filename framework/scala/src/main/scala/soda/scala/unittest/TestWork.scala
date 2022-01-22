package soda.scala.unittest

import scala.io.StdIn.readLine

class TestWork {
  def run() = {
    var input = LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    System.err.println("from scala: " + input);
//    println(input)
  }
}
