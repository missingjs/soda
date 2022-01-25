package soda.scala.unittest
import play.api.libs.json.{JsValue, Json}

class CharList2dConverter extends ObjectConverter[List[List[Char]]] {

  override def fromJsonSerializable(js: JsValue): List[List[Char]] = {
    js.as[List[List[String]]].map(CharListConverter.s2c)
  }

  override def toJsonSerializable(mx: List[List[Char]]): JsValue = {
    Json.toJson(mx.map(CharListConverter.c2s))
  }
}
