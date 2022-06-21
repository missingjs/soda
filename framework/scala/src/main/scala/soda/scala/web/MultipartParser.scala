package soda.scala.web

import soda.scala.web.http.Part

import java.io.{ByteArrayOutputStream, InputStream}
import java.nio.charset.StandardCharsets
import scala.util.control.Breaks.{break, breakable}

class MultipartParser(source: InputStream, boundary: String) {

  def parse(): List[Part] = {
    val contentBytes = Utils.toByteArray(source)
    val bounds = boundary.getBytes(StandardCharsets.UTF_8)
    var parts = List.empty[Part]

    var start = 0
    var end = locateNewLine(contentBytes, start)
    if (!isBoundary(contentBytes, start, end, bounds)) {
      reportFormatError()
    }

    start = end + 2
    do {
      val part = new Part()

      breakable {
        // read headers in a part
        while (true) {
          end = locateNewLine(contentBytes, start)
          if (end == start) { // blank line, between header and body in a part
            start = end + 2
            break()
          }
          val header = new String(contentBytes, start, end - start, StandardCharsets.UTF_8)
          start = end + 2
          if (header.startsWith("Content-Disposition:")) {
            part.contentDisposition = Part.Disposition.parse(header)
          } else if (header.startsWith("Content-Type:")) {
            part.contentType = header.split(":", 2)(1)
          }
        }
      }

      val bytebuf = new ByteArrayOutputStream()
      var n = 0
      breakable {
        while (true) {
          end = locateNewLine(contentBytes, start)
          if (isBoundary(contentBytes, start, end, bounds)) {
            break()
          }
          if (n > 0) {
            bytebuf.write('\r')
            bytebuf.write('\n')
          }
          bytebuf.write(contentBytes, start, end - start)
          start = end + 2
          n += 1
        }
      }
      part.payload = bytebuf.toByteArray
      parts = part :: parts
    } while (contentBytes(end-1) != '-' || contentBytes(end-2) != '-')

    parts
  }

  private def reportFormatError(): Unit = {
    throw new RuntimeException("invalid multipart format")
  }

  private def isBoundary(bytes: Array[Byte], start: Int, end: Int, bound: Array[Byte]): Boolean = {
    if (end - start < bound.length + 2 || bytes(start) != '-' || bytes(start+1) != '-') {
      return false
    }
    var i = start + 2
    for (j <- bound.indices) {
      if (bytes(i) != bound(j)) {
        return false
      }
      i += 1
    }
    true
  }

  private def locateNewLine(bytes: Array[Byte], start: Int): Int = {
    var p = start
    while (p < bytes.length) {
      if (bytes(p) == '\r' && p < bytes.length - 1 && bytes(p+1) == '\n') {
        return p
      }
      p += 1
    }
    p
  }

}
