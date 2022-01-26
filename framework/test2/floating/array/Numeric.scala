import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def multiply(a: Array[Double], b: Array[Double]): List[Double] = {
    val res = Array.fill[Double](a.size)(0.0)
    for (i <- res.indices) {
      res(i) = a(i) * b(i)
    }
    res.toList
  }
}

class Numeric {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "multiply")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    // work.compareSerial = true
    work
  }
}

object Numeric extends App {
  new Numeric().get().run()
}
