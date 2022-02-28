import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def containsNearbyAlmostDuplicate(nums: Array[Int], k: Int, t: Int): Boolean = {
    val imap = mutable.TreeMap[Long,Int]()
    var i = 0
    var j = 0
    while (j < nums.size) {
      if (j - i <= k) {
        val value = nums(j)
        j += 1
        val lower = value - t
        val upper = value + t
        imap.minAfter(lower) match {
          case Some((key, _)) => {
            if (key <= upper) {
              return true
            }
          }
          case None =>
        }
        imap(value) = imap.getOrElse(value, 0) + 1
      } else {
        val value = nums(j)
        i += 1
        val c = imap.getOrElse(value, 0) - 1
        if (c == 0) {
          imap -= value
        } else {
          imap(value) = c
        }
      }
    }
    false
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    val work = GenericTestWork.create3(Solution.containsNearbyAlmostDuplicate)
    // val work = GenericTestWork.forStruct(typeOf[STRUCT])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
