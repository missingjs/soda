package soda.web;

import com.sun.net.httpserver.HttpExchange;
import soda.web.http.RequestHelper;

public class SetupHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	public SetupHandler(ClassLoaderManager mgr) {
		this.mgr = mgr;
	}

	@Override
	protected void doPost(HttpExchange exchange) throws Exception {
		// multipart/form-data, key = ?, jar = ?
		var data = RequestHelper.multipartFormData(exchange);
		var key = data.firstValue("key");
		mgr.remove(key);

		var bytes = data.firstFile("jar");
		mgr.setupForJar(key, bytes);

		Logger.infof("reset class loader for key %s", key);

		Utils.setResponse(exchange, 200, "success");
	}

}
