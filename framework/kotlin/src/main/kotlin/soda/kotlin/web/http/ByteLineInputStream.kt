package soda.kotlin.web.http

import soda.kotlin.web.BusinessCode
import soda.kotlin.web.exception.ServiceException
import java.io.InputStream

class ByteLineInputStream(private val input: InputStream, bufferSize: Int) {

    companion object {
        private const val CR = '\r'.code.toByte()
        private const val LF = '\n'.code.toByte()
    }

    private val buffer = ByteArray(bufferSize){0}

    private var cur = 0

    private var end = 0

    fun readLine(buf: ByteArray): Int {
        return readLine(buf, 0, buf.size)
    }

    fun readLine(bytes: ByteArray, offset: Int, len: Int): Int {
        if (len < 2) {
            throw IllegalArgumentException("len must not less than 2")
        }

        var j = offset
        val jEnd = offset + len

        while (true) {
            val si = cur
            val sj = j
            while (cur < end && buffer[cur] != CR && j < jEnd) {
                ++cur
                ++j
            }

            System.arraycopy(buffer, si, bytes, sj, cur - si)

            if (cur == end) {
                if (end == buffer.size) {
                    cur = 0
                    end = 0
                }
                if (loadMore() == -1) {
                    return j - offset
                }
            } else if (cur == end - 1) {
                if (end == buffer.size) {
                    buffer[0] = buffer[cur]
                    cur = 0
                    end = 1
                }
                if (loadMore() == -1) {
                    if (j == jEnd) {
                        return j - offset
                    }
                    // buffer[cur] == '\r', and now it reach the end of stream
                    val errMsg = "invalid format of multipart/form-data, the last char in stream is \\r"
                    throw ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
                }
            } else {
                // buffer[cur] == '\r', cur < end - 1
                if (j == jEnd || j == jEnd - 1) {
                    return j - offset
                }

                if (buffer[cur+1] == LF) {
                    bytes[j] = CR
                    bytes[j+1] = LF
                    cur += 2
                    return j - offset + 2
                }

                bytes[j] = buffer[cur]
                ++j
                ++cur
            }
        }
    }

    private fun loadMore(): Int  {
        val size = input.read(buffer, end, buffer.size - end)
        if (size > 0) {
            end += size
        }
        return size
    }

}