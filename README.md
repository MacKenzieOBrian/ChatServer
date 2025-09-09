# Concurrent Chat Server in Java

This project is a practical demonstration of Java concurrency

## System Architecture

The ChatServer acts as the central director. It listens for incoming client connections through a ServerSocket then hands each connection off to a thread pool managed by an ExecutorService. The server also maintains a thread-safe list of all active clients using a CopyOnWriteArrayList

Each connected client is represented by a ClientHandler. A handler manages all communication for its client by reading messages from the socket and sending responses back dedicating a thread per client ensures one slow or unresponsive client does not affect others. Handlers also notify the server when they disconnect so the client list stays accurate.

The ChatClient connects to the server through a socket and runs in the main thread and sends it to the server while a separate thread continuously listens for broadcast messages. This separation prevents the program from freezing while waiting for incoming messages and keeps the experience smooth 

## Concurrency Design Decisions

Thread management in this project is handled with a cached thread pool created via Executors.newCachedThreadPool(). This pool grows dynamically to handle new clients and reuses idle threads to avoid resource overhead.

The choice to rely on blocking I/O with dedicated threads per client also reflects a conscious design tradeoff. Non-blocking I/O with NIO channels could be more scalable for thousands of clients, but for the purpose of demonstrating thread-based concurrency, blocking streams paired with thread isolation are more straightforward and educational.

## Running the Application

start by cloning the repository:

git clone <repository_url>
cd chat-server


Then compile and run the server:

javac *.java
java ChatServer


The server will start listening for client connections. In separate terminal you can run multiple clients:

java ChatClient


