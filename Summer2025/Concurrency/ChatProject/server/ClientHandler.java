package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ChatServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientIdentifier; // To store a unique identifier for the client

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.clientIdentifier = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort(); // Default identifier
        } catch (IOException e) {
            System.err.println("Error setting up client handler streams: " + e.getMessage());
            closeResources();
        }
    }

    @Override
    public void run() {
        try {
            // First message from client is expected to be their desired identifier (e.g., username)
            String initialMessage = in.readLine();
            if (initialMessage != null && initialMessage.startsWith("IDENTIFY:")) {
                this.clientIdentifier = initialMessage.substring("IDENTIFY:".length()).trim();
                System.out.println("Client identified as: " + clientIdentifier);
                server.broadcastMessage("Server: " + clientIdentifier + " has joined the chat.", this);
            } else {
                // If no identifier is provided, use the default one
                server.broadcastMessage("Server: " + clientIdentifier + " has joined the chat.", this);
            }

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(clientIdentifier + ": " + message);
                server.broadcastMessage(clientIdentifier + ": " + message, this);
            }
        } catch (IOException e) {
            System.err.println("Client " + clientIdentifier + " disconnected unexpectedly: " + e.getMessage());
        } finally {
            server.removeClient(this);
            closeResources();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String getClientAddress() {
        return clientSocket.getInetAddress().getHostAddress();
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client resources: " + e.getMessage());
        }
    }
}
