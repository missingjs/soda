import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def reverseVowels(s: String): String = {
    val isv = Array.fill(128)(false)
    val vs = "aeiouAEIOU"
    for (i <- vs.indices) {
      isv(vs(i)) = true
    }
    val buf = s.toCharArray
    var i = 0
    var j = s.size - 1
    while (i < j) {
      while (i < j && !isv(buf(i))) {
        i += 1
      }
      while (i < j && !isv(buf(j))) {
        j -= 1
      }
      if (i < j) {
        val temp = buf(i)
        buf(i) = buf(j)
        buf(j) = temp
        i += 1
        j -= 1
      }
    }
    return new String(buf)
  }
}

class Reverse extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "reverseVowels")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Reverse extends App {
  println(new Reverse()(Utils.fromStdin()))
}
