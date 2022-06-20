package soda.web;

import com.sun.net.httpserver.HttpExchange;
import soda.web.http.RequestHelper;

public class EchoHandler extends BaseHandler {

	@Override
	protected void doGet(HttpExchange exchange) throws Exception {
		Utils.setResponse(exchange, 200, RequestHelper.queryString(exchange));
	}

}
