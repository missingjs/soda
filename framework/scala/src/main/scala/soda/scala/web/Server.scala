package soda.scala.web

import com.sun.net.httpserver.{HttpExchange, HttpServer}
import soda.scala.web.resp.{Response, ResponseFactory}
import soda.scala.web.bootstrap.{BootstrapHandler, ContextManager}
import soda.scala.web.work.WorkHandler

import java.net.InetSocketAddress
import java.util.concurrent.{Executors, TimeUnit}

class Server(address: String, port: Int) {

  private val server = HttpServer.create(new InetSocketAddress(address, port), 0)

  private val executor = Executors.newFixedThreadPool(4)

  private val contextManager = new ContextManager

  server.setExecutor(executor)

  // GET
  server.createContext("/soda/scala/echo", new EchoHandler)

  // GET
  server.createContext("/soda/scala/stop", new BaseHandler {
    override def doGet(exchange: HttpExchange): Response = {
      stop()
      ResponseFactory.success("stop signal sent")
    }
  })

  // POST application/json
  server.createContext("/soda/scala/work", new WorkHandler(contextManager, 5000))

  // GET
  // POST, multipart/form-data
  server.createContext("/soda/scala/bootstrap", new BootstrapHandler(contextManager))

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
    var port = 9202
    var i = 0
    while (i < args.length) {
      val cmd = args(i)
      cmd match {
        case "-p" | "--port" =>
          i += 1
          port = args(i).toInt
        case _ =>
          System.err.println(s"invalid option: $cmd")
          System.exit(1)
      }
      i += 1
    }

    val address = "localhost"
    new Server(address, port).start()
    Logger.info(s"soda scala server start, listening $address:$port")
  }
}
