import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  def mirror(root: TreeNode): TreeNode = {
    if (root == null) {
      return null
    }
    mirror(root.left)
    mirror(root.right)
    val temp = root.left
    root.left = root.right
    root.right = temp
    root
  }
}

class Solution {}

class Mirror {
  def get(): TestWork = {
    val work = TestWork.forObject(typeOf[Solution], "mirror")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Mirror extends App {
  new Mirror().get().run()
}
