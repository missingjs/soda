import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def groupByLength(strs: Array[String]): Array[Array[String]] = {
    val group = mutable.Map[Int, ArrayBuffer[String]]()
    for (s <- scala.util.Random.shuffle(strs.toList)) {
      group.getOrElseUpdate(s.length, ArrayBuffer[String]()) += s
    }
    scala.util.Random.shuffle(group.keys.toList).map(group(_).toArray).toArray
  }
}

class List2d {
  def get(): () => Unit = {
    () => {
      val work = GenericTestWork.create1(Solution.groupByLength)
      // val work = GenericTestWork.forStruct(typeOf[STRUCT])
      // val work = TestWork.forObject(typeOf[Solution], "METHOD")
      // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
      // val work = TestWork.createN((...)=>R)
      // val work = TestWork.forStruct(typeOf[STRUCT])
      work.setValidator(Validators.forArray2d[String](false, false))
      work.compareSerial = true
      work.run()
    }
  }
}

object List2d extends App {
  new List2d().get().apply()
}
