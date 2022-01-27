import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}
object Solution {
  def findLeaves(root: TreeNode): List[List[Int]] = {
    val res = ArrayBuffer[ArrayBuffer[Int]]()
    for (i <- 0 until 100) {
      res.append(ArrayBuffer[Int]())
    }
    val r = solve2(root, res)
    val ans = Array.fill[Array[Int]](r)(null)
    for (i <- 0 until r) {
      ans(i) = res(i).toArray
    }
    ans.map(_.toList).toList
  }

  private def solve2(root: TreeNode, res: ArrayBuffer[ArrayBuffer[Int]]): Int = {
    if (root == null) {
      return 0
    }
    val R = solve2(root.right, res)
    val L = solve2(root.left, res)
    val index = L max R
    res(index) += root.value
    index + 1
  }
}

class Leet {
  def get(): () => Unit = {
    () => {
      val work = GenericTestWork.create1(Solution.findLeaves)
      // val work = GenericTestWork.forStruct(typeOf[STRUCT])
      // val work = TestWork.forObject(typeOf[Solution], "METHOD")
      // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
      // val work = TestWork.createN((...)=>R)
      // val work = TestWork.forStruct(typeOf[STRUCT])
      work.setValidator(Validators.forList2d[Int](true,false))
      work.compareSerial = true
      work.run()
    }
  }
}

object Leet extends App {
  new Leet().get().apply()
}
