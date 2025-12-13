## Explanation: How the concurrency chat app works

This document explains the implementation at a “talk me through your code” level, focusing on concurrency and flow.

### Components
- `ChatServer` (server): accepts connections, owns the shared list of clients, and broadcasts messages.
- `ClientHandler` (server): one runnable per connected client; reads that client’s messages and asks the server to broadcast.
- `ChatClient` (client): console UI; main thread sends messages, background thread listens for incoming broadcasts.

### Lifecycle / message flow
1. Client connects to `localhost:9090` over TCP.
2. Client sends `IDENTIFY:<username>` as the first line so the server can label messages.
3. Server accepts the socket and submits a `ClientHandler` to an `ExecutorService`.
4. `ClientHandler` reads lines from its socket; each line becomes a broadcast to other clients.
5. On disconnect, the handler closes resources and asks the server to remove it from the active list.

### Concurrency choices (why this is “safe enough” for a class project)
- The server uses a thread pool so multiple clients can block on socket I/O at the same time without blocking the accept loop.
- The list of connected clients is a `CopyOnWriteArrayList`, which is safe to iterate during broadcasts even if other threads add/remove clients.
- The client uses two threads to avoid blocking the UI: stdin handling and socket listening happen independently.

### Known tradeoffs (good interview discussion)
- A cached thread pool can create many threads under load; a fixed pool or NIO approach can control resource usage.
- `CopyOnWriteArrayList` copies on every write; it’s best when broadcasts are frequent and connects/disconnects are relatively infrequent.
- Broadcasting may call `sendMessage` concurrently for a given client; production code typically serializes outbound writes per client.
