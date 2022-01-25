package soda.scala.unittest

import scala.reflect.runtime.universe.Type

object Validators {

  def forList[T](elemType: Type, ordered: Boolean): (List[T], List[T]) => Boolean = {
    val f = FeatureFactory.create[T](elemType)
    if (ordered) StrategyFactory.list[T](f) else StrategyFactory.unorderList[T](f)
  }

  def forArray[T](elemType: Type, ordered: Boolean): (Array[T], Array[T]) => Boolean = {
    (a: Array[T], b: Array[T]) => forList(elemType, ordered)(a.toList, b.toList)
  }

}
