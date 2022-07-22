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
    var nodes = ArrayBuffer[Node]()
    collect(root, nodes, target)
    quickSelect(nodes, 0, nodes.size-1, k)
    nodes.slice(0, k).map(_.value).toList
  }

  private def quickSelect(nodes: ArrayBuffer[Node], _start: Int, _end: Int, index: Int): Unit = {
    var start = _start
    var end = _end
    while (start < end) {
      val mid = (start + end) / 2
      placeMedian3(nodes, start, mid, end)
      val k = partition(nodes, start, end, mid)
      if (k == index) {
        return
      } else if (k > index) {
        end = k - 1
      } else {
        start = k + 1
      }
    }
  }

  private def partition(nodes: ArrayBuffer[Node], start: Int, end: Int, pivot: Int): Int = {
    val d = nodes(pivot).diff
    swap(nodes, pivot, end)
    var p = start
    for (i <- start to end) {
      if (nodes(i).diff < d) {
        if (p != i) {
          swap(nodes, p, i)
        }
        p += 1
      }
    }
    swap(nodes, p, end)
    p
  }

  private def placeMedian3(nodes: ArrayBuffer[Node], start: Int, mid: Int, end: Int): Unit = {
    if (nodes(start).diff > nodes(mid).diff) {
      swap(nodes, start, mid)
    }
    if (nodes(start).diff > nodes(end).diff) {
      swap(nodes, start, end)
    }
    if (nodes(mid).diff > nodes(end).diff) {
      swap(nodes, mid, end)
    }
  }

  private def swap(nodes: ArrayBuffer[Node], i: Int, j: Int): Unit = {
    val temp = nodes(i)
    nodes(i) = nodes(j)
    nodes(j) = temp
  }

  def collect(root: TreeNode, nodes: ArrayBuffer[Node], target: Double): Unit = {
    if (root == null) {
      return
    }
    nodes += Node.withTarget(root.value, target)
    collect(root.left, nodes, target)
    collect(root.right, nodes, target)
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
