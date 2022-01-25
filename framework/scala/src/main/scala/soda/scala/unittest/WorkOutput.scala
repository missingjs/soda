package soda.scala.unittest

import play.api.libs.json._

class WorkOutput {
  var id: Int = 0
  var success: Boolean = false
  var result: JsValue = JsNull
  var elapse: Double = 0.0

  case class _output(id: Int, success: Boolean, result: JsValue, elapse: Double)

  def jsonString: String = {
    implicit val outWrites: OWrites[_output] = Json.writes[_output]
    Json.stringify(Json.toJson(_output(id,success,result,elapse)))
  }
}
