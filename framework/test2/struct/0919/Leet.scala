import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

class CBTInserter(_root: TreeNode) {

  val root = _root
  val qu = ArrayDeque[TreeNode]()

  val _init = () => {
    qu.append(root)
    breakable {
      while (qu.nonEmpty) {
        val node = qu.head
        if (node.left == null) {
          break()
        }
        qu.append(node.left)
        if (node.right == null) {
          break()
        }
        qu.append(node.right)
        qu.removeHead()
      }
    }
  }
  _init()

  def insert(`val`: Int): Int = {
    val node = new TreeNode(`val`)
    val head = qu.head
    qu.append(node)
    if (head.left == null) {
      head.left = node
    } else {
      head.right = node
      qu.removeHead()
    }
    head.value
  }

  def get_root(): TreeNode = {
    root
  }

}

class Leet {
  def get(): () => Unit = {
    () => {
      // val work = GenericTestWork.create1(Solution.eq)
      val work = GenericTestWork.forStruct(typeOf[CBTInserter])
      // val work = TestWork.forObject(typeOf[Solution], "METHOD")
      // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
      // val work = TestWork.createN((...)=>R)
      // val work = TestWork.forStruct(typeOf[STRUCT])
      // work.setValidator((R, R) => Boolean)
      work.compareSerial = true
      work.run()
    }
  }
}

object Leet extends App {
  new Leet().get().apply()
}
