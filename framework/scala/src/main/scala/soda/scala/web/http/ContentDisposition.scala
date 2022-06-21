package soda.scala.web.http

import soda.scala.web.Utils

class ContentDisposition {

  var name: String = null

  var filename: String = null

  def isFile: Boolean = filename != null

}

object ContentDisposition {

  def fromHeaderValue(value: String): ContentDisposition = {
    val name = Utils.findOne(value, "name=\"(.*?)\"")
    if (name == null) {
      throw new IllegalArgumentException("no name found in Content-Disposition")
    }

    val cd = new ContentDisposition()
    cd.name = name
    cd.filename = Utils.findOne(value, "filename=\"(.*?)\"")
    cd
  }

  def fromHeaderLine(headerLine: String): ContentDisposition = {
    fromHeaderValue(headerLine)
  }

}
