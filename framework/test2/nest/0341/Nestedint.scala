import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import util.control.Breaks.{break, breakable}
object Solution {
  def flatNested(niList: List[NestedInteger]): List[Int] = {
    val res = mutable.ArrayBuffer[Int]()
    val iter = new NestedIterator(niList)
    while (iter.hasNext()) {
      res.append(iter.next())
    }
    res.toList
  }
}

class NestedIterator(_nestedList: List[NestedInteger]) {

  class Node(list: List[NestedInteger]) {
    var index: Int = 0
    def isEnd: Boolean = index >= list.size
    def value: Int = current.getInteger
    def current: NestedInteger = list(index)
  }

  val stk = mutable.ArrayDeque[Node]()

  stk.append(new Node(_nestedList))
  locate()

  private def locate(): Unit = {
    breakable {
      while (stk.nonEmpty) {
        if (stk.last.isEnd) {
          stk.removeLast()
          if (stk.nonEmpty) {
            stk.last.index += 1
          }
        } else if (stk.last.current.isInteger) {
          break()
        } else {
          stk.append(new Node(stk.last.current.getList.toList))
        }
      }
    }
  }

  def next(): Int = {
    val value = stk.last.value
    stk.last.index += 1
    locate()
    value
  }
  
  def hasNext(): Boolean = {
    stk.nonEmpty && !stk.last.isEnd
  }
}

class Solution {}

class Nestedint {
  def get(): TestWork = {
    val work = TestWork.forObject(typeOf[Solution], "flatNested")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Nestedint extends App {
  new Nestedint().get().run()
}
