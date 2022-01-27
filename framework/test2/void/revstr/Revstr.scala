import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  def reverseString(s: Array[Char]): Unit = {
    var i = 0
    var j = s.length - 1
    while (i < j) {
      val temp = s(i)
      s(i) = s(j)
      s(j) = temp
      i += 1
      j -= 1
    }
  }
}

class Revstr {
  def get(): ()=>Unit = {
    () => {
      val work = GenericTestWork.create1u(Solution.reverseString)
      work.compareSerial = true
      work.run()
    }
  }
}

object Revstr extends App {
  new Revstr().get().apply()
}
