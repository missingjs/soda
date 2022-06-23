package soda.scala.web.http

import soda.scala.web.WebUtils

class ContentDisposition {

  var name: String = null

  var filename: String = null

  def isFile: Boolean = filename != null

}

object ContentDisposition {

  def fromHeaderValue(value: String): ContentDisposition = {
    WebUtils.findOne(value, "name=\"(.*?)\"") match {
      case Some(name) =>
        val cd = new ContentDisposition()
        cd.name = name
        cd.filename = WebUtils.findOne(value, "filename=\"(.*?)\"").orNull
        cd
      case None => throw new IllegalArgumentException("no name found in Content-Disposition")
    }
  }

  def fromHeaderLine(headerLine: String): ContentDisposition = {
    fromHeaderValue(headerLine)
  }

}
