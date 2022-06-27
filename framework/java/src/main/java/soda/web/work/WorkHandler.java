package soda.web.work;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import soda.unittest.TestWork;
import soda.unittest.Utils;
import soda.web.BaseHandler;
import soda.web.BusinessCode;
import soda.web.Logger;
import soda.web.bootstrap.ContextManager;
import soda.web.exception.ServiceException;
import soda.web.http.RequestHelper;
import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;

public class WorkHandler extends BaseHandler {
	
	private final ContextManager contextManager;
	
	private final long timeoutMillis;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public WorkHandler(ContextManager mgr, long timeoutMillis) {
		contextManager = mgr;
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	protected Response doPost(HttpExchange exchange) throws Exception {
		var req = parse(exchange);
		Logger.infof("context key: %s", req.key);
		Logger.infof("entry class: %s", req.entryClass);
		Logger.infof("test case: %s", req.testCase);
		
		var classLoader = contextManager.get(req.key).orElseThrow(() ->
				new RuntimeException("no context found by key " + req.key)
		).getClassLoader();
		Class<?> klass = classLoader.loadClass(req.entryClass);

		Callable<String> callable = () -> {
			Constructor<?> ctor = klass.getDeclaredConstructor();
			ctor.setAccessible(true);
			var workDef = ctor.newInstance();
			if (workDef instanceof Supplier) {
				return Utils.<Supplier<TestWork>>cast(workDef).get().run(req.testCase);
			} else {
				return Utils.<Function<String, String>>cast(workDef).apply(req.testCase);
			}
		};
		
		TimeLimitedJob tLJob = new TimeLimitedJob(callable);
		FutureTask<String> future = tLJob.start();
    	try {
    		var result = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
			Logger.infof("test output: %s", result);
			return ResponseFactory.text(result);
    	} catch (TimeoutException tex) {
    		tLJob.kill();
    		throw new RuntimeException("Job timeout", tex);
    	}
	}

	private static WorkRequest parse(HttpExchange exchange) throws Exception {
		var contentType = exchange.getRequestHeaders().getFirst("Content-Type");
		if (contentType.contains("application/json")) {
			var body = RequestHelper.bodyString(exchange);
			return objectMapper.readValue(body, WorkRequest.class);
		} else if (contentType.contains("multipart/form-data")) {
			var formData = RequestHelper.multipartFormData(exchange);
			var key = formData.firstValue("key");
			var entryClass = formData.firstValue("entry_class");
			var caseBytes = formData.firstFile("test_case");
			var testCase = new String(caseBytes, StandardCharsets.UTF_8);
			var workReq = new WorkRequest();
			workReq.key = key;
			workReq.entryClass = entryClass;
			workReq.testCase = testCase;
			return workReq;
		} else {
			throw new ServiceException(BusinessCode.COMMON_ERROR, "unknown Content-Type: " + contentType, 400);
		}
	}

}
