import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Scanner;

public class QuizHub {

    private static final int PORT = 8080;
    private static QuizController quizController = new QuizController();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // API routes
        server.createContext("/api/questions", new QuestionsHandler());
        server.createContext("/api/submit", new SubmitHandler());

        // Static files routes (index.html, style.css, script.js)
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("QuizHub server started at http://localhost:" + PORT);
    }

    // Handles GET /api/questions
    static class QuestionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String response = quizController.getQuestionsJson();
                sendResponse(exchange, 200, "application/json", response.getBytes());
            } else {
                sendResponse(exchange, 405, "text/plain", "Method Not Allowed".getBytes());
            }
        }
    }

    // Handles POST /api/submit
    static class SubmitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Scanner scanner = new Scanner(exchange.getRequestBody()).useDelimiter("\\A");
                String requestBody = scanner.hasNext() ? scanner.next() : "";
                String response = quizController.calculateResultJson(requestBody);
                sendResponse(exchange, 200, "application/json", response.getBytes());
            } else {
                sendResponse(exchange, 405, "text/plain", "Method Not Allowed".getBytes());
            }
        }
    }

    // Serves index.html, style.css, script.js from current directory
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path) || "".equals(path)) {
                path = "/index.html";
            }

            File file = new File("." + path);
            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(path);
                byte[] bytes = Files.readAllBytes(file.toPath());
                sendResponse(exchange, 200, contentType, bytes);
            } else {
                sendResponse(exchange, 404, "text/plain", "404 Not Found".getBytes());
            }
        }
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "text/javascript";
        return "text/plain";
    }

    private static void sendResponse(HttpExchange exchange, int status, String contentType, byte[] data) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        if ("HEAD".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(status, -1);
        } else {
            exchange.sendResponseHeaders(status, data.length);
            OutputStream os = exchange.getResponseBody();
            os.write(data);
            os.close();
        }
    }
}
