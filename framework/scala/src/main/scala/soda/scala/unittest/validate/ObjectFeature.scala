package soda.scala.unittest.validate

trait ObjectFeature[T] {

  def hash(obj: T): Long = obj.hashCode

  def isEqual(a: T, b: T): Boolean = a == b

}

class DefaultObjectFeature[T] extends ObjectFeature[T]

class DoubleFeature extends ObjectFeature[Double] {
  override def isEqual(a: Double, b: Double): Boolean = (a-b).abs < 1e-6
}
