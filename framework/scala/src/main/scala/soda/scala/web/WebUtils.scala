package soda.scala.web

import java.io.{ByteArrayOutputStream, InputStream, PrintStream}
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
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

  def md5Hex(data: Array[Byte]): String = try {
    val md5 = MessageDigest.getInstance("MD5")
    md5.update(data)
    hex(md5.digest)
  } catch {
    case ex: Exception =>
      throw new RuntimeException("md5 error", ex)
  }

  def hex(data: Array[Byte]): String = {
    val code = "0123456789abcdef"
    val buf = new Array[Char](data.length * 2)
    for (i <- data.indices) {
      val high = (data(i) >> 4) & 0x0f
      val low = data(i) & 0x0f
      buf(i * 2) = code.charAt(high)
      buf(i * 2 + 1) = code.charAt(low)
    }
    new String(buf)
  }

}
