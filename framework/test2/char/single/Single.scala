import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def nextChar(ch: Char): Char = (ch + 1).toChar
}

class Single {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "nextChar")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Single extends App {
  new Single().get().run()
}
