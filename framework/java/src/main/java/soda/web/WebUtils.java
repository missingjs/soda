package soda.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Pattern;

public class WebUtils {

    public static Optional<String> findOne(String text, String pattern, int group) {
        var mat = Pattern.compile(pattern).matcher(text);
        return Optional.ofNullable(mat.find() ? mat.group(group) : null);
    }

    public static Optional<String> findOne(String text, String pattern) {
        return findOne(text, pattern, 1);
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        var buf = new byte[1024];
        var outs = new ByteArrayOutputStream();
        int size = 0;
        while ((size = in.read(buf)) != -1) {
            outs.write(buf, 0, size);
        }
        return outs.toByteArray();
    }

    public static String toString(Throwable ex) {
        var out = new ByteArrayOutputStream();
        var pw = new PrintStream(out);
        ex.printStackTrace(pw);
        pw.flush();
        return out.toString(StandardCharsets.UTF_8);
    }

    public static String md5Hex(byte[] data) {
        try {
            var md5 = MessageDigest.getInstance("MD5");
            md5.update(data);
            return hex(md5.digest());
        } catch (Exception ex) {
            throw new RuntimeException("md5 error", ex);
        }
    }

    public static String hex(byte[] data) {
        var code = "0123456789abcdef";
        var buf = new char[data.length * 2];
        for (int i = 0; i < data.length; ++i) {
            int high = (data[i] >> 4) & 0x0f;
            int low = data[i] & 0x0f;
            buf[i*2] = code.charAt(high);
            buf[i*2+1] = code.charAt(low);
        }
        return new String(buf);
    }

}
