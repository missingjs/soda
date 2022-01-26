import soda.scala.unittest._

import scala.collection.mutable
import scala.reflect.runtime.universe.typeOf

class Solution {}
object Solution {
  def intersection(nums1: Array[Int], nums2: Array[Int]): Array[Int] = {
    if (nums1.size > nums2.size) {
      return intersection(nums2, nums1)
    }

    val mset = mutable.Set[Int]()
    val res = mutable.Set[Int]()
    for (n <- nums1) {
      mset += n
    }
    for (b <- nums2) {
      if (mset.contains(b)) {
        res += b
      }
    }
    res.toArray
  }
}

class Intersect {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "intersection")
    // val work = TestWork.forStruct(...)
    work.setValidator(Validators.forArray[Int](false))
    work.compareSerial = true
    work
  }
}

object Intersect extends App {
  new Intersect().get().run()
}
