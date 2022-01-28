import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  def reverse(_head: ListNode): ListNode = {
    var head = _head
    var h: ListNode = null
    while (head != null) {
      val next = head.next
      head.next = h
      h = head
      head = next
    }
    h
  }
}

class Solution {}

class Reverse extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "reverse")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Reverse extends App {
  println(new Reverse()(Utils.fromStdin()))
}
