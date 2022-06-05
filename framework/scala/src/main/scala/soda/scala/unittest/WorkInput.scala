package soda.scala.unittest

import play.api.libs.json._

class WorkInput(jstr: String) {

  private val root = Json.parse(jstr)

  def id: Int = (root \ "id").get.as[Int]

  def hasExpected: Boolean = {
    val r = (root \ "expected")
    r.isDefined match {
      case false => false
      case true => r.get match {
        case null => false
        case JsNull => false
        case _ => true
      }
    }
  }

  def expected: JsValue = (root \ "expected").get

  def arg(index: Int): JsValue = (root \ "args" \ index).get

  def arguments: JsValue = (root \ "args").get

}
