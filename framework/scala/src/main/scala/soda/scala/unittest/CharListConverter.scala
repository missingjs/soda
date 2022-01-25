package soda.scala.unittest
import play.api.libs.json.{JsValue, Json}

class CharListConverter extends ObjectConverter[List[Char]] {

  override def fromJsonSerializable(js: JsValue): List[Char] = {
    CharListConverter.s2c(js.as[List[String]])
  }

  override def toJsonSerializable(chars: List[Char]): JsValue = {
    Json.toJson(CharListConverter.c2s(chars))
  }

}

object CharListConverter {
  def s2c(s: List[String]): List[Char] = s.map(_.charAt(0))
  def c2s(c: List[Char]): List[String] = c.map("" + _)
}
