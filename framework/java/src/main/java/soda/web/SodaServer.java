package soda.web;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import soda.web.bootstrap.ContextManager;
import soda.web.resp.Response;
import soda.web.resp.ResponseFactory;
import soda.web.bootstrap.BootstrapHandler;
import soda.web.work.WorkHandler;

public class SodaServer {

	private String bindAddress;
	
	private int port;
	
	private HttpServer server;
	
	private ExecutorService executor;
	
	private int concurrency = 20;
	
	private final ContextManager contextManager = new ContextManager();
	
	public static void main(String[] args) throws Exception {
		var address = "0.0.0.0";
		int port = 9201;
		int i = 0;
		while (i < args.length) {
			var cmd = args[i];
			if (cmd.equals("-p") || cmd.equals("--port")) {
				++i;
				port = Integer.parseInt(args[i]);
			} else if (cmd.equals("--address")) {
				++i;
				address = args[i];
			} else {
				System.err.println("invalid option: " + cmd);
				System.exit(1);
			}
			++i;
		}

		SodaServer ss = new SodaServer(address, port);
		ss.start();
		Logger.infof("soda java server start, listening %s:%d", address, port);
	}
	
	public SodaServer(String bindAddress, int port) throws IOException {
		this.bindAddress = bindAddress;
		this.port = port;
		server = HttpServer.create(new InetSocketAddress(this.bindAddress, this.port), 0);
        executor = Executors.newFixedThreadPool(concurrency);
        server.setExecutor(executor);
        initialize();
	}
	
	private class StopHandler extends BaseHandler {
		@Override
		public Response doGet(HttpExchange exchange) throws Exception {
			stop();
			return ResponseFactory.success("stop signal sent");
		}
	}
	
	private void initialize() {
		// GET
		server.createContext("/soda/java/echo", new EchoHandler());
		
		// GET
		server.createContext("/soda/java/stop", new StopHandler());
		
		// POST, application/json
		server.createContext("/soda/java/work", new WorkHandler(contextManager, 5000));

		// GET
		// POST, multipart/form-data
		server.createContext("/soda/java/bootstrap", new BootstrapHandler(contextManager));
	}
	
	public void start() {
		server.start();
	}
	
	public void stop() {
		new Thread(() -> {
        	server.stop(1);
        	executor.shutdown();
        	try {
				int shutdownTimeout = 1;
        		if (!executor.awaitTermination(shutdownTimeout, TimeUnit.SECONDS)) {
					Logger.errorf("executor not terminate after %d", shutdownTimeout);
				}
        		System.out.println("server stop");
        	} catch (Exception ex) {
        		ex.printStackTrace();
        	}
        }).start();
	}
	
}
