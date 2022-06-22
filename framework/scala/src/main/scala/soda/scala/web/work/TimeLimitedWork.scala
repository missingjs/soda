package soda.scala.web

import java.util.concurrent.FutureTask

class TimeLimitedWork(job: () => String) {

  private var jobThread: Thread = null

  def start(): FutureTask[String] = {
    val task = new FutureTask[String](() => job())
    jobThread = new Thread(task)
    jobThread.start()
    task
  }

  def kill(): Unit = {
    try {
      if (jobThread != null) {
        jobThread.stop()
      }
    } catch {
      case th: Throwable => th.printStackTrace()
    }
  }
}
