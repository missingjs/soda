package soda.web;

import java.io.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;

public abstract class BaseHandler implements HttpHandler {

	protected Response doGet(HttpExchange exchange) throws Exception {
		throw new RuntimeException("not implemented");
	}

	protected Response doPost(HttpExchange exchange) throws Exception {
		throw new RuntimeException("not implemented");
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		var method = exchange.getRequestMethod();
		var uri = exchange.getRequestURI();

		Response resp = null;
		try {
			long startNano = System.nanoTime();
			
			if (method.equals("GET")) {
				resp = doGet(exchange);
			} else if (method.equals("POST")) {
				resp = doPost(exchange);
			} else {
				throw new RuntimeException("method " + method + " not supported");
			}

			long endNano = System.nanoTime();
			double elapseMs = (endNano - startNano) / 1e6;
			int code = resp.getHttpCode();
			Logger.infof("%s %s %d %f", method, uri, code, elapseMs);
		} catch (ServiceException ex) {
			String message = String.format("%s %s %d business error", method, uri, ex.getHttpCode());
			Logger.exception(message, ex);
			resp = ResponseFactory.exception(ex);
		} catch (Throwable ex) {
			Logger.exception(String.format("%s %s 500 internal error", method, uri), ex);
			resp = ResponseFactory.exception(ex);
		}

		writeResponse(exchange, resp);
	}

	private void writeResponse(HttpExchange exchange, Response resp) throws IOException {
		var headers = resp.getHeaders();
		for (var key : headers.keySet()) {
			var values = headers.get(key);
			for (var value: values) {
				exchange.getResponseHeaders().add(key, value);
			}
		}

		var body = resp.getBody();
		exchange.getResponseHeaders().set("Content-Type", resp.getContentType());
		exchange.sendResponseHeaders(resp.getHttpCode(), body.length);
		exchange.getResponseBody().write(body);
        exchange.getResponseBody().close();
	}
	
}

