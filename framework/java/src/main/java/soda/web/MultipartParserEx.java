package soda.web;

import soda.web.http.ContentDisposition;
import soda.web.http.Part;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipartParserEx {

    private final String boundary;

    private ByteLineInputStream lineInput;

    private static final int BUF_SIZE = 1024;

    public MultipartParserEx(InputStream input, String boundary) {
        this.boundary = boundary;
        lineInput = new ByteLineInputStream(input, 4096);
    }

    public List<Part> parse() throws IOException {
        byte[] boundaryBytes = boundary.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[BUF_SIZE];

        int size = lineInput.readLine(buffer);
        if (!isBoundaryLine(buffer, 0, size, boundaryBytes)) {
            throw new RuntimeException("body not start with boundary " + boundary);
        }

        var partList = new ArrayList<Part>();
        boolean reachEnd = false;
        while (!reachEnd) {
            var part = new Part();
            // read headers in a part
            while (true) {
                String headerLine = null;
                size = lineInput.readLine(buffer);
                if (isLine(buffer, 0, size)) {
                    headerLine = new String(buffer, 0, size - 2, StandardCharsets.UTF_8);
                } else {
                    var bs = new ByteArrayOutputStream();
                    bs.write(buffer, 0, size);
                    do {
                        size = lineInput.readLine(buffer);
                        bs.write(buffer, 0, size);
                    } while (!isLine(buffer, 0, size));
                    var dataBytes = bs.toByteArray();
                    headerLine = new String(dataBytes, 0, dataBytes.length - 2, StandardCharsets.UTF_8);
                }
                if (headerLine.length() == 0) {
                    // encounter the separator between a part's header and body
                    break;
                }
                if (headerLine.startsWith("Content-Disposition:")) {
                    part.contentDisposition = ContentDisposition.fromHeaderLine(headerLine);
                } else if (headerLine.startsWith("Content-Type:")) {
                    part.contentType = headerLine.split(":", 2)[1].trim();
                }
            }

            if (part.contentDisposition == null) {
                throw new RuntimeException("no Content-Disposition header in part");
            }

            // read body in a part
            byte[] ready = new byte[BUF_SIZE];
            int readySize = lineInput.readLine(ready);
            ByteArrayOutputStream overflow = null;

            while (true) {
                int n = lineInput.readLine(buffer);
                final int bbSize = boundaryBytes.length;
                if (isBoundaryLine(buffer, 0, n, boundaryBytes)) {
                    if (overflow == null) {
                        // exclude trailing "\r\n"
                        part.payload = Arrays.copyOf(ready, readySize - 2);
                    } else {
                        // exclude trailing "\r\n"
                        overflow.write(ready, 0, readySize - 2);
                        part.payload = overflow.toByteArray();
                    }
                    if (n == bbSize + 6) {
                        reachEnd = true;
                    }
                    break;
                } else {
                    if (overflow == null) {
                        overflow = new ByteArrayOutputStream();
                    }
                    overflow.write(ready, 0, readySize);
                    var _temp = ready;
                    ready = buffer;
                    buffer = _temp;
                    readySize = n;
                }
            }
            partList.add(part);
        }
        return partList;
    }

    private boolean isLine(byte[] data, int offset, int length) {
        int end = offset + length;
        return data[end-1] == '\n' && data[end-2] == '\r';
    }

    private boolean isBoundary(byte[] data, int offset, int length, byte[] boundary) {
        for (int i = offset + length - 1, j = boundary.length - 1; i >= offset; --i, --j) {
            if (data[i] != boundary[j]) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoundaryLine(byte[] data, int offset, int length, byte[] boundary) {
        int s = boundary.length;
        int end = offset + length;
        return isLine(data, offset, length)
                && (length == s + 4 || length == s + 6 && data[end-4] == '-' && data[end-3] == '-')
                && data[offset] == '-' && data[offset+1] == '-'
                && isBoundary(data, offset + 2, s, boundary);
    }

}
