package soda.scala.unittest

class ListFeature[T](elemFeat: ObjectFeature[T]) extends ObjectFeature[List[T]] {

  override def hash(obj: List[T]): Long = obj.map(elemFeat.hash).foldLeft(0L)((a, b) => a * 133 + b)

  override def isEqual(a: List[T], b: List[T]): Boolean = StrategyFactory.list(elemFeat)(a, b)

}
