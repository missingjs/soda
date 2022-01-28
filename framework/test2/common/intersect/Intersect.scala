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

class Intersect extends (String => String) {
  override def apply(text: String): String = {
    val work = TestWork.forObject(typeOf[Solution], "intersection")
    // val work = TestWork.forStruct(...)
    work.setValidator(Validators.forArray[Int](false))
    work.compareSerial = true
    work.run(text)
  }
}

object Intersect extends App {
  println(new Intersect()(Utils.fromStdin()))
}
