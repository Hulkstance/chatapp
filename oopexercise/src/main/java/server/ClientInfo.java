package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientInfo {

    private User user;
    private Socket client;
    private ServerHandler handler;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientInfo(User user, Socket client, ServerHandler handler) throws IOException {
        this.user = user;
        this.client = client;
        this.handler = handler;

        if (client.isConnected()) {
            this.writer = new PrintWriter(client.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
    }

    public User getUser() {
        return user;
    }

    public Socket getClient() {
        return client;
    }

    public ServerHandler getHandler() {
        return handler;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }
}
