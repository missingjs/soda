package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe.Type

object ValidatorFactory {

  private val factoryMap = mutable.Map[Type, () => (_, _) => Boolean]()

  def create[T](elemType: Type): (T, T) => Boolean = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact().asInstanceOf[(T,T)=>Boolean]
      case None => (a, b) => FeatureFactory.create[T](elemType).isEqual(a, b)
    }
  }

}
