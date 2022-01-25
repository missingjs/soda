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

  private def registerFactory[E](implicit fmt: Format[E], tt: TypeTag[E]): Unit = {
    registerFactory(typeOf[E], (js: JsValue) => js.as[E], (e: E) => Json.toJson(e))
  }

  registerFactory[Boolean]
  registerFactory[Short]
  registerFactory[Int]
  registerFactory[Long]
  registerFactory[Float]
  registerFactory[Double]
  registerFactory[String]

  registerFactory[Array[Int]]
  registerFactory[List[Int]]
  registerFactory[Array[Array[Int]]]
  registerFactory[List[List[Int]]]

  registerFactory[Array[String]]
  registerFactory[List[String]]
  registerFactory[Array[Array[String]]]
  registerFactory[List[List[String]]]

  registerFactory[Array[Boolean]]
  registerFactory[List[Boolean]]

  registerFactory[Array[Double]]
  registerFactory[List[Double]]
  registerFactory[Array[Array[Double]]]
  registerFactory[List[List[Double]]]

  registerFactory(typeOf[Char], (js: JsValue) => js.as[String].charAt(0), (ch: Char) => Json.toJson(new String(Array(ch))))

  def create(elemType: Type): ObjectConverter[_] = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact()
      case None => {
        throw new RuntimeException(s"[ConverterFactory] element type is not supported: $elemType")
      }
    }
  }
}
