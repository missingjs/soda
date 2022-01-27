import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

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
  def get(): ()=>Unit = {
    () => {
      val work = GenericTestWork.create1(Solution.doubleList)
      work.compareSerial = true
      work.run()
    }
  }
}

object Chars1d extends App {
  new Chars1d().get().apply()
}
