package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;
    private ServerSocket server;

    public Server(int port) {
        this.port = port;
    }

    /**
     * Starts the server.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        // Listen
        this.server = new ServerSocket(port);

        // Print information
        System.out.println("Started listening on port " + port + ".");
    }

    /**
     * Accepts client connections.
     *
     * @throws IOException
     */
    public void accept() throws IOException {
        if (server.isClosed())
            return;

        ServerHandler handler = new ServerHandler();

        // ThreadPools are better performance-wise in our case
        ExecutorService pool = Executors.newFixedThreadPool(500);

        while (true) {
            // Accept client
            Socket client = server.accept();

            // Assign username and add user to the client collection
            String username = handler.generateUsername();
            User user = new User.UserBuilder()
                    .hasUsername(username)
                    .build();
            ClientInfo clientInfo = new ClientInfo(user, client, handler);
            handler.addUser(clientInfo);

            // Print information
            System.out.println(username + " [" + client.getInetAddress().getHostAddress() + "] has connected.");

            // Spawn a new thread
            pool.execute(new ClientHandler(clientInfo));
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(1200);
        server.start();
        server.accept();
    }
}
