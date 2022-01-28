import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  var p = 0

  def deserialize(s: String): NestedInteger = {
    p = 0
    parse(s)
  }

  def parse(s: String): NestedInteger = {
    if (s(p) == '[') {
      p += 1
      val root = DefaultNestedInteger.forList()
      while (s(p) != ']') {
        root.add(parse(s))
        if (s(p) == ',') {
          p += 1
        }
      }
      p += 1
      return root
    }

    var negative = false
    if (s(p) == '-') {
      p += 1
      negative = true
    }

    var value = 0
    while (p < s.length && s(p) >= '0' && s(p) <= '9') {
      value = value * 10 + s(p) - '0'
      p += 1
    }

    if (negative) {
      value = -value
    }
    DefaultNestedInteger.forInt(value)
  }
}

class Solution {}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "deserialize")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
