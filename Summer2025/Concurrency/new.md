## Interview recap: Java socket chat app (concurrency)

### What it is
- A TCP, console-based chat system: one `ChatServer` accepts many `ChatClient`s and broadcasts messages to all connected participants.

### How a message flows (end-to-end)
1. A client starts and opens a `Socket` to the server (`localhost:9090`).
2. The client immediately sends an identification line like `IDENTIFY:alice`.
3. The server accepts the connection (`ServerSocket.accept()`), wraps it in a `ClientHandler`, stores the handler, and runs it in a thread pool.
4. The `ClientHandler` reads the first line to set the user’s identifier, then loops on `readLine()` for chat messages.
5. Each message triggers `ChatServer.broadcastMessage(...)`, which iterates over the active handlers and calls `sendMessage(...)` on each recipient.

### Concurrency story (the key design choices)
- Server-side: each client connection is handled concurrently via an `ExecutorService` (a cached thread pool), so one slow client doesn’t block others.
- Shared state: the server tracks connected clients in a `CopyOnWriteArrayList`, so broadcasting can iterate safely even while other threads add/remove clients.
- Client-side: the client uses two threads—main thread reads stdin and sends messages, while a background thread blocks on incoming server messages and prints them.

### Cleanup and disconnect behavior
- When a client disconnects (EOF on socket) or errors, `ClientHandler` triggers `server.removeClient(this)` and closes streams/sockets.
- The server removes the handler from the list and broadcasts a “left the chat” message to remaining clients.

### Tradeoffs / improvement ideas (good interview answers)
- A cached thread pool can grow without bound under heavy load; a fixed pool or NIO/selector approach scales more predictably.
- `CopyOnWriteArrayList` is ideal for many reads and few writes; if joins/leaves are frequent, a `ConcurrentHashMap` (by client id) can reduce copying overhead.
- Broadcasting can invoke `sendMessage` concurrently on the same client from multiple threads; a per-client outbound queue + single writer loop avoids interleaved writes.
