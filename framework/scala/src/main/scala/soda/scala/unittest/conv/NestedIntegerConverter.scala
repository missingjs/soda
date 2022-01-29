package soda.scala.unittest.conv

import play.api.libs.json._
import soda.scala.leetcode.{DefaultNestedInteger, NestedInteger}

class NestedIntegerConverter extends ObjectConverter[NestedInteger] {

  override def fromJsonSerializable(js: JsValue): NestedInteger = NestedIntegerConverter.parse(js)

  override def toJsonSerializable(element: NestedInteger): JsValue = NestedIntegerConverter.serialize(element)

}

object NestedIntegerConverter {
  def parse(obj: JsValue): NestedInteger = {
    obj match {
      case i: JsNumber => DefaultNestedInteger.forInt(i.as[Int])
      case arr: JsArray => {
        val ni = DefaultNestedInteger.forList()
        for (elem <- arr.value) {
          ni.add(parse(elem))
        }
        ni
      }
      case _ => throw new RuntimeException("[NestedInteger] error type of object: " + obj.getClass)
    }
  }

  def serialize(ni: NestedInteger): JsValue = {
    if (ni.isInteger) Json.toJson(ni.getInteger) else Json.toJson(ni.getList.map(serialize))
  }
}
