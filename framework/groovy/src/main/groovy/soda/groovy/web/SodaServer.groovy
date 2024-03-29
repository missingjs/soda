package soda.groovy.web

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import soda.groovy.web.bootstrap.ContextManager
import soda.groovy.web.resp.Response
import soda.groovy.web.resp.ResponseFactory

import soda.groovy.web.bootstrap.BootstrapHandler
import soda.groovy.web.work.WorkHandler

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SodaServer {

    private String bindAddress

    private int port

    private HttpServer server

    private ExecutorService executor

    private int concurrency = 20

    private final ContextManager contextManager = new ContextManager()

    static void main(String... args) throws Exception {
        def address = "0.0.0.0"
        int port = 9203
        int i = 0
        while (i < args.length) {
            var cmd = args[i]
            if (cmd == "-p" || cmd == "--port") {
                ++i
                port = Integer.parseInt(args[i])
            } else if (cmd == "--address") {
                ++i
                address = args[i]
            } else {
                System.err.println("invalid option: $cmd")
                System.exit(1)
            }
            ++i
        }

        def ss = new SodaServer(address, port)
        ss.start()
        Logger.info("soda java server start, listening $address:$port")
    }

    SodaServer(String bindAddress, int port) throws IOException {
        this.bindAddress = bindAddress
        this.port = port
        server = HttpServer.create(new InetSocketAddress(this.bindAddress, this.port), 0)
        executor = Executors.newFixedThreadPool(concurrency)
        server.setExecutor(executor)
        initialize()
    }

    private class StopHandler extends BaseHandler {
        @Override
        Response doGet(HttpExchange exchange) throws Exception {
            stop()
            return ResponseFactory.success("stop signal sent")
        }
    }

    private void initialize() {
        // GET
        server.createContext("/soda/groovy/echo", new EchoHandler())

        // GET
        server.createContext("/soda/groovy/stop", new StopHandler())

        // POST, application/json
        server.createContext("/soda/groovy/work", new WorkHandler(contextManager, 5000))

        // GET
        // POST, multipart/form-data
        server.createContext("/soda/groovy/bootstrap", new BootstrapHandler(contextManager))
    }

    void start() {
        server.start()
    }

    void stop() {
        new Thread(() -> {
            server.stop(1)
            executor.shutdown()
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS)
                System.out.println("server stop")
            } catch (Exception ex) {
                ex.printStackTrace()
            }
        }).start()
    }

}
