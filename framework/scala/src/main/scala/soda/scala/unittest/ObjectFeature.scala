package soda.scala.unittest

trait ObjectFeature[T] {

  def hash(obj: T): Long = obj.hashCode

  def isEqual(a: T, b: T): Boolean = a == b

}

class DefaultObjectFeature[T] extends ObjectFeature[T]
