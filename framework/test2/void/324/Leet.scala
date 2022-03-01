import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

class VirIndex(nums: Array[Int]) {
  def apply(index: Int): Int = nums(mapIndex(index))
  def update(index: Int, value: Int): Unit = nums(mapIndex(index)) = value
  private def mapIndex(i: Int): Int = {
    val n = nums.size
    if ((n&1) == 1 || i > ((n-1)>>1)) {
      (((n-i) << 1) - 1) % n
    } else {
      (n - 2 - (i << 1))
    }
  }
}

object Solution {
  def wiggleSort(nums: Array[Int]): Unit = {
    val vi = new VirIndex(nums)
    val n = nums.size
    quickSelect(vi, 0, n-1, (n-1)/2)
  }

  private def quickSelect(vi: VirIndex, _start: Int, _end: Int, k: Int): Unit = {
    var start = _start
    var end = _end
    while (start < end) {
      val (p0, p1) = partition(vi, start, end)
      if (k >= p0 && k <= p1) {
        return
      }
      if (k > p1) {
        start = p1 + 1
      } else {
        end = p0 - 1
      }
    }
  }

  private def partition(vi: VirIndex, start: Int, end: Int): Tuple2[Int, Int] = {
    val mid = (start + end) / 2
    val pivot = getMedian(vi(start), vi(mid), vi(end))
    var p = start
    var z = end + 1
    var q = start
    while (q < z) {
      if (vi(q) < pivot) {
        val temp = vi(p)
        vi(p) = vi(q)
        vi(q) = temp
        p += 1
        q += 1
      } else if (vi(q) == pivot) {
        q += 1
      } else {
        z -= 1
        val temp = vi(z)
        vi(z) = vi(q)
        vi(q) = temp
      }
    }
    return (p, z-1)
  }

  private def getMedian(a: Int, b: Int, c: Int): Int = {
    if (a >= b) {
      if (b >= c) b else a.min(c)
    } else {
      if (a >= c) a else b.min(c)
    }
  }
}

class Leet extends (String => String) {
  override def apply(text: String): String = {
    // * create work by method with N number of parameter
    // val work = GenericTestWork.createN(Solution.METHOD)
    // * OR, by method without return value and has only one parameter
    // val work = GenericTestWork.create1u(Solution.METHOD_WITHOUT_RETURN)

    val work = GenericTestWork.create1u(Solution.wiggleSort)

    // val work = GenericTestWork.forStruct(typeOf[STRUCT])
    // val work = TestWork.forObject(typeOf[Solution], "METHOD")
    // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
    // val work = TestWork.createN((...)=>R)
    // val work = TestWork.forStruct(typeOf[STRUCT])

    // * setup validator
    work.setValidator((_, r) => validate(r))
    work.compareSerial = true
    work.run(text)
  }

  private def validate(nums: Array[Int]): Boolean = {
    for (i <- 1 until nums.size) {
      if (i % 2 != 0 && nums(i) <= nums(i-1) || i % 2 == 0 && nums(i) >= nums(i-1)) {
        return false
      }
    }
    true
  }
}

object Leet extends App {
  println(new Leet()(Utils.fromStdin()))
}
