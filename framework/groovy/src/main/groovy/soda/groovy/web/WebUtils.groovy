package soda.groovy.web

import java.nio.charset.StandardCharsets

class WebUtils {

    static Optional<String> findOne(String text, String pattern, int group) {
        def mat = text =~ /$pattern/
        return Optional.ofNullable(mat.find() ? mat.group(group) : null)
    }

    static Optional<String> findOne(String text, String pattern) {
        return findOne(text, pattern, 1)
    }

    static byte[] toByteArray(InputStream input) throws IOException {
        def buf = new byte[1024]
        def outs = new ByteArrayOutputStream()
        int size
        while ((size = input.read(buf)) != -1) {
            outs.write(buf, 0, size)
        }
        return outs.toByteArray()
    }

    static String toString(Throwable ex) {
        def out = new ByteArrayOutputStream()
        def pw = new PrintStream(out)
        ex.printStackTrace(pw)
        pw.flush()
        out.toString(StandardCharsets.UTF_8)
    }

}
