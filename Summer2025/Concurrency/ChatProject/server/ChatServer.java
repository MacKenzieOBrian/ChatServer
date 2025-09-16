package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static final int PORT = 9090;
    
    // These are my client handlers 
    private final List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    // This is my thread pool for connected clients
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void start() {
        System.out.println("Chat Server started on port: " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!serverSocket.isClosed()) {
                try {
                    //accept a new client connection
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // add them to the list of client handlers
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clientHandlers.add(clientHandler);

                    // handle client in a separate thread
                    executorService.execute(clientHandler);
                } catch (IOException e) {
                    System.err.println("IOException in server loop: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT + ": " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    // message to all clients except the sender
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    
    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Client disconnected: " + clientHandler.getClientAddress());
        broadcastMessage("Server: " + clientHandler.getClientIdentifier() + " has left the chat.", null);
    }

    /**
     * Shuts down the server.
     */
    public void shutdown() {
        System.out.println("Shutting down server...");
        executorService.shutdown();
        System.out.println("Server shutdown complete.");
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
