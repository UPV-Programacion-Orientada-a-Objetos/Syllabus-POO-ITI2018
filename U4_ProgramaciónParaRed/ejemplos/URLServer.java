package U4_ProgramaciónParaRed.ejemplos;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class URLServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3333), 0);
        server.createContext("/something", new PostHandler());
        server.setExecutor(null);
        server.start();
    }

    private static class PostHandler implements HttpHandler {

        public void handle(HttpExchange exch) {
            System.out.println(); // to skip the row
            System.out.println(exch.getRequestURI());
            System.out.println(exch.getHttpContext().getPath());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(exch.getRequestBody()));
            OutputStream os = exch.getResponseBody()) {
                System.out.println("Receive as body: ");
                in.lines().forEach(l -> System.out.println(" " + l));

                String confirm = "Got it! Thanks.";
                exch.sendResponseHeaders(200, confirm.length());
                os.write(confirm.getBytes());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
}