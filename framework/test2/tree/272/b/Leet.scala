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
    nodes.sortBy(_.diff).slice(0, k).map(_.value).toList
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
