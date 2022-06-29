package soda.scala.unittest.conv

import play.api.libs.json.JsValue

trait ObjConv {
  def as[E]: ObjectConverter[E] = this.asInstanceOf[ObjectConverter[E]]
}

trait ObjectConverter[E] extends ObjConv {

  def fromJsonSerializable(js: JsValue): E

  def toJsonSerializable(element: E): JsValue

}
