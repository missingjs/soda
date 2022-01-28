package soda.scala.web

import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets

object Utils {

  def toString(ex: Throwable): String = {
    val out = new ByteArrayOutputStream()
    val pw = new PrintStream(out)
    ex.printStackTrace(pw)
    pw.flush()
    out.toString(StandardCharsets.UTF_8)
  }

}
