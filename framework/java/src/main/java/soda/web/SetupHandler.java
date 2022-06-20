package soda.web;

import com.sun.net.httpserver.HttpExchange;

public class SetupHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	public SetupHandler(ClassLoaderManager mgr) {
		this.mgr = mgr;
	}

	@Override
	protected String handleWork(HttpExchange exchange) throws Exception {
		// multipart/form-data, key = ?, jar = ?
		var parts = parseMultipart(exchange);
		String key = parts.get("key").bodyString();
		mgr.remove(key);

		var bytes = parts.get("jar").payload;
		mgr.setupForJar(key, bytes);

		return "reset class loader with " + key;
	}

}
