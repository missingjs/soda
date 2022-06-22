package soda.web.http;

import com.sun.net.httpserver.HttpExchange;
import soda.web.Utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RequestHelper {

    public static String queryString(HttpExchange exchange) {
        return exchange.getRequestURI().getQuery();
    }

    public static String contentType(HttpExchange exchange) {
        var ct = exchange.getRequestHeaders().getFirst("Content-Type");
        return ct != null ? ct : "";
    }

    public static Map<String, String> queryMap(HttpExchange exchange) {
        return queryStringToMap(queryString(exchange));
    }

    public static Map<String, List<String>> queryMultimap(HttpExchange exchange) {
        return queryStringToMultimap(queryString(exchange));
    }

    public static byte[] body(HttpExchange exchange) throws IOException {
        return Utils.toByteArray(exchange.getRequestBody());
    }

    public static String bodyString(HttpExchange exchange) throws IOException {
        return new String(body(exchange), StandardCharsets.UTF_8);
    }

    public static Map<String, String> formMap(HttpExchange exchange) throws IOException {
        var cType = contentType(exchange);
        if (cType.startsWith("application/x-www-form-urlencoded")) {
            return queryStringToMap(bodyString(exchange));
        } else {
            return Collections.emptyMap();
        }
    }

    public static Map<String, List<String>> formMultimap(HttpExchange exchange) throws IOException {
        var cType = contentType(exchange);
        if (cType.startsWith("application/x-www-form-urlencoded")) {
            return queryStringToMultimap(bodyString(exchange));
        } else {
            return Collections.emptyMap();
        }
    }

    public static List<Part> formParts(HttpExchange exchange) throws IOException {
        var boundary = parseBoundary(contentType(exchange));
        var parser = new MultipartParserEx(exchange.getRequestBody(), boundary);
        return parser.parse();
    }

    public static MultipartFormData multipartFormData(HttpExchange exchange) throws IOException {
        var m = formParts(exchange).stream().collect(Collectors.groupingBy(Part::getName));
        return new MultipartFormData(m);
    }

    private static Map<String, String> queryStringToMap(String query) {
        return Arrays.stream(query.split("&"))
                .map(s -> s.split("=", 2))
                .filter(ss -> ss.length == 2)
                .collect(Collectors.toMap(ss -> decode(ss[0]), ss -> decode(ss[1])));
    }

    private static Map<String, List<String>> queryStringToMultimap(String query) {
        return Arrays.stream(query.split("&"))
                .map(s -> s.split("=", 2))
                .filter(ss -> ss.length == 2)
                .collect(
                        Collectors.groupingBy(
                                ss -> decode(ss[0]),
                                Collectors.mapping(
                                        ss -> decode(ss[1]),
                                        Collectors.toList())));
    }

    private static String decode(String encodedText) {
        return URLDecoder.decode(encodedText, StandardCharsets.UTF_8);
    }

    private static String parseBoundary(String contentType) {
        String boundary = match("boundary=\"(.+?)\"", contentType, 1);
        if (boundary == null) {
            boundary = match("boundary=(\\S+)", contentType, 1);
            if (boundary == null) {
                throw new RuntimeException("no boundary found in content type header");
            }
        }
        return boundary;
    }

    private static String match(String pattern, String text, int group) {
        var p = Pattern.compile(pattern);
        var m = p.matcher(text);
        return m.find() ? m.group(group) : null;
    }

}
