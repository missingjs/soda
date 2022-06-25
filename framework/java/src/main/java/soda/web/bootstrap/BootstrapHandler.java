package soda.web.bootstrap;

import com.sun.net.httpserver.HttpExchange;
import soda.web.BaseHandler;
import soda.web.BusinessCode;
import soda.web.Logger;
import soda.web.exception.ParameterMissingException;
import soda.web.http.RequestHelper;
import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;

public class BootstrapHandler extends BaseHandler {
	
	private final ContextManager contextManager;

	public BootstrapHandler(ContextManager cm) {
		contextManager = cm;
	}

	@Override
	protected Response doGet(HttpExchange exchange) throws Exception {
		var qm = RequestHelper.queryMap(exchange);
		var key = qm.get("key");
		if (key == null) {
			throw new ParameterMissingException("key");
		}

		return contextManager.get(key)
				.map(ctx ->
					"text".equals(qm.get("format"))
						? ResponseFactory.text(ctx.getMd5())
						: ResponseFactory.success(ctx.getMd5()))
				.orElseGet(() ->
						ResponseFactory.response(
								404, BusinessCode.COMMON_ERROR,
								"no context bind to key " + key));
	}

	@Override
	protected Response doPost(HttpExchange exchange) throws Exception {
		// multipart/form-data, key = ?, jar = ?
		var data = RequestHelper.multipartFormData(exchange);

		var key = data.firstValue("key").orElseThrow(() -> new ParameterMissingException("key"));
		var bytes = data.firstFile("jar").orElseThrow(() -> new ParameterMissingException("jar"));
		contextManager.register(key, bytes);
		
		Logger.infof("reset class loader for key %s", key);
		return ResponseFactory.success();
	}

}
