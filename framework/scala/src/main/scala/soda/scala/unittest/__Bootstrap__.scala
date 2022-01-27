package soda.scala.unittest

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}
object Solution {}

class __Bootstrap__ {
  def get(): () => Unit = {
    () => {
      val work = GenericTestWork.create1(Solution.eq)
      // val work = GenericTestWork.forStruct(typeOf[STRUCT])
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

object __Bootstrap__ extends App {
  new __Bootstrap__().get().apply()
}
