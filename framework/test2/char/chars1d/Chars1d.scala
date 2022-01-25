import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def doubleList(chars: Array[Char]): List[Char] = {
    var res = mutable.ArrayBuffer[Char]()
    for (ch <- chars) {
      res += ch
    }
    for (ch <- chars) {
      res += ch
    }
    return res.toList
  }
}

class Chars1d {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "doubleList")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work
  }
}

object Chars1d extends App {
  new Chars1d().get().run()
}
