package soda.kotlin.web.http

import soda.kotlin.web.BusinessCode
import soda.kotlin.web.exception.ServiceException
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

class MultipartParserEx(input: InputStream, private val boundary: String) {

    companion object {
        private const val BUF_SIZE = 1024
        private const val CR = '\r'.code.toByte()
        private const val LF = '\n'.code.toByte()
        private const val HYPHEN = '-'.code.toByte()
    }

    private val lineInput = ByteLineInputStream(input, 4096)

    fun parse(): List<Part> {
        val boundaryBytes = boundary.toByteArray(StandardCharsets.UTF_8)
        var buffer = ByteArray(BUF_SIZE)

        var size = lineInput.readLine(buffer)
        if (!isBoundaryLine(buffer, 0, size, boundaryBytes)) {
            val errMsg = "body not start with boundary $boundary"
            throw ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
        }

        val partList = mutableListOf<Part>()
        var reachEnd = false
        while (!reachEnd) {
            val part = Part()
            while (true) {
                var headerLine: String
                size = lineInput.readLine(buffer)
                if (isLine(buffer, 0, size)) {
                    headerLine = String(buffer, 0, size - 2, StandardCharsets.UTF_8)
                } else {
                    val bs = ByteArrayOutputStream()
                    bs.write(buffer, 0, size)
                    do {
                        size = lineInput.readLine(buffer)
                        bs.write(buffer, 0, size)
                    } while (!isLine(buffer, 0, size))
                    val dataBytes = bs.toByteArray()
                    headerLine = String(dataBytes, 0, dataBytes.size - 2, StandardCharsets.UTF_8)
                }
                if (headerLine.isEmpty()) {
                    break
                }
                if (headerLine.startsWith("Content-Disposition:")) {
                    part.contentDisposition = ContentDisposition.fromHeaderLine(headerLine)
                } else if (headerLine.startsWith("Content-Type:")) {
                    part.contentType = headerLine.split(":")[1].trim()
                }
            }

            if (part.contentDisposition == null) {
                val errMsg = "no Content-Disposition header in part"
                throw ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
            }

            var ready = ByteArray(BUF_SIZE)
            var readySize = lineInput.readLine(ready)
            var overflow: ByteArrayOutputStream? = null
            var contentEnd = false
            while (!contentEnd) {
                val n = lineInput.readLine(buffer)
                val bbSize = boundaryBytes.size
                if (isBoundaryLine(buffer, 0, n, boundaryBytes)) {
                    if (overflow == null) {
                        // exclude trailing "\r\n"
                        part.payload = ready.copyOf(readySize - 2)
                    } else {
                        // exclude trailing "\r\n"
                        overflow.write(ready, 0, readySize - 2)
                        part.payload = overflow.toByteArray()
                    }
                    if (n == bbSize + 6) {
                        reachEnd = true
                    }
                    contentEnd = true
                } else {
                    if (overflow == null) {
                        overflow = ByteArrayOutputStream()
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
        return partList
    }

    private fun isLine(data: ByteArray, offset: Int, length: Int): Boolean {
        val end = offset + length
        return data[end-1] == LF && data[end-2] == CR
    }

    private fun isBoundary(data: ByteArray, offset: Int, length: Int, boundary: ByteArray): Boolean {
        var i = offset + length - 1
        var j = boundary.size - 1
        while (i >= offset) {
            if (data[i] != boundary[j]) {
                return false
            }
            --i
            --j
        }
        return true
    }

    private fun isBoundaryLine(data: ByteArray, offset: Int, length: Int, boundary: ByteArray): Boolean {
        val s = boundary.size
        val end = offset + length
        return isLine(data, offset, length)
                && (length == s + 4 || length == s + 6 && data[end-4] == HYPHEN && data[end-3] == HYPHEN)
                && data[offset] == HYPHEN && data[offset+1] == HYPHEN
                && isBoundary(data, offset + 2, s, boundary)
    }

}