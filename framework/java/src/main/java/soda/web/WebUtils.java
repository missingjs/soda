package soda.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class WebUtils {

    public static String findOne(String text, String pattern, int group) {
        var mat = Pattern.compile(pattern).matcher(text);
        return mat.find() ? mat.group(group) : null;
    }

    public static String findOne(String text, String pattern) {
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

}
