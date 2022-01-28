import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}

class SummaryRanges() {

  private val parent = Array.fill(10003)(0)

  private val ancestorSet = mutable.Set[Int]()

  def addNum(`val`: Int): Unit = {
    val value = `val` + 1
    if (parent(value) != 0) {
      return
    }

    parent(value) = -1
    ancestorSet += value

    var left = value - 1
    val right = value + 1
    if (left > 0 && parent(left) != 0) {
      merge(left, value)
    }
    if (parent(right) != 0) {
      merge(value, right)
    }
  }

  def getIntervals(): Array[Array[Int]] = {
    val ans = ancestorSet.toArray.sortWith(_ < _)
    val res = Array.fill[Array[Int]](ans.size)(null)
    for (i <- res.indices) {
      val start = ans(i)
      val end = start - parent(start) - 1
      res(i) = Array(start-1, end-1)
    }
    res
  }

  def merge(x: Int, y: Int): Unit = {
    val ax = getAncestor(x)
    val ay = getAncestor(y)
    if (ax < ay) {
      mergeAncestor(ax, ay)
    } else {
      mergeAncestor(ay, ax)
    }
  }

  def mergeAncestor(ax: Int, ay: Int): Unit = {
    parent(ax) += parent(ay)
    parent(ay) = ax
    ancestorSet -= ay
  }

  def getAncestor(x: Int): Int = {
    if (parent(x) < 0) {
      x
    } else {
      parent(x) = getAncestor(parent(x))
      parent(x)
    }
  }

}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    var work = GenericTestWork.forStruct(typeOf[SummaryRanges])
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
