package soda.groovy.web.http

import soda.groovy.web.BusinessCode
import soda.groovy.web.exception.ServiceException

import java.nio.charset.StandardCharsets

class MultipartParserEx {

    private static final byte CR = 0x0d

    private static final byte LF = 0x0a

    private static final byte HYPHEN = '-'.charAt(0)

    private final String boundary

    private final ByteLineInputStream lineInput

    private static final int BUF_SIZE = 1024

    MultipartParserEx(InputStream input, String boundary) {
        this.boundary = boundary
        lineInput = new ByteLineInputStream(input, 4096)
    }

    List<Part> parse() throws IOException {
        def boundaryBytes = boundary.getBytes(StandardCharsets.UTF_8)
        def buffer = new byte[BUF_SIZE]

        def size = lineInput.readLine(buffer)
        if (!isBoundaryLine(buffer, 0, size, boundaryBytes)) {
            def errMsg = "body not start with boundary " + boundary
            throw new ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
        }

        def partList = [] as List<Part>
        boolean reachEnd = false
        while (!reachEnd) {
            def part = new Part()
            // read headers in a part
            while (true) {
                String headerLine
                size = lineInput.readLine(buffer)
                if (isLine(buffer, 0, size)) {
                    headerLine = new String(buffer, 0, size - 2, StandardCharsets.UTF_8)
                } else {
                    def bs = new ByteArrayOutputStream()
                    bs.write(buffer, 0, size)
                    do {
                        size = lineInput.readLine(buffer)
                        bs.write(buffer, 0, size)
                    } while (!isLine(buffer, 0, size))
                    def dataBytes = bs.toByteArray()
                    headerLine = new String(dataBytes, 0, dataBytes.length - 2, StandardCharsets.UTF_8)
                }
                if (headerLine.length() == 0) {
                    // encounter the separator between a part's header and body
                    break
                }
                if (headerLine.startsWith("Content-Disposition:")) {
                    part.contentDisposition = ContentDisposition.fromHeaderLine(headerLine)
                } else if (headerLine.startsWith("Content-Type:")) {
                    part.contentType = headerLine.split(":", 2)[1].trim()
                }
            }

            if (part.contentDisposition == null) {
                def errMsg = "no Content-Disposition header in part"
                throw new ServiceException(BusinessCode.COMMON_ERROR, errMsg, 400)
            }

            // read body in a part
            def ready = new byte[BUF_SIZE]
            def readySize = lineInput.readLine(ready)
            ByteArrayOutputStream overflow = null

            while (true) {
                def n = lineInput.readLine(buffer)
                def bbSize = boundaryBytes.length
                if (isBoundaryLine(buffer, 0, n, boundaryBytes)) {
                    if (overflow == null) {
                        // exclude trailing "\r\n"
                        part.payload = Arrays.copyOf(ready, readySize - 2)
                    } else {
                        // exclude trailing "\r\n"
                        overflow.write(ready, 0, readySize - 2)
                        part.payload = overflow.toByteArray()
                    }
                    if (n == bbSize + 6) {
                        reachEnd = true
                    }
                    break
                } else {
                    if (overflow == null) {
                        overflow = new ByteArrayOutputStream()
                    }
                    overflow.write(ready, 0, readySize)
                    def _temp = ready
                    ready = buffer
                    buffer = _temp
                    readySize = n
                }
            }
            partList << part
        }
        return partList
    }

    private static boolean isLine(byte[] data, int offset, int length) {
        int end = offset + length
        return data[end-1] == LF && data[end-2] == CR
    }

    private static boolean isBoundary(byte[] data, int offset, int length, byte[] boundary) {
        for (int i = offset + length - 1, j = boundary.length - 1; i >= offset; --i, --j) {
            if (data[i] != boundary[j]) {
                return false
            }
        }
        return true
    }

    private static boolean isBoundaryLine(byte[] data, int offset, int length, byte[] boundary) {
        int s = boundary.length
        int end = offset + length
        return isLine(data, offset, length)
                && (length == s + 4 || length == s + 6 && data[end-4] == HYPHEN && data[end-3] == HYPHEN)
                && data[offset] == HYPHEN && data[offset+1] == HYPHEN
                && isBoundary(data, offset + 2, s, boundary)
    }

}
