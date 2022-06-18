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
		// multipart/form-data, key = ?, jar = ?
		var parts = parseMultipart(exchange);
//		String content = getPostBody(exchange);
//		Map<String, String> params = parseQuery(content);
		String key = parts.get("key").bodyString();
		mgr.remove(key);

//		String jarB64 = params.get("jar");
//		byte[] bytes = Base64.getUrlDecoder().decode(jarB64);
		var bytes = parts.get("jar").contentBytes;
		mgr.setupForJar(key, bytes);

		return "reset class loader with " + key;
	}

}
