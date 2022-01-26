import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  def reverseAll(lists: List[ListNode]): List[ListNode] = {
    val res = mutable.ArrayBuffer[ListNode]()
    for (elem <- lists) {
      res += reverse(elem)
    }
    var i = 0
    var j = res.size - 1
    while (i < j) {
      val temp = res(i)
      res(i) = res(j)
      res(j) = temp
      i += 1
      j -= 1
    }
    res.toList
  }

  def reverse(_head: ListNode): ListNode = {
    var h: ListNode = null
    var head = _head
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

class Revall {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "reverseAll")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Revall extends App {
  new Revall().get().run()
}
