import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def toUpper(matrix: List[List[Char]]): Array[Array[Char]] = {
    val diff = 'a' - 'A'
    val mx = Array.ofDim[Char](matrix.size, matrix(0).size)
    for (i <- matrix.indices) {
      for (j <- matrix(0).indices) {
        mx(i)(j) = (matrix(i)(j) - diff).toChar
      }
    }
    return mx
  }
}

class Chars2d extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "toUpper")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Chars2d extends App {
  println(new Chars2d()(Utils.fromStdin()))
}
