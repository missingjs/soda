package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe._

object FeatureFactory {

  private val factoryMap = mutable.Map[Type, () => ObjectFeature[_]]()

  private def registerFactory(elemType: Type, factory: () => ObjectFeature[_]) = {
    factoryMap(elemType) = factory
  }

  def create[T](elemType: Type): ObjectFeature[T] = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact().asInstanceOf[ObjectFeature[T]]
      case None => new DefaultObjectFeature
    }
  }
}
