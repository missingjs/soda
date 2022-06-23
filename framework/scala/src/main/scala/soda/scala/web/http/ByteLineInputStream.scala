package soda.scala.web.http

import soda.scala.web.BusinessCode
import soda.scala.web.exception.ServiceException

import java.io.InputStream

class ByteLineInputStream(private val input: InputStream, bufferSize: Int) {

    private val buffer = Array.fill[Byte](bufferSize)(0)

    private var cur = 0

    private var end = 0

    def readLine(buf: Array[Byte]): Int = {
        readLine(buf, 0, buf.length)
    }

    def readLine(bytes: Array[Byte], offset: Int, len: Int): Int = {
        if (len < 2) {
            throw new IllegalArgumentException("len must not less than 2")
        }

        var j = offset
        val jEnd = offset + len

        while (true) {
            val si = cur
            val sj = j
            while (cur < end && buffer(cur) != '\r' && j < jEnd) {
                cur += 1
                j += 1
            }

            System.arraycopy(buffer, si, bytes, sj, cur - si)

            if (cur == end) {
                if (end == buffer.length) {
                    cur = 0
                    end = 0
                }
                if (loadMore() == -1) {
                    return j - offset
                }
            } else if (cur == end - 1) {
                if (end == buffer.length) {
                    buffer(0) = buffer(cur)
                    cur = 0
                    end = 1
                }
                if (loadMore() == -1) {
                    if (j == jEnd) {
                        return j - offset
                    }
                    // buffer[cur] == '\r', and now it reach the end of stream
                    val errMsg = "invalid format of multipart/form-data, the last char in stream is \\r"
                    throw new ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
                }
            } else {
                // buffer[cur] == '\r', cur < end - 1
                if (j == jEnd || j == jEnd - 1) {
                    return j - offset
                }

                if (buffer(cur+1) == '\n') {
                    bytes(j) = '\r'
                    bytes(j+1) = '\n'
                    cur += 2
                    return j - offset + 2
                }

                bytes(j) = buffer(cur)
                j += 1
                cur += 1
            }
        }
        -1
    }

    private def loadMore(): Int = {
        val size = input.read(buffer, end, buffer.length - end)
        if (size > 0) {
            end += size
        }
        size
    }

}