package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe._

import play.api.libs.json._

object ConverterFactory {

  private val factoryMap = mutable.Map[Type, () => ObjectConverter[_]]()

  private def registerFactory[E](elemType: Type, factory: () => ObjectConverter[E]): Unit = {
    factoryMap(elemType) = factory
  }

  private def registerFactory[E](elemType: Type, parser: JsValue => E, serializer: E => JsValue): Unit = {
    factoryMap(elemType) = () => new ObjectConverter[E] {
      override def fromJsonSerializable(js: JsValue): E = parser(js)

      override def toJsonSerializable(element: E): JsValue = serializer(element)
    }
  }

  private def registerFactory[E](elemType: Type)(implicit fmt: Format[E]): Unit = {
    registerFactory(elemType, (js: JsValue) => js.as[E], (e: E) => Json.toJson(e))
  }

  registerFactory[Int](typeOf[Int])

  def create(elemType: Type): ObjectConverter[_] = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact()
      case None => {
        throw new Exception(s"element type is not supported: $elemType")
      }
    }
  }
}
