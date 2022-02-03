package soda.scala.unittest.validate

import scala.collection.mutable
import scala.reflect.runtime.universe._

object FeatureFactory {

  private val factoryMap = mutable.Map[String, () => ObjectFeature[_]]()

  private def registerFactory[T](factory: () => ObjectFeature[T])(implicit tt: TypeTag[T]): Unit = {
    factoryMap(typeOf[T].toString) = factory
  }

  registerFactory(() => new DoubleFeature)
  registerFactory(() => new ListFeature(create[Double]))
  registerFactory(() => new DoubleArrayFeature)
  registerFactory(() => new ListFeature(create[List[Double]]))
  registerFactory(() => new DoubleArray2dFeature)

  def create[T: TypeTag]: ObjectFeature[T] = {
    create(typeOf[T])
  }

  def create[T](elemType: Type): ObjectFeature[T] = {
    factoryMap.get(elemType.toString) match {
      case Some(fact) => fact().asInstanceOf[ObjectFeature[T]]
      case None => new DefaultObjectFeature
    }
  }
}
