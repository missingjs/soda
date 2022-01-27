import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}
object Solution {
  def permutation(chars: Array[Char], n: Int): Array[String] = {
    val res = ArrayBuffer[String]()
    val buf = Array.fill[Char](n)(' ')
    solve(chars, 0, buf, 0, res)
    res.toArray
  }

  private def solve(chars: Array[Char], i: Int, buf: Array[Char], j: Int, res: ArrayBuffer[String]): Unit = {
    if (j == buf.length) {
      res += new String(buf)
      return
    }
    for (k <- i until chars.length) {
      var temp = chars(i)
      chars(i) = chars(k)
      chars(k) = temp
      buf(j) = chars(i)
      solve(chars, i+1, buf, j+1, res)
      temp = chars(i)
      chars(i) = chars(k)
      chars(k) = temp
    }
  }
}

class List {
  def get(): () => Unit = {
    () => {
      val work = GenericTestWork.create2(Solution.permutation)
      // val work = GenericTestWork.forStruct(typeOf[STRUCT])
      // val work = TestWork.forObject(typeOf[Solution], "METHOD")
      // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
      // val work = TestWork.createN((...)=>R)
      // val work = TestWork.forStruct(typeOf[STRUCT])
      work.setValidator(Validators.forArray[String](false))
      work.compareSerial = true
      work.run()
    }
  }
}

object List extends App {
  new List().get().apply()
}
