package soda.scala.web.http

import soda.scala.web.BusinessCode
import soda.scala.web.exception.ServiceException

import java.io.{ByteArrayOutputStream, InputStream}
import java.nio.charset.StandardCharsets
import java.util
import scala.collection.mutable
import scala.util.control.Breaks._

class MultipartParserEx(input: InputStream, private val boundary: String) {

    private val BUF_SIZE = 1024

    private val lineInput = new ByteLineInputStream(input, 4096)

    def parse(): List[Part] = {
        val boundaryBytes = boundary.getBytes(StandardCharsets.UTF_8)
        var buffer = Array.fill[Byte](BUF_SIZE)(0)

        var size = lineInput.readLine(buffer)
        if (!isBoundaryLine(buffer, 0, size, boundaryBytes)) {
            val errMsg = s"body not start with boundary $boundary"
            throw new ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
        }

        val partList = mutable.ArrayBuffer[Part]()
        var reachEnd = false
        while (!reachEnd) {
            val part = new Part()
            breakable {
                while (true) {
                    var headerLine: String = null
                    size = lineInput.readLine(buffer)
                    if (isLine(buffer, 0, size)) {
                        headerLine = new String(buffer, 0, size - 2, StandardCharsets.UTF_8)
                    } else {
                        val bs = new ByteArrayOutputStream()
                        bs.write(buffer, 0, size)
                        do {
                            size = lineInput.readLine(buffer)
                            bs.write(buffer, 0, size)
                        } while (!isLine(buffer, 0, size))
                        val dataBytes = bs.toByteArray
                        headerLine = new String(dataBytes, 0, dataBytes.length - 2, StandardCharsets.UTF_8)
                    }
                    if (headerLine.isEmpty) {
                        break()
                    }
                    if (headerLine.startsWith("Content-Disposition:")) {
                        part.contentDisposition = ContentDisposition.fromHeaderLine(headerLine)
                    } else if (headerLine.startsWith("Content-Type:")) {
                        part.contentType = headerLine.split(":", 2)(1).trim()
                    }
                }
            }

            if (part.contentDisposition == null) {
                val errMsg = "no Content-Disposition header in part"
                throw new ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
            }

            var ready = Array.fill[Byte](BUF_SIZE)(0)
            var readySize = lineInput.readLine(ready)
            var overflow: ByteArrayOutputStream = null
            var contentEnd = false
            while (!contentEnd) {
                val n = lineInput.readLine(buffer)
                val bbSize = boundaryBytes.size
                if (isBoundaryLine(buffer, 0, n, boundaryBytes)) {
                    if (overflow == null) {
                        // exclude trailing "\r\n"
                        part.payload = util.Arrays.copyOf(ready, readySize - 2)
                    } else {
                        // exclude trailing "\r\n"
                        overflow.write(ready, 0, readySize - 2)
                        part.payload = overflow.toByteArray
                    }
                    if (n == bbSize + 6) {
                        reachEnd = true
                    }
                    contentEnd = true
                } else {
                    if (overflow == null) {
                        overflow = new ByteArrayOutputStream()
                    }
                    overflow.write(ready, 0, readySize)
                    val temp = ready
                    ready = buffer
                    buffer = temp
                    readySize = n
                }
            }
            partList += part
        }
        partList.toList
    }

    private def isLine(data: Array[Byte], offset: Int, length: Int): Boolean = {
        val end = offset + length
        data(end-1) == '\n' && data(end-2) == '\r'
    }

    private def isBoundary(data: Array[Byte], offset: Int, length: Int, boundary: Array[Byte]): Boolean = {
        var i = offset + length - 1
        var j = boundary.length - 1
        while (i >= offset) {
            if (data(i) != boundary(j)) {
                return false
            }
            i -= 1
            j -= 1
        }
        true
    }

    private def isBoundaryLine(data: Array[Byte], offset: Int, length: Int, boundar: Array[Byte]): Boolean = {
        val s = boundar.length
        val end = offset + length
        (isLine(data, offset, length) 
            && (length == s + 4 || length == s + 6 && data(end-4) == '-' && data(end-3) == '-')
            && data(offset) == '-' && data(offset+1) == '-'
            && isBoundary(data, offset + 2, s, boundar))
    }

}