package soda.web.bootstrap;

import com.sun.net.httpserver.HttpExchange;
import soda.web.BaseHandler;
import soda.web.Logger;
import soda.web.exception.ParameterMissingException;
import soda.web.http.RequestHelper;
import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;

public class BootstrapHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	public BootstrapHandler(ClassLoaderManager mgr) {
		this.mgr = mgr;
	}

	@Override
	protected Response doGet(HttpExchange exchange) throws Exception {
		var qm = RequestHelper.queryMap(exchange);
		var key = qm.get("key");
		if (key == null) {
			throw new ParameterMissingException("key");
		}
//		mgr.
		return null;
	}

	@Override
	protected Response doPost(HttpExchange exchange) throws Exception {
		// multipart/form-data, key = ?, jar = ?
		var data = RequestHelper.multipartFormData(exchange);

		var key = data.firstValue("key").orElseThrow(() -> new ParameterMissingException("key"));
		var bytes = data.firstFile("jar").orElseThrow(() -> new ParameterMissingException("jar"));
		mgr.setupForJar(key, bytes);
		
		Logger.infof("reset class loader for key %s", key);
		return ResponseFactory.success();
	}

}
