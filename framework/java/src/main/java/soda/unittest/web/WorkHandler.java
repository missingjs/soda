package soda.unittest.web;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import soda.unittest.AbstractTestJob;
import soda.unittest.CommonJob;
import soda.unittest.JobRunner;
import soda.unittest.JobTemplate;
import soda.unittest.job.JobExecutor;
import soda.unittest.job.JobSpec;
import soda.unittest.work.TestWork;

public class WorkHandler extends BaseHandler {
	
	private final ClassLoaderManager mgr;
	
	private final long timeoutMillis;
	
	public WorkHandler(ClassLoaderManager mgr, long timeoutMillis) {
		this.mgr = mgr;
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	protected String handleWork(HttpExchange exchange) throws Exception {
		String content = getPostBody(exchange);
		ObjectMapper om = new ObjectMapper();
		WorkRequest jr = om.readValue(content, WorkRequest.class);
		
		ClassLoader loader = mgr.get(jr.classpath);
		Class<?> klass = loader.loadClass(jr.bootClass);
		
		Callable<String> callable = null;
		if (CommonJob.class.isAssignableFrom(klass)) {
			callable = () -> {
				Constructor<?> ctor = klass.getDeclaredConstructor();
				ctor.setAccessible(true);
				CommonJob cj = (CommonJob) ctor.newInstance();
				return cj.execute(jr.testCase);
			};
		} else if (JobTemplate.class.isAssignableFrom(klass)) {
	    	callable = () -> {
	    		Constructor<?> ctor = klass.getDeclaredConstructor();
				ctor.setAccessible(true);
				JobTemplate<?,?> job = (JobTemplate<?,?>) ctor.newInstance();
	    		JobRunner runner = new JobRunner();
	    		return runner.runJob(jr.testCase, job);
	    	};
		} else if (AbstractTestJob.class.isAssignableFrom(klass)) {
			callable = () -> {
				var spec = (JobSpec) klass.getMethod("createSpec").invoke(null);
				var executor = new JobExecutor();
				return executor.exec(jr.testCase, spec);
			};
		} else {
			callable = () -> {
				Constructor<?> ctor = klass.getDeclaredConstructor();
			    ctor.setAccessible(true);
			    var workDef = ctor.newInstance();
				if (workDef instanceof Supplier) {
					@SuppressWarnings("unchecked")
					TestWork work = ((Supplier<TestWork>)workDef).get();
					return work.run(jr.testCase);
				} else {
					@SuppressWarnings("unchecked")
					var func = ((Function<String, String>) workDef);
					return func.apply(jr.testCase);
				}
			};
		}
		
		TimeLimitedJob tLJob = new TimeLimitedJob(callable);
		FutureTask<String> future = tLJob.start();
    	try {
    		return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
    	} catch (TimeoutException tex) {
    		tLJob.kill();
    		throw new RuntimeException("Job timeout");
    	}
	}

}
