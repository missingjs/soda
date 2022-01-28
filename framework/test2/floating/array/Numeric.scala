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

class Numeric extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "multiply")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    // work.compareSerial = true
    work.run(text)
  }
}

object Numeric extends App {
  println(new Numeric()(Utils.fromStdin()))
}
