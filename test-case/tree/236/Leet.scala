import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def lowestCommonAncestor(root: TreeNode, p: TreeNode, q: TreeNode): TreeNode = {
    val stk = mutable.ArrayBuffer[TreeNode]()
    stk += root
    var last = root
    var foundOne = false
    var index = -1

    if (root == p || root == q) {
      foundOne = true
      index = 0
    }

    while (stk.nonEmpty) {
      val node = stk.last
      if (node.left != null && last != node.left && last != node.right) {
        if (node.left == p || node.left == q) {
          if (!foundOne) {
            index = stk.size
            foundOne = true
          } else {
            return stk(index)
          }
        }
        stk += node.left
      } else if (node.right != null && last != node.right) {
        if (node.right == p || node.right == q) {
          if (!foundOne) {
            index = stk.size
            foundOne = true
          } else {
            return stk(index)
          }
        }
        stk += node.right
      } else {
        last = node
        if (index == stk.size - 1) {
          index -= 1
        }
        stk.dropRightInPlace(1)
      }
    }
    null
  }
}

object Driver {
  def exec(root: TreeNode, p: Int, q: Int): Int = {
    val pNode = findNode(root, p)
    val qNode = findNode(root, q)
    Solution.lowestCommonAncestor(root, pNode, qNode).value
  }

  def findNode(root: TreeNode, value: Int): TreeNode = {
    if (root == null) {
      return null
    }
    if (root.value == value) {
      return root
    }
    findNode(root.left, value) match {
      case null => findNode(root.right, value)
      case n => n
    }
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    val work = GenericTestWork.create3(Driver.exec)
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
