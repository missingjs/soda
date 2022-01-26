package soda.scala.unittest

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
// object Solution { ... }

class Solution {}

class __Bootstrap__ {
  def get(): TestWork = {
    val work = TestWork.forCompanion(typeOf[Solution], "METHOD")
    // val work = TestWork.forStruct(typeOf[STRUCT])
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object __Bootstrap__ extends App {
  new __Bootstrap__().get().run()
}
