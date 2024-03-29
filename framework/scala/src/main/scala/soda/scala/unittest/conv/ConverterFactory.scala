package soda.scala.unittest.conv

import scala.collection.mutable
import scala.reflect.runtime.universe._

import play.api.libs.json._
import soda.scala.leetcode._

object ConverterFactory {

  private val factoryMap = mutable.Map[String, () => ObjConv]()

  private def registerFactory[E](factory: () => ObjectConverter[E])(implicit tt: TypeTag[E]): Unit = {
    factoryMap(typeOf[E].toString) = factory
  }

  private def registerFactory[T, M](parser: M => T, serializer: T => M)(implicit tt: TypeTag[T], fmt: Format[M]): Unit = {
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
  // List[ListNode]
  registerFactory(
    (data: List[List[Int]]) => data.map(ListFactory.create),
    (heads: List[ListNode]) => heads.map(ListFactory.dump)
  )
  // Array[ListNode]
  registerFactory(
    (data: List[List[Int]]) => data.map(ListFactory.create).toArray,
    (lists: Array[ListNode]) => lists.map(ListFactory.dump).toList
  )

  implicit def optionFormat[T: Format]: Format[Option[T]] = new Format[Option[T]] {
    override def reads(json: JsValue): JsResult[Option[T]] = json.validateOpt[T]

    override def writes(o: Option[T]): JsValue = o match {
      case Some(t) => implicitly[Writes[T]].writes(t)
      case None => JsNull
    }
  }
  // TreeNode
  registerFactory(TreeFactory.create, TreeFactory.dump)

  // NestedInteger
  registerFactory(() => new NestedIntegerConverter)
  // List[NestedInteger]
  registerFactory(() => new ObjectConverter[List[NestedInteger]] {
    override def fromJsonSerializable(js: JsValue): List[NestedInteger] = {
      js.as[JsArray].value.map(NestedIntegerConverter.parse).toList
    }

    override def toJsonSerializable(element: List[NestedInteger]): JsValue = {
      Json.toJson(element.map(NestedIntegerConverter.serialize))
    }
  })

  // JsValue
  registerFactory((j: JsValue) => j, (d: JsValue) => d)

  def create[E](elemType: Type): ObjectConverter[E] = {
    create(elemType.toString)
  }

  def create[E](typeName: String): ObjectConverter[E] = {
    factoryMap.get(typeName) match {
      case Some(fact) => fact().as[E]
      case None =>
        throw new RuntimeException(s"[ConverterFactory] element type is not supported: $typeName")
    }
  }

}
