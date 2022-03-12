package soda.groovy.web

import java.util.concurrent.FutureTask

class TimeLimitedJob {

    private Closure<String> job

    private Thread jobThread

    TimeLimitedJob(Closure<String> job) {
        this.job = job
    }

    FutureTask<String> start() {
        FutureTask<String> task = new FutureTask<>(() -> job.call())
        jobThread = new Thread(task)
        jobThread.start()
        task
    }

    @SuppressWarnings("deprecation")
    void kill() {
        try {
            if (jobThread != null) {
                jobThread.stop()
            }
        } catch (Throwable th) {
            th.printStackTrace()
        }
    }

}
