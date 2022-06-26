package soda.web.work;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import soda.unittest.TestWork;
import soda.web.BaseHandler;
import soda.web.Logger;
import soda.web.bootstrap.ContextManager;
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
		String content = RequestHelper.bodyString(exchange);
		WorkRequest jr = objectMapper.readValue(content, WorkRequest.class);

		Logger.infof("context key: %s", jr.key);
		Logger.infof("boot class: %s", jr.bootClass);
		Logger.infof("test case: %s", jr.testCase);
		
		var classLoader = contextManager.get(jr.key).orElseThrow(() ->
				new RuntimeException("no context found by key " + jr.key)
		).getClassLoader();
		Class<?> klass = classLoader.loadClass(jr.bootClass);

		Callable<String> callable = () -> {
			Constructor<?> ctor = klass.getDeclaredConstructor();
			ctor.setAccessible(true);
			var workDef = ctor.newInstance();
			if (workDef instanceof Supplier) {
				@SuppressWarnings("unchecked")
				TestWork work = ((Supplier<TestWork>) workDef).get();
				return work.run(jr.testCase);
			} else {
				@SuppressWarnings("unchecked")
				var func = ((Function<String, String>) workDef);
				return func.apply(jr.testCase);
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

}
