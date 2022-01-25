import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

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

class Intbreak {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "integerBreak")
    work.compareSerial = true
    work
  }
}

object Intbreak extends App {
  new Intbreak().get().run()
}
