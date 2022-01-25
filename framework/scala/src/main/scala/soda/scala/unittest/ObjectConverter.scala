package soda.scala.unittest

import play.api.libs.json._

trait ObjectConverter[E] {

  def fromJsonSerializable(js: JsValue): E

  def toJsonSerializable(element: E): JsValue

}
