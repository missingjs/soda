package soda.kotlin.web

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.net.URL
import java.net.URLClassLoader
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.system.exitProcess

class Server(private val address: String, private val port: Int) {

    private val server = HttpServer.create(InetSocketAddress(address, port), 0)

    private val executor = Executors.newFixedThreadPool(4)

    init {
        server.executor = executor

        // GET
        server.createContext("/soda/kotlin/echo", EchoHandler())

        // POST
        server.createContext("/soda/kotlin/setup", SetupHandler())

        // POST
        server.createContext("/soda/kotlin/work", WorkHandler())

        // GET
        server.createContext("/soda/kotlin/stop", object: BaseHandler() {
            override fun handleWork(exchange: HttpExchange): String {
                stop()
                return "stop signal sent"
            }
        })
    }

    fun start() = server.start()

    fun stop() {
        val t = Thread {
            server.stop(1)
            executor.shutdown()
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS)
                Logger.info("server stopped")
            } catch (th: Throwable) {
                Logger.exception("error when stop server", th)
            }
        }
        t.start()
    }

}

fun main(args: Array<String>) {
    var port = 9204
    var i = 0
    while (i < args.size) {
        val cmd = args[i]
        if (cmd == "-p" || cmd == "--port") {
            port = args[++i].toInt()
        } else {
            System.err.println("invalid option: $cmd")
            exitProcess(1)
        }
        ++i
    }

    val address = "localhost"
    Server(address, port).start()
    Logger.info("soda kotlin server start, listening $address:$port")
}
