package soda.web;

import java.io.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class BaseHandler implements HttpHandler {

	protected void doGet(HttpExchange exchange) throws Exception {
		throw new RuntimeException("not implemented");
	}

	protected void doPost(HttpExchange exchange) throws Exception {
		throw new RuntimeException("not implemented");
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		var method = exchange.getRequestMethod();
		var uri = exchange.getRequestURI();

		try {
			long startNano = System.nanoTime();

			if (method.equals("GET")) {
				doGet(exchange);
			} else if (method.equals("POST")) {
				doPost(exchange);
			} else {
				throw new RuntimeException("method " + method + " not supported");
			}

			long endNano = System.nanoTime();
			double elapseMs = (endNano - startNano) / 1e6;
			int code = exchange.getResponseCode();
			Logger.infof("%s %s %d %f", method, uri, code, elapseMs);
		} catch (Exception ex) {
			Utils.setResponse(exchange, 500, Utils.toString(ex));
			Logger.exception(String.format("%s %s 500 server error", method, uri), ex);
		}
	}
	
}

