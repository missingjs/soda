import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def divide(a: Double, b: Double): Double = a / b
}

class Floating {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "divide")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    // work.compareSerial = true
    work
  }
}

object Floating extends App {
  new Floating().get().run()
}
