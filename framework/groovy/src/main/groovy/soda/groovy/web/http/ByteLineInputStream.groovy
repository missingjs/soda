package soda.groovy.web.http

import soda.groovy.web.BusinessCode
import soda.groovy.web.exception.ServiceException

class ByteLineInputStream {

    private static final byte CR = 0x0d

    private static final byte LF = 0x0a

    private InputStream input

    private byte[] buffer

    private int cur = 0

    private int end = 0

    ByteLineInputStream(InputStream input, int bufferSize) {
        this.input = input
        buffer = new byte[bufferSize]
    }

    int readLine(byte[] buf) throws IOException {
        return readLine(buf, 0, buf.length)
    }

    int readLine(byte[] bytes, int offset, int len) throws IOException {
        if (len < 2) {
            throw new IllegalArgumentException("len must not less than 2")
        }

        int j = offset, jEnd = offset + len
        while (true) {
            int si = cur, sj = j
            while (cur < end && buffer[cur] != CR && j < jEnd) {
                ++cur
                ++j
            }

            System.arraycopy(buffer, si, bytes, sj, cur - si)

            if (cur == end) {
                if (end == buffer.length) {
                    cur = end = 0
                }
                if (loadMore() == -1) {
                    return j - offset
                }
                continue
            }

            if (cur == end - 1) {
                if (end == buffer.length) {
                    buffer[0] = buffer[cur]
                    cur = 0
                    end = 1
                }
                if (loadMore() == -1) {
                    if (j == jEnd) {
                        return j - offset
                    }
                    // buffer[cur] == '\r', and now it reach the end of stream
                    var errMsg = "invalid format of multipart/form-data, the last char in stream is \\r"
                    throw new ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
                }
                continue
            }

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

            bytes[j++] = buffer[cur++]
        }
    }

    private int loadMore() throws IOException {
        int size = input.read(buffer, end, buffer.length - end)
        if (size > 0) {
            end += size
        }
        return size
    }

}
