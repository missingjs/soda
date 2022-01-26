package soda.scala.leetcode

import collection.mutable

class DefaultNestedInteger(val isAtomic: Boolean) extends NestedInteger {

  private val list = mutable.ArrayBuffer[NestedInteger]()

  private var value = 0

  override def isInteger: Boolean = isAtomic

  override def getInteger: Int = value

  override def setInteger(i: Int): Unit = value = i

  override def getList: Array[NestedInteger] = list.toArray

  override def add(ni: NestedInteger): Unit = list += ni

}

object DefaultNestedInteger {
  def forInt(i: Int): DefaultNestedInteger = {
    val n = new DefaultNestedInteger(true)
    n.setInteger(i)
    n
  }
  def forList(): DefaultNestedInteger = {
    new DefaultNestedInteger(false)
  }
}
