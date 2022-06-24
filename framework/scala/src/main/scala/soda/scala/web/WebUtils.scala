package soda.scala.web

import java.io.{ByteArrayOutputStream, InputStream, PrintStream}
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

object WebUtils {

  def findOne(text: String, pattern: String, group: Int): Option[String] = {
    val mat = Pattern.compile(pattern).matcher(text)
    Option(if (mat.find()) mat.group(group) else null)
  }

  def findOne(text: String, pattern: String): Option[String] = {
    findOne(text, pattern, 1)
  }

  def toByteArray(in: InputStream): Array[Byte] = {
    val buf = Array.fill[Byte](1024)(0)
    val outs = new ByteArrayOutputStream()
    var size = 0
    while ({size = in.read(buf); size != -1}) {
      outs.write(buf, 0, size)
    }
    outs.toByteArray
  }

  def toString(ex: Throwable): String = {
    val out = new ByteArrayOutputStream()
    val pw = new PrintStream(out)
    ex.printStackTrace(pw)
    pw.flush()
    out.toString(StandardCharsets.UTF_8)
  }

}
