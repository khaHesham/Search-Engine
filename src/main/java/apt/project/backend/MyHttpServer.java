package apt.project.backend;

import apt.project.QueryProcessor.QueryProcessor;
import com.sun.net.httpserver.HttpServer;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyHttpServer {
    public static void main(String []args) {

        HttpServer httpServer = null;
        QueryProcessor qp = new QueryProcessor("");

        Jedis jedis = new Jedis("ec2-54-82-59-13.compute-1.amazonaws.com", 6379);
        jedis.select(9);

        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setting the handler for the GET method.
        httpServer.createContext("/", new PostHandler(jedis, qp));

        httpServer.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        httpServer.start();
    }
}
