package soda.scala.unittest.job

import soda.unittest.CommonJob

object JobEntry {
    def run(cj: CommonJob) = new ConsoleRunner().run(cj)
}
