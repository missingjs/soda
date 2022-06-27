package soda.scala.web.work

import play.api.libs.json.{Json, OFormat}

case class WorkRequest(key: String, entryClass: String, testCase: String)

object WorkRequest {
  implicit val _format: OFormat[WorkRequest] = Json.format[WorkRequest]
}
