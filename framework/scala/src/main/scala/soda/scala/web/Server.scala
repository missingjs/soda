package soda.scala.web

import com.sun.net.httpserver.{HttpExchange, HttpServer}

import java.net.InetSocketAddress
import java.util.concurrent.{Executors, TimeUnit}

class Server(address: String, port: Int) {

  private val server = HttpServer.create(new InetSocketAddress(address, port), 0)

  private val executor = Executors.newFixedThreadPool(4)

  server.setExecutor(executor)

  server.createContext("/soda/scala/echo", new EchoHandler)
  server.createContext("/soda/scala/reset", new ResetHandler)
  server.createContext("/soda/scala/work", new WorkHandler)
  server.createContext("/soda/scala/stop", new BaseHandler {
    override def handleWork(exchange: HttpExchange): String = {
      stop()
      "stop signal sent"
    }
  })

  def start(): Unit = {
    server.start()
  }

  def stop(): Unit = {
    val t = new Thread(() => {
      server.stop(1)
      executor.shutdown()
      try {
        executor.awaitTermination(1, TimeUnit.SECONDS)
        Logger.info("server stopped")
      } catch {
        case th: Throwable =>
          Logger.exception("error when stop server", th)
      }
    })
    t.start()
  }

}

object Server {
  def main(args: Array[String]): Unit = {
    var port = 9202;
    var i = 0
    while (i < args.length) {
      val cmd = args(i)
      cmd match {
        case "-p" | "--port" => {
          i += 1
          port = args(i).toInt
        }
        case _ => {
          System.err.println(s"invalid option: $cmd")
          System.exit(1)
        }
      }
      i += 1
    }

    val address = "localhost"
    new Server(address, port).start()
    Logger.info(s"soda scala server start, listening ${address}:${port}")
  }
}
