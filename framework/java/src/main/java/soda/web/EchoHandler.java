package soda.web;

import com.sun.net.httpserver.HttpExchange;
import soda.web.http.RequestHelper;
import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;

public class EchoHandler extends BaseHandler {

	@Override
	protected Response doGet(HttpExchange exchange) throws Exception {
		return ResponseFactory.success("echo " + RequestHelper.queryString(exchange));
	}

}
