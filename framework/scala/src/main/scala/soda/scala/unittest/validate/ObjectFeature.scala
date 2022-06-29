package soda.scala.unittest.validate

trait ObjFeat {
  def as[T]: ObjectFeature[T] = this.asInstanceOf[ObjectFeature[T]]
}

trait ObjectFeature[T] extends ObjFeat {

  def hash(obj: T): Long = obj.hashCode

  def isEqual(a: T, b: T): Boolean = a == b

}

class DefaultObjectFeature[T] extends ObjectFeature[T]

class DoubleFeature extends ObjectFeature[Double] {
  override def isEqual(a: Double, b: Double): Boolean = (a-b).abs < 1e-6
}

class DoubleArrayFeature extends ObjectFeature[Array[Double]] {
  private val proxy = FeatureFactory.create[List[Double]]

  override def hash(obj: Array[Double]): Long = proxy.hash(obj.toList)

  override def isEqual(a: Array[Double], b: Array[Double]): Boolean = proxy.isEqual(a.toList, b.toList)
}

class DoubleArray2dFeature extends ObjectFeature[Array[Array[Double]]] {
  private val proxy = FeatureFactory.create[List[List[Double]]]

  override def hash(obj: Array[Array[Double]]): Long = proxy.hash(obj.map(_.toList).toList)

  override def isEqual(a: Array[Array[Double]], b: Array[Array[Double]]): Boolean = {
    proxy.isEqual(a.map(_.toList).toList, b.map(_.toList).toList)
  }
}
