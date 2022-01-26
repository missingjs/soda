package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe._

import play.api.libs.json._

object ConverterFactory {

  private val factoryMap = mutable.Map[Type, () => ObjectConverter[_]]()

  private def registerFactory[E](factory: () => ObjectConverter[E])(implicit tt: TypeTag[E]): Unit = {
    factoryMap(typeOf[E]) = factory
  }

  private def registerFactory[E](parser: JsValue => E, serializer: E => JsValue)(implicit tt: TypeTag[E]): Unit = {
    registerFactory(() => new ObjectConverter[E] {
      override def fromJsonSerializable(js: JsValue): E = parser(js)
      override def toJsonSerializable(element: E): JsValue = serializer(element)
    })
  }

  private def registerFactory[E](implicit fmt: Format[E], tt: TypeTag[E]): Unit = {
    registerFactory((js: JsValue) => js.as[E], (e: E) => Json.toJson(e))
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

  registerFactory((js: JsValue) => js.as[String].charAt(0), (ch: Char) => Json.toJson(new String(Array(ch))))
  registerFactory(() => new CharListConverter)
  registerFactory(
    (js: JsValue) => new CharListConverter().fromJsonSerializable(js).toArray,
    (chs: Array[Char]) => new CharListConverter().toJsonSerializable(chs.toList)
  )
  registerFactory(() => new CharList2dConverter)
  registerFactory(
    (js: JsValue) => new CharList2dConverter().fromJsonSerializable(js).map(_.toArray).toArray,
    (mx: Array[Array[Char]]) => new CharList2dConverter().toJsonSerializable(mx.map(_.toList).toList)
  )

  def create[E]()(implicit tt: TypeTag[E]): ObjectConverter[E] = {
    create(typeOf[E]).asInstanceOf[ObjectConverter[E]]
  }

  def create(elemType: Type): ObjectConverter[_] = {
    factoryMap.get(elemType) match {
      case Some(fact) => fact()
      case None => {
        throw new RuntimeException(s"[ConverterFactory] element type is not supported: $elemType")
      }
    }
  }
}
