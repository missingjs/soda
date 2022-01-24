package soda.scala.unittest

import play.api.libs.json._

class WorkInput(jstr: String) {

  private val jsValue = Json.parse(jstr)

  def id: Int = (jsValue \ "id").get.as[Int]

  def hasExpected: Boolean = {
    val r = (jsValue \ "expected")
    r.isDefined match {
      case false => false
      case true => r.get match {
        case null => false
        case JsNull => false
        case _ => true
      }
    }
  }

  def expected: JsValue = (jsValue \ "expected").get

  def arg(index: Int): JsValue = (jsValue \ "args" \ 1).get

  def arguments: JsValue = (jsValue \ "args").get

}
