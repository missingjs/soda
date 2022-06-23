package soda.web.setup;

import com.sun.net.httpserver.HttpExchange;
import soda.web.BaseHandler;
import soda.web.Logger;
import soda.web.exception.ParameterMissingException;
import soda.web.http.RequestHelper;
import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;

public class SetupHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	public SetupHandler(ClassLoaderManager mgr) {
		this.mgr = mgr;
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
