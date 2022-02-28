import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

class Node(val diff: Double, val value: Int)

object Node {
  def withTarget(value: Int, target: Double): Node = 
    new Node((target - value).abs, value)
}

object Solution {
  def closestKValues(root: TreeNode, target: Double, k: Int): List[Int] = {
    val queue = PriorityQueue[Node]()(Ordering.by((n: Node) => n.diff))
    solve(root, target, k, queue)

    val res = mutable.ArrayBuffer[Int]()
    while (queue.nonEmpty) {
      res += queue.dequeue().value
    }
    return res.toList
  }

  def solve(root: TreeNode, target: Double, k: Int, queue: mutable.PriorityQueue[Node]): Unit = {
    if (root == null) {
      return
    }

    val node = Node.withTarget(root.value, target)
    if (queue.size == k) {
      if (node.diff < queue.head.diff) {
        queue.dequeue()
        queue.enqueue(node)
      }
    } else {
      queue.enqueue(node)
    }

    solve(root.left, target, k, queue)
    solve(root.right, target, k, queue)
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    val work = GenericTestWork.create3(Solution.closestKValues)
    // val work = GenericTestWork.forStruct(typeOf[STRUCT])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])
    work.setValidator(Validators.forList[Int](false))
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
