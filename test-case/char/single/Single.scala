import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

object Solution {
  def nextChar(ch: Char): Char = (ch + 1).toChar
}

class Single extends (String => String) {
  override def apply(text: String): String = {
    val work = GenericTestWork.create1(Solution.nextChar)
    work.compareSerial = true
    work.run(text)
  }
}

object Single extends App {
  println(new Single()(Utils.fromStdin()))
}
