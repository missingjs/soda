package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe._

object ValidatorFactory {

  private val factoryMap = mutable.Map[Type, () => (_, _) => Boolean]()

  private def registerFactory[T](factory: () => (T, T) => Boolean)(implicit tt: TypeTag[T]): Unit = {
    factoryMap(typeOf[T]) = factory
  }

  registerFactory(() => Validators.forArray[Double](ordered = true))
  registerFactory(() => Validators.forArray2d[Double](dim1Ordered = true, dim2Ordered = true))
  registerFactory(() => Validators.forList[Double](ordered = true))
  registerFactory(() => Validators.forList2d[Double](dim1Ordered = true, dim2Ordered = true))

  def create[T]()(implicit tt: TypeTag[T]): (T, T) => Boolean = {
    create(typeOf[T])
  }

  def create[T](elemType: Type): (T, T) => Boolean = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact().asInstanceOf[(T,T)=>Boolean]
      case None => (a, b) => FeatureFactory.create[T](elemType).isEqual(a, b)
    }
  }

}
