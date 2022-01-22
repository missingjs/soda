package soda.scala.unittest.job

import scala.io.StdIn.readLine

import soda.unittest.CommonJob

class ConsoleRunner {

    def run(cj: CommonJob) = {
        println(cj.execute(readStdin()))
    }

    private def readStdin(): String = {
        LazyList.continually(readLine()).takeWhile(_ != null).mkString("\n")
    }

}
