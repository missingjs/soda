import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

class NumMatrix(matrix: Array[Array[Int]]) {

  private var bit: Array[Array[Int]] = Array.empty
  private var rows: Int = 0
  private var cols: Int = 0

  val _init = () => {
    rows = matrix.length + 1
    cols = matrix(0).length + 1
    bit = Array.ofDim[Int](rows, cols)
    for (i <- 0 until rows - 1) {
      for (j <- 0 until cols - 1) {
        val value = matrix(i)(j)
        matrix(i)(j) = 0
        update(i, j, value)
      }
    }
  }
  _init()

  def update(row: Int, col: Int, `val`: Int): Unit = {
    val diff = `val` - matrix(row)(col)
    matrix(row)(col) = `val`
    var i = row + 1
    while (i < rows) {
      var j = col + 1
      while (j < cols) {
        bit(i)(j) += diff
        j += (j & -j)
      }
      i += (i & -i)
    }
  }

  def sumRegion(row1: Int, col1: Int, row2: Int, col2: Int): Int = {
    query(row1,col1) - query(row1,col2+1) - query(row2+1,col1) + query(row2+1,col2+1)
  }

  def query(r: Int, c: Int): Int = {
    var res = 0
    var i = r
    while (i > 0) {
      var j = c
      while (j > 0) {
        res += bit(i)(j)
        j -= (j & -j)
      }
      i -= (i & -i)
    }
    res
  }

}

class Query2d {
  def get(): () => Unit = {
    () => {
      // val work = GenericTestWork.create1(Solution.eq)
      val work = GenericTestWork.forStruct(typeOf[NumMatrix])
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

object Query2d extends App {
  new Query2d().get().apply()
}
