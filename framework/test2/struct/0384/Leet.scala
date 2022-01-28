import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}
class Solution(_nums: Array[Int]) {

  private val original = _nums

  def reset(): Array[Int] = {
    original.toArray
  }

  def shuffle(): Array[Int] = {
    val res = reset()
    val rand = new util.Random
    for (s <- res.length until 0 by -1) {
      val i = rand.nextInt(s)
      val j = s - 1
      if (i != j) {
        val temp = res(i)
        res(i) = res(j)
        res(j) = temp
      }
    }
    res
  }

}

class Leet extends (String => String) {
  import play.api.libs.json._
  def validate(work: GenericTestWork[JsValue], expect: JsValue, result: JsValue): Boolean = {
    val commands = work.arguments.head.asInstanceOf[List[String]]
    for (i <- commands.indices) {
      if (commands(i) == "shuffle") {
        val evalues = expect(i).as[List[Int]]
        val rvalues = result(i).as[List[Int]]
        val counts = mutable.Map[Int,Int]()
        for (a <- evalues) {
          counts(a) = counts.getOrElse(a, 0) + 1
        }
        for (b <- rvalues) {
          val c = counts.getOrElse(b, 0) - 1
          if (c < 0) {
            return false
          }
        }
      }
    }
    true
  }
  override def apply(text: String): String = {
    // val work = GenericTestWork.create1(Solution.eq)
    val work = GenericTestWork.forStruct(typeOf[Solution])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])
    // work.setValidator((R, R) => Boolean)
    work.setValidator((e, r) => validate(work, e, r))
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
