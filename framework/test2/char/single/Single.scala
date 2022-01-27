import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

object Solution {
  def nextChar(ch: Char): Char = (ch + 1).toChar
}

class Single {
  def get(): () => Unit = {
    () => {
      val work = GenericTestWork.create1(Solution.nextChar)
      work.compareSerial = true
      work.run()
    }
  }
}

object Single extends App {
  new Single().get().apply()
}
