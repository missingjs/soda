package soda.scala.unittest.conv

import play.api.libs.json.JsValue

trait ObjectConverter[E] {

  def fromJsonSerializable(js: JsValue): E

  def toJsonSerializable(element: E): JsValue

}
