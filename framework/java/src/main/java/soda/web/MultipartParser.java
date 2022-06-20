package soda.web;

import soda.web.http.ContentDisposition;
import soda.web.http.Part;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MultipartParser {

    private InputStream source;

    private final String boundary;

    public MultipartParser(InputStream input, String boundary) {
        this.source = input;
        this.boundary = boundary;
    }

    public List<Part> parse() throws IOException {
        var contentBytes = Utils.toByteArray(source);
        var bounds = boundary.getBytes(StandardCharsets.UTF_8);
        var parts = new ArrayList<Part>();

        var start = 0;
        var end = locateNewLine(contentBytes, start);
        if (!isBoundary(contentBytes, start, end, bounds)) {
            reportFormatError();
        }

        start = end + 2;
        do {
            var part = new Part();

            // read headers in a part
            while (true) {
                end = locateNewLine(contentBytes, start);
                if (end == start) {  // blank line, between header and body in a part
                    start = end + 2;
                    break;
                }
                var header = new String(contentBytes, start, end - start, StandardCharsets.UTF_8);
                start = end + 2;
                if (header.startsWith("Content-Disposition:")) {
                    part.contentDisposition = ContentDisposition.fromHeaderLine(header);
                } else if (header.startsWith("Content-Type:")) {
                    part.contentType = header.split(":", 2)[1];
                }
            }

            // read body in a part
            var bytebuf = new ByteArrayOutputStream();
            int n = 0;
            while (true) {
                end = locateNewLine(contentBytes, start);
                if (isBoundary(contentBytes, start, end, bounds)) {
                    break;
                }
                if (n > 0) {
                    bytebuf.write('\r');
                    bytebuf.write('\n');
                }
                bytebuf.write(contentBytes, start, end - start);
                start = end + 2;
                ++n;
            }
            part.payload = bytebuf.toByteArray();
            parts.add(part);

        } while (contentBytes[end-1] != '-' || contentBytes[end-2] != '-');
        return parts;
    }

    private void reportFormatError() {
        throw new RuntimeException("invalid multipart format");
    }

    private int locateNewLine(byte[] bytes, int start) {
        int p = start;
        while (p < bytes.length) {
            if (bytes[p] == '\r' && p < bytes.length - 1 && bytes[p+1] == '\n') {
                return p;
            }
            ++p;
        }
        return p;
    }

    private boolean isBoundary(byte[] bytes, int start, int end, byte[] bound) {
        if (end - start < bound.length + 2 || bytes[start] != '-' || bytes[start+1] != '-') {
            return false;
        }
        for (int i = start + 2, j = 0; j < bound.length; ++i, ++j) {
            if (bytes[i] != bound[j]) {
                return false;
            }
        }
        return true;
    }

}
