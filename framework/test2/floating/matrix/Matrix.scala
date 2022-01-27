import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def matrixMultiply(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
    val rows = a.size
    val cols = b(0).size
    val res = Array.ofDim[Double](rows, cols)
    for (i <- 0 until rows) {
      for (j <- 0 until cols) {
        var c: Double = 0.0
        for (k <- b.indices) {
          c += a(i)(k) * b(k)(j)
        }
        res(i)(j) = c
      }
    }
    res
  }
}

class Matrix {
  def get(): TestWork = {
    val work = TestWork.forObject(typeOf[Solution], "matrixMultiply")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    // work.compareSerial = true
    work
  }
}

object Matrix extends App {
  new Matrix().get().run()
}
