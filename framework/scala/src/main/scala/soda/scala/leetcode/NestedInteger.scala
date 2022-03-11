package soda.scala.leetcode

import collection.mutable

trait NestedInteger {

  private val list = mutable.ArrayBuffer[NestedInteger]()

  private var value = 0

  private var isAtomic = false

  // Return true if this NestedInteger holds a single integer, rather than a nested list.
  def isInteger: Boolean = isAtomic

  // Return the single integer that this NestedInteger holds, if it holds a single integer.
  def getInteger: Int = value

  // Set this NestedInteger to hold a single integer.
  def setInteger(i: Int): Unit = {
    isAtomic = true
    value = i
  }

  // Return the nested list that this NestedInteger holds, if it holds a nested list.
  def getList: Array[NestedInteger] = list.toArray

  // Set this NestedInteger to hold a nested list and adds a nested integer to it.
  def add(ni: NestedInteger): Unit = {
    list += ni
  }

}
