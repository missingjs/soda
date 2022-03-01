import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

object Solution {
  def moveZeroes(nums: Array[Int]): Unit = {
    var p = 0
    for (i <- 0 until nums.size) {
      if (nums(i) != 0) {
        if (i != p) {
          val temp = nums(p)
          nums(p) = nums(i)
          nums(i) = temp
        }
        p += 1
      }
    }
    while (p < nums.size) {
      nums(p) = 0
      p += 1
    }
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    // * create work by method with N number of parameter
    // val work = GenericTestWork.createN(Solution.METHOD)
    // * OR, by method without return value and has only one parameter
    // val work = GenericTestWork.create1u(Solution.METHOD_WITHOUT_RETURN)

    val work = GenericTestWork.create1u(Solution.moveZeroes)

    // val work = GenericTestWork.forStruct(typeOf[STRUCT])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])

    // * setup validator
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
