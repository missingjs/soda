import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def mergeKLists(lists: Array[ListNode]): ListNode = {
    var pq = PriorityQueue[ListNode]()(Ordering.by(e => -e.x))
    for (L <- lists) {
      if (L != null) {
        pq.enqueue(L)
      }
    }

    var head = new ListNode(0)
    var tail = head
    while (pq.nonEmpty) {
      var t = pq.dequeue()
      var L = t.next
      if (L != null) {
        pq.enqueue(L)
      }
      tail.next = t
      tail = t
    }
    head.next
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    val work = GenericTestWork.create1(Solution.mergeKLists)
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
