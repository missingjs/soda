package soda.web;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class Utils {

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

    public static void setResponse(HttpExchange exchange, int code, String body) throws IOException {
        var data = body.getBytes(StandardCharsets.UTF_8);
        setResponse(exchange, code, data);
    }

    public static void setResponse(HttpExchange exchange, int code, byte[] body) throws IOException {
        exchange.sendResponseHeaders(code, body.length);
        exchange.getResponseBody().write(body);
        exchange.getResponseBody().close();
    }

    public static String toString(Throwable ex) {
        return soda.unittest.Utils.toString(ex);
    }

}
