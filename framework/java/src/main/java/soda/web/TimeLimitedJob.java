package soda.web;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class TimeLimitedJob {

	private Callable<String> job;
	
	private Thread jobThread;
	
	public TimeLimitedJob(Callable<String> job) {
		this.job = job;
	}
	
	public FutureTask<String> start() {
		FutureTask<String> task = new FutureTask<>(job);
		jobThread = new Thread(task);
		jobThread.start();
		return task;
	}
	
	@SuppressWarnings("deprecation")
	public void kill() {
		try {
			if (jobThread != null) {
				jobThread.stop();
			}
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	
}
