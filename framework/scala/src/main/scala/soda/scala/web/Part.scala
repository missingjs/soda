package soda.scala.web

import soda.scala.web.Part.Disposition

import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class Part {

  var contentDisposition: Disposition = null

  var contentType: String = null

  var contentBytes: Array[Byte] = Array.empty

  def getName: String = contentDisposition.name

  def bodyString = new String(contentBytes, StandardCharsets.UTF_8)

}

object Part {
  case class Disposition(var name: String = null, var fileName: String = null)

  object Disposition {
    def parse(content: String): Disposition = {
      val dis = new Disposition()
      val m1 = Pattern.compile("name=\"(.*?)\"").matcher(content)
      if (m1.find()) {
        dis.name = m1.group(1)
      }
      val m2 = Pattern.compile("filename=\"(.*?)\"").matcher(content)
      if (m2.find()) {
        dis.fileName = m2.group(1)
      }
      dis
    }
  }
}
