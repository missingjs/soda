package soda.scala.unittest

import scala.collection.mutable
import scala.reflect.runtime.universe._
import play.api.libs.json._
import soda.scala.leetcode._

object ConverterFactory {

  private val factoryMap = mutable.Map[String, () => ObjectConverter[_]]()

  private def registerFactory[E](factory: () => ObjectConverter[E])(implicit tt: TypeTag[E]): Unit = {
    factoryMap(typeOf[E].toString) = factory
  }

  private def registerFactory[T,M](parser: M => T, serializer: T => M)(implicit tt: TypeTag[T], fmt: Format[M]): Unit = {
    registerFactory(() => new ObjectConverter[T] {
      override def fromJsonSerializable(js: JsValue): T = parser(js.as[M])
      override def toJsonSerializable(element: T): JsValue = Json.toJson(serializer(element))
    })
  }

  private def regFact[E](implicit tt: TypeTag[E], fmt: Format[E]): Unit = {
    registerFactory((e: E) => e, (e: E) => e)
  }

  regFact[Boolean]
  regFact[Short]
  regFact[Int]
  regFact[Long]
  regFact[Float]
  regFact[Double]
  regFact[String]

  regFact[Array[Int]]
  regFact[List[Int]]
  regFact[Array[Array[Int]]]
  regFact[List[List[Int]]]

  regFact[Array[String]]
  regFact[List[String]]
  regFact[Array[Array[String]]]
  regFact[List[List[String]]]

  regFact[Array[Boolean]]
  regFact[List[Boolean]]

  regFact[Array[Double]]
  regFact[List[Double]]
  regFact[Array[Array[Double]]]
  regFact[List[List[Double]]]

  private def s2c(s: String) = s(0)
  private def c2s(c: Char) = s"$c"
  // Char
  registerFactory(s2c, c2s)
  // List[Char]
  registerFactory(
    (strList: List[String]) => strList.map(s2c),
    (charList: List[Char]) => charList.map(c2s)
  )
  // List[List[Char]]
  registerFactory(
    (strList2d: List[List[String]]) => strList2d.map(_.map(s2c)),
    (charList2d: List[List[Char]]) => charList2d.map(_.map(c2s))
  )
  // Array[Char]
  registerFactory(
    (sa: Array[String]) => sa.map(s2c),
    (ca: Array[Char]) => ca.map(c2s)
  )
  // Array[Array[Char]]
  registerFactory(
    (sa2d: Array[Array[String]]) => sa2d.map(_.map(s2c)),
    (ca2d: Array[Array[Char]]) => ca2d.map(_.map(c2s))
  )

  // ListNode
  registerFactory(ListFactory.create, ListFactory.dump)

  def create[E]()(implicit tt: TypeTag[E]): ObjectConverter[E] = {
    create(typeOf[E]).asInstanceOf[ObjectConverter[E]]
  }

  def create(elemType: Type): ObjectConverter[_] = {
    create(elemType.toString)
  }

  def create(typeName: String): ObjectConverter[_] = {
    factoryMap.get(typeName) match {
      case Some(fact) => fact()
      case None => {
        throw new RuntimeException(s"[ConverterFactory] element type is not supported: $typeName")
      }
    }
  }
  
}
