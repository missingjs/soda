package soda.scala.unittest

import scala.reflect.runtime.universe._

import soda.scala.unittest.validate._

object Validators {

  def forList[T](ordered: Boolean)(implicit tt: TypeTag[T]): (List[T], List[T]) => Boolean = {
    forList(ordered, FeatureFactory.create[T])
  }

  def forList2d[T](dim1Ordered: Boolean, dim2Ordered: Boolean)(implicit tt: TypeTag[T]): (List[List[T]], List[List[T]]) => Boolean = {
    val elemFeat = FeatureFactory.create[T]
    val lsFeat = if (dim2Ordered) new ListFeature[T](elemFeat) else new UnorderedListFeature[T](elemFeat)
    forList(dim1Ordered, lsFeat)
  }

  private def forList[T: TypeTag](ordered: Boolean, elemFeat: ObjectFeature[T]): (List[T], List[T]) => Boolean = {
    if (ordered) new ListFeature(elemFeat).isEqual else new UnorderedListFeature(elemFeat).isEqual
  }

  def forArray[T](ordered: Boolean)(implicit tt: TypeTag[T]): (Array[T], Array[T]) => Boolean = {
    (a: Array[T], b: Array[T]) => forList[T](ordered)(tt)(a.toList, b.toList)
  }

  def forArray2d[T](dim1Ordered: Boolean, dim2Ordered: Boolean)(implicit tt: TypeTag[T]): (Array[Array[T]], Array[Array[T]]) => Boolean = {
    (a: Array[Array[T]], b: Array[Array[T]]) =>
      forList2d(dim1Ordered, dim2Ordered)(tt)(a.map(_.toList).toList, b.map(_.toList).toList)
  }

}
