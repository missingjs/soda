package soda.groovy.web

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

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

    static String md5Hex(byte[] data) {
        try {
            def md5 = MessageDigest.getInstance("MD5")
            md5.update(data)
            return hex(md5.digest())
        } catch (Exception ex) {
            throw new RuntimeException("md5 error", ex)
        }
    }

    static String hex(byte[] data) {
        var code = "0123456789abcdef"
        var buf = new char[data.length * 2]
        for (int i = 0; i < data.length; ++i) {
            int high = (data[i] >> 4) & 0x0f
            int low = data[i] & 0x0f
            buf[i*2] = code.charAt(high)
            buf[i*2+1] = code.charAt(low)
        }
        return new String(buf)
    }

}
