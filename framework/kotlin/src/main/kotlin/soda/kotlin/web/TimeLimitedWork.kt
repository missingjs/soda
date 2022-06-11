package soda.kotlin.web

import java.util.concurrent.FutureTask

class TimeLimitedWork(private val job: () -> String) {

    private var jobThread: Thread? = null

    fun start(): FutureTask<String> {
        return FutureTask { job() }.also {
            jobThread = Thread(it)
            jobThread!!.start()
        }
    }

    fun kill() {
        try {
            jobThread?.stop()
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }
}