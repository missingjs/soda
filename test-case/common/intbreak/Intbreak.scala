import soda.scala.unittest._

import scala.reflect.runtime.universe._

class Solution {}
object Solution {
  var memo: Array[Int] = Array.empty
  def integerBreak(n: Int): Int = {
    memo = Array.fill(59)(0)
    solve(n)
  }

  def solve(n: Int): Int = {
    if (n == 1) return 1
    if (memo(n) > 0) return memo(n)
    var res = 0
    for (i <- 1 until n) {
      res = res max (i * (n-i))
      res = res max (i * solve(n-i))
    }
    memo(n) = res
    res
  }
}

class Intbreak extends (String => String) {
  override def apply(text: String): String = {
    // val work = TestWork.forObject(typeOf[Solution], "integerBreak")
    // val work = GenericTestWork.create1(Solution.integerBreak)
    // val work = TestWork.create1(Solution.integerBreak)
    var work = GenericTestWork.create1(Solution.integerBreak)
    work.compareSerial = true
    work.run(text)
  }
}

object Intbreak extends App {
  println(new Intbreak()(Utils.fromStdin()))
}
