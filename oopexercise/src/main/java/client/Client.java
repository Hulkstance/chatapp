package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * This class represents the clients that connect to the server.
 * It has two threads: the first one is supposed to read all messages from the server
 * and the second one is supposed to send messages to the server (using keyboard input).
 */
public class Client {

    private String host;
    private int port;
    private Socket client;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connects to the server.
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        // Connect to the server
        this.client = new Socket(host, port);

        // Print information
        System.out.println("Connected to the server. Type /quit to close the connection.");
    }

    /**
     * Closes the connection to the server.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        client.close();
    }

    /**
     * Sends messages to the server (keyboard input).
     *
     * @throws IOException
     */
    private void sendMessages() throws IOException {
        if (client.isClosed())
            return;

        try (PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {
            Thread sendMessages = new Thread(() -> {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    String message = scanner.nextLine();

                    writer.println(message);
                }
            });

            // Start thread
            sendMessages.start();
        }
    }

    /**
     * Reads messages from the server.
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private void readMessages() throws InterruptedException, IOException {
        if (client.isClosed())
            return;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            Thread readMessages = new Thread(() -> {
                while (true) {
                    try {
                        String input = reader.readLine();

                        // Ignore empty messages
                        if (input == null || input.trim().isEmpty())
                            continue;

                        System.out.println(input);
                    } catch (SocketException e) {
                        System.out.println("Server has stopped unexpectedly.");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            });

            // Start thread
            readMessages.start();

            // Wait for the thread to finish
            readMessages.join();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client("localhost", 1200);

        // Connect to the server
        client.connect();

        // Send message listener
        client.sendMessages();

        // Read message listener
        client.readMessages();

        // Close connection
        client.close();
    }
}
