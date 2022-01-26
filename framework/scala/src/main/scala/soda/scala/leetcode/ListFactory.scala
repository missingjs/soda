package soda.scala.leetcode

import scala.collection.mutable.ArrayBuffer

object ListFactory {

  def create(values: List[Int]): ListNode = {
    val head = new ListNode()
    var tail = head
    for (value <- values) {
      val node = new ListNode(value)
      tail.next = node
      tail = node
    }
    head.next
  }

  def dump(head: ListNode): List[Int] = {
    var p = head
    val list = ArrayBuffer[Int]()
    while (p != null) {
      list += p.x
      p = p.next
    }
    list.toList
  }

}
