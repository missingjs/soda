import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  def reverseWords(s: Array[Char]): Unit = {
    if (s.length == 0) {
      return
    }

    reverse(s, 0, s.length-1)

    val N = s.length
    var i = 0
    var j = 0
    while (j < N) {
      if (s(j) == ' ') {
        reverse(s, i, j-1)
        i = j + 1
      }
      j += 1
    }
    if (i < j) {
      reverse(s, i, j-1)
    }
  }

  def reverse(s: Array[Char], _i: Int, _j: Int): Unit = {
    var i = _i
    var j = _j
    while (i < j) {
      val temp = s(i)
      s(i) = s(j)
      s(j) = temp
      i += 1
      j -= 1
    }
  }
}

class Solution {}

class Revword {
  def get(): TestWork = {
    val work = TestWork.forObject(typeOf[Solution], "reverseWords")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Revword extends App {
  new Revword().get().run()
}
