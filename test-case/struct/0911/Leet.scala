import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

class TopVotedCandidate(_persons: Array[Int], _times: Array[Int]) {

  val N = _persons.length
  val times = _times
  val persons = _persons
  val winner = Array.fill(N)(0)

  (() => {
    val counter = Array.fill(N+1)(0)
    var win = 0
    for (i <- 0 until N) {
      counter(persons(i)) += 1
      if (counter(persons(i)) >= counter(win)) {
        win = persons(i)
      }
      winner(i) = win
    }
  })()

  def q(t: Int): Int = {
    if (t >= times(times.length-1)) {
      return winner(N-1)
    }
    var (low, high) = (0, N-1)
    while (low < high) {
      val mid = (low + high) / 2
      if (t <= times(mid)) {
        high = mid
      } else {
        low = mid + 1
      }
    }
    if (t == times(low)) winner(low) else winner(low-1)
  }

}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    // val work = GenericTestWork.create1(Solution.eq)
    val work = GenericTestWork.forStruct(typeOf[TopVotedCandidate])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])
    // work.setValidator((R, R) => Boolean)
    work.compareSerial = true
    work.run(text)
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
