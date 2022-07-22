import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

object Solution {
  def divide(a: Double, b: Double): Double = a / b
}

class Solution {}

class Floating extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "divide")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    // work.compareSerial = true
    work.run(text)
  }
}

object Floating extends App {
  println(new Floating()(Utils.fromStdin()))
}
