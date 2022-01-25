package soda.scala.unittest

object StrategyFactory {

  def unorderList[T](feat: ObjectFeature[T]): (List[T], List[T]) => Boolean = {
    def func(a: List[T], b: List[T]): Boolean = {
      if (a.size != b.size) {
        return false
      }
      val xmap = new XMap[T,Int](feat)
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
    func
  }

  def list[T](feat: ObjectFeature[T]): (List[T], List[T]) => Boolean = {
    def func(a: List[T], b: List[T]): Boolean = {
      a.size == b.size && a.zip(b).forall(x => feat.isEqual(x._1, x._2))
    }
    func
  }

}
