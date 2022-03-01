package soda.scala.unittest

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def add(a: Int): Int = a + 2004
}

class __Bootstrap__ extends (String => String) {
  override def apply(text: String): String = {
    // * create work by method with N number of parameter
    // val work = GenericTestWork.createN(Solution.METHOD)
    // * OR, by method without return value and has only one parameter
    // val work = GenericTestWork.create1u(Solution.METHOD_WITHOUT_RETURN)

    val work = GenericTestWork.create1(Solution.add)

    // val work = GenericTestWork.forStruct(typeOf[STRUCT])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])

    // * setup validator
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object __Bootstrap__ extends App {
  println(new __Bootstrap__()(Utils.fromStdin()))
}
