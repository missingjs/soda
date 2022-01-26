import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  case class Info(sum: Int, product: Int, maxDepth: Int)

  def depthSumInverse(nestedList: List[NestedInteger]): Int = {
    val info = getInfo(nestedList, 1)
    (info.maxDepth + 1) * info.sum - info.product
  }

  def getInfo(nestedList: List[NestedInteger], depth: Int): Info = {
    var sum, product = 0
    var maxDepth = depth
    for (ni <- nestedList) {
      if (ni.isInteger) {
        val value = ni.getInteger
        sum += value
        product += value * depth
        maxDepth = maxDepth max depth
      } else {
        val res = getInfo(ni.getList.toList, depth+1)
        sum += res.sum
        product += res.product
        maxDepth = maxDepth max res.maxDepth
      }
    }
    Info(sum, product, maxDepth)
  }
}

class Solution {}

class Leet {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "depthSumInverse")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Leet extends App {
  new Leet().get().run()
}
