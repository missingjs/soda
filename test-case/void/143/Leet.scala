import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def reorderList(head: ListNode): Unit = {
    var slow = head
    var fast = head
    while (fast.next != null && fast.next.next != null) {
      slow = slow.next
      fast = fast.next.next
    }

    if (slow == fast) {
      return
    }

    val r = reverse(slow.next)
    slow.next = null
    merge(head, r)
  }

  private def reverse(_head: ListNode): ListNode = {
    var q: ListNode = null
    var head = _head
    while (head != null) {
      val next = head.next
      head.next = q
      q = head
      head = next
    }
    q
  }

  private def merge(_L1: ListNode, _L2: ListNode): Unit = {
    var t = new ListNode
    var (h1, h2) = (_L1, _L2)
    while (h1 != null && h2 != null) {
      t.next = h1
      t = h1
      h1 = h1.next
      t.next = h2
      t = h2
      h2 = h2.next
    }
    t.next = if (h1 != null) h1 else h2
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    val work = GenericTestWork.create1u(Solution.reorderList)
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
