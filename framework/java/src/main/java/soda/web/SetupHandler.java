package soda.web;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class SetupHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	public SetupHandler(ClassLoaderManager mgr) {
		this.mgr = mgr;
	}

	@Override
	protected String handleWork(HttpExchange exchange) throws Exception {
		// key=xxx&jar=xxx
		String content = getPostBody(exchange);
		Map<String, String> params = parseQuery(content);
		final String key = params.get("key");
		mgr.remove(key);

		String jarB64 = params.get("jar");
		byte[] bytes = Base64.getUrlDecoder().decode(jarB64);
		mgr.setupForJar(key, bytes);

		return "reset class loader with " + key;
	}

}
