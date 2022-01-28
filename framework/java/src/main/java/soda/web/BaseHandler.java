package soda.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import soda.unittest.Utils;

public abstract class BaseHandler implements HttpHandler {
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		var method = exchange.getRequestMethod();
		var uri = exchange.getRequestURI();
		try {
			String result = handleWork(exchange);
			sendMessage(exchange, 200, result);
			Logger.info(String.format("%s %s 200 %s", method, uri, result));
		} catch (Exception ex) {
			Logger.exception(String.format("%s %s 500 request handling error", method, uri), ex);
			sendMessageWithCatch(exchange, 500, Utils.toString(ex));
		}
	}
	
	protected abstract String handleWork(HttpExchange exchange) throws Exception;

	protected void sendMessage(HttpExchange exch, int code, String message) throws IOException {
		byte[] data = message.getBytes(StandardCharsets.UTF_8);
		exch.sendResponseHeaders(code, data.length);
		exch.getResponseBody().write(data);
		exch.getResponseBody().close();
	}

	protected void sendMessageWithCatch(HttpExchange exch, int code, String message) {
		try {
			sendMessage(exch, code, message);
		} catch (IOException ex) {
			var msg = String.format("message sending error, code - %d, msg - %s", code, message);
			Logger.exception(msg, ex);
		}
	}
	
	protected Map<String, String> parseQuery(HttpExchange exch) {
    	String query = exch.getRequestURI().getQuery();
    	return parseQuery(query);
    }
    
	protected Map<String, String> parseQuery(String query) {
    	return Arrays.stream(query.split("[&]"))
        		.map(s -> s.split("="))
        		.filter(ss -> ss.length == 2)
        		.collect(Collectors.toMap(ss -> ss[0], ss->ss[1]));
    }
	
	protected String getPostBody(HttpExchange exch) throws IOException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(exch.getRequestBody(), StandardCharsets.UTF_8));
    	StringBuilder buf = new StringBuilder();
    	String line = null;
    	while ((line = reader.readLine()) != null) {
    		buf.append(line);
    	}
    	reader.close();
    	return buf.toString();
    }
	
}

