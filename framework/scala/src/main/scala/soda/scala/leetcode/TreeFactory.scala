package soda.scala.leetcode

import scala.collection.mutable
import scala.util.control.Breaks.{break, breakable}

object TreeFactory {

  def create(data: List[Option[Int]]): TreeNode = {
    if (data.isEmpty || data.head.isEmpty) {
      return null
    }

    val root = new TreeNode(data.head.get)
    val qu = mutable.ArrayDeque[TreeNode]()
    qu.append(root)
    val arr = data.toArray
    var i = 1
    breakable(while (i < arr.length) {
      val node = qu.removeHead()
      if (arr(i).isDefined) {
        node.left = new TreeNode(arr(i).get)
        qu.append(node.left)
      }
      i += 1
      if (i == arr.length) {
        break()
      }
      if (arr(i).isDefined) {
        node.right = new TreeNode(arr(i).get)
        qu.append(node.right)
      }
      i += 1
    })
    root
  }

  def dump(root: TreeNode): List[Option[Int]] = {
    if (root == null)
      return List.empty

    var curr = mutable.ArrayBuffer[TreeNode]()
    var next = mutable.ArrayBuffer[TreeNode]()
    var order = mutable.ArrayBuffer[TreeNode]()
    curr += root
    while (curr.nonEmpty) {
      next.clear()
      for (node <- curr) {
        order += node
        if (node != null) {
          next += node.left
          next += node.right
        }
      }
      val temp = curr
      curr = next
      next = temp
    }

    var i = order.size - 1
    while (order(i) == null) {
      i -= 1
    }

    val data = mutable.ArrayBuffer[Option[Int]]()
    for (j <- 0 to i) {
      if (order(j) != null) {
        data += Some(order(j).value)
      } else {
        data += None
      }
    }
    data.toList
  }

}
