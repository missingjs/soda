package soda.scala.unittest

import scala.reflect.runtime.universe._

object Validators {

  def forList[T](ordered: Boolean)(implicit tt: TypeTag[T]): (List[T], List[T]) => Boolean = {
    val elemType = typeOf[T]
    val f = FeatureFactory.create[T](elemType)
    if (ordered) StrategyFactory.list[T](f) else StrategyFactory.unorderList[T](f)
  }

  def forList2d[T](dim1Ordered: Boolean, dim2Ordered: Boolean)(implicit tt: TypeTag[T]): (List[List[T]], List[List[T]]) => Boolean = {
    val elemType = typeOf[T]
    val elemFeat = FeatureFactory.create[T](elemType)
    Log.info(s"element feat: $elemFeat, elem type: $elemType")
    val lsFeat = if (dim2Ordered) new ListFeature[T](elemFeat) else new UnorderListFeature[T](elemFeat)
    if (dim1Ordered) StrategyFactory.list(lsFeat) else StrategyFactory.unorderList(lsFeat)
  }

  def forArray[T](ordered: Boolean)(implicit tt: TypeTag[T]): (Array[T], Array[T]) => Boolean = {
    (a: Array[T], b: Array[T]) => forList[T](ordered)(tt)(a.toList, b.toList)
  }

  def forArray2d[T](dim1Ordered: Boolean, dim2Ordered: Boolean)(implicit tt: TypeTag[T]): (Array[Array[T]], Array[Array[T]]) => Boolean = {
    (a: Array[Array[T]], b: Array[Array[T]]) =>
      forList2d(dim1Ordered, dim2Ordered)(tt)(a.map(_.toList).toList, b.map(_.toList).toList)
  }

}
