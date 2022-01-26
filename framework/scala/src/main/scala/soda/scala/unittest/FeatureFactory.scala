package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe._

object FeatureFactory {

  private val factoryMap = mutable.Map[Type, () => ObjectFeature[_]]()

  private def registerFactory[T](factory: () => ObjectFeature[T])(implicit tt: TypeTag[T]): Unit = {
    factoryMap(typeOf[T]) = factory
//    Log.info("feature factor for " + typeOf[T])
  }

  registerFactory(() => new DoubleFeature)

  def create[T]()(implicit tt: TypeTag[T]): ObjectFeature[T] = {
    create(typeOf[T])
  }

  def create[T](elemType: Type): ObjectFeature[T] = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact().asInstanceOf[ObjectFeature[T]]
      case None => new DefaultObjectFeature
    }
  }
}
