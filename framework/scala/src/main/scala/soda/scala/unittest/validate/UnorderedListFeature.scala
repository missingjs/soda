package soda.scala.unittest.validate

class UnorderedListFeature[T](elemFeat: ObjectFeature[T]) extends ObjectFeature[List[T]] {

  override def hash(obj: List[T]): Long = {
    obj.map(elemFeat.hash).sorted.foldLeft(0L)((a, b) => a * 133 + b)
  }

  override def isEqual(a: List[T], b: List[T]): Boolean = {
    if (a.size != b.size) {
      return false
    }
    val xmap = new XMap[T, Int](elemFeat)
    for (e <- a) {
      xmap.put(e, xmap.getOrElse(e, 0) + 1)
    }
    for (e <- b) {
      if (!xmap.contains(e)) {
        return false
      }
      val c = xmap.get(e).get - 1
      if (c == 0) {
        xmap.remove(e)
      } else {
        xmap.put(e, c)
      }
    }
    true
  }

}
