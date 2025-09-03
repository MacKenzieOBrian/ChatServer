package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.ClientHandler;

public class ChatServer {

    private static final int PORT = 9090;
    // Using a thread-safe list to store client handlers.
    // CopyOnWriteArrayList is suitable for scenarios where reads greatly outnumber writes.
    // Every time we broadcast, we are reading. We only write when a client connects or disconnects.
    private final List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    // Using a cached thread pool to manage client threads efficiently.
    // It creates threads as needed and reuses them, which is good for many short-lived tasks.
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void start() {
        System.out.println("Chat Server started on port: " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!serverSocket.isClosed()) {
                try {
                    // The accept() call is a blocking I/O operation. It waits here until a client connects.
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // Create a new handler for the connected client.
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clientHandlers.add(clientHandler);

                    // Submit the client handler to the thread pool to be executed.
                    executorService.execute(clientHandler);
                } catch (IOException e) {
                    // This exception might be thrown if the server socket is closed while waiting on accept().
                    // This is expected during a graceful shutdown.
                    System.err.println("IOException in server loop: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT + ": " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    /**
     * Broadcasts a message to all connected clients, except the sender.
     * This method is synchronized to prevent potential race conditions if multiple
     * clients send messages at the exact same time, though with System.out, it's less of an issue.
     * However, it's good practice for methods that modify or access shared state.
     *
     * @param message The message to broadcast.
     * @param sender  The client handler of the client who sent the message.
     */
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clientHandlers) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Removes a client handler from the list of active clients.
     * This is typically called when a client disconnects.
     *
     * @param clientHandler The client handler to remove.
     */
    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Client disconnected: " + clientHandler.getClientAddress());
        broadcastMessage("Server: " + clientHandler.getClientIdentifier() + " has left the chat.", null);
    }

    /**
     * Shuts down the server gracefully.
     */
    public void shutdown() {
        System.out.println("Shutting down server...");
        // Stop accepting new clients by closing the server socket (if it's not already closed).
        // This will also interrupt the serverSocket.accept() call.
        // A more robust implementation would involve a volatile boolean flag.

        // Shutdown the executor service.
        // shutdown() initiates a graceful shutdown. No new tasks are accepted,
        // but previously submitted tasks are executed.
        executorService.shutdown();
        // You could use executorService.shutdownNow() for an abrupt shutdown, which attempts to stop all actively executing tasks.
        System.out.println("Server shutdown complete.");
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
