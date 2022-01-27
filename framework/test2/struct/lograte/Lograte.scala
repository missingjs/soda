import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}
class Logger {

  val msgMap = mutable.Map[String, Int]()
  val limit = 10
  var lastTimestamp = -limit

  def shouldPrintMessage(timestamp: Int, message: String): Boolean = {
    val T = lastTimestamp
    lastTimestamp = timestamp
    if (timestamp - T >= 10) {
      msgMap.clear()
      msgMap(message) = timestamp
      return true
    }
    if (msgMap.contains(message) && timestamp - msgMap(message) < limit) {
      return false
    }
    msgMap(message) = timestamp
    true
  }
}

class Lograte {
  def get(): () => Unit = {
    () => {
      // val work = GenericTestWork.create1(Solution.eq)
      val work = GenericTestWork.forStruct(typeOf[Logger])
      // val work = TestWork.forObject(typeOf[Solution], "METHOD")
      // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
      // val work = TestWork.createN((...)=>R)
      // val work = TestWork.forStruct(typeOf[STRUCT])
      // work.setValidator((R, R) => Boolean)
      work.compareSerial = true
      work.run()
    }
  }
}

object Lograte extends App {
  new Lograte().get().apply()
}
