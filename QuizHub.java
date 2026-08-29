import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class QuizHub implements HttpHandler {

    static QuizController quiz = new QuizController();


    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        server.createContext("/", new QuizHub());

        server.start();

        System.out.println("QuizHub is running at http://localhost:8080");
    }


    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();


        if (path.equals("/api/questions") && method.equals("GET")) {

            send(exchange, quiz.getQuestions(), "application/json");

        }

        else if (path.equals("/api/submit") && method.equals("POST")) {

            String answers = new String(
                    exchange.getRequestBody().readAllBytes()
            );

            send(exchange, quiz.checkAnswers(answers), "application/json");

        }

        else {

            showFile(exchange, path);
        }
    }


    static void showFile(HttpExchange exchange, String path)
            throws IOException {

        if (path.equals("/")) {
            path = "/index.html";
        }

        Path file = Path.of("." + path);

        if (Files.exists(file)) {

            byte[] data = Files.readAllBytes(file);

            String type = "text/plain";

            if (path.endsWith(".html")) {
                type = "text/html";
            }

            else if (path.endsWith(".css")) {
                type = "text/css";
            }

            else if (path.endsWith(".js")) {
                type = "text/javascript";
            }

            send(exchange, data, type);
        }

        else {

            send(exchange, "File not found", "text/plain");
        }
    }


    static void send(
            HttpExchange exchange,
            String text,
            String type
    ) throws IOException {

        send(exchange, text.getBytes(), type);
    }


    static void send(
            HttpExchange exchange,
            byte[] data,
            String type
    ) throws IOException {

        exchange.getResponseHeaders().set(
                "Content-Type",
                type + "; charset=UTF-8"
        );

        exchange.sendResponseHeaders(200, data.length);

        OutputStream output = exchange.getResponseBody();

        output.write(data);

        output.close();
    }
}