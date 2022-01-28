package soda.unittest.web;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class SetupHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	public SetupHandler(ClassLoaderManager mgr) {
		this.mgr = mgr;
	}

	@Override
	protected String handleWork(HttpExchange exchange) throws Exception {
		String content = getPostBody(exchange);
		Map<String, String> params = parseQuery(content);
		String classpath = params.get("classpath");
		mgr.remove(classpath);
		return "reset class loader for " + classpath;
	}

}
