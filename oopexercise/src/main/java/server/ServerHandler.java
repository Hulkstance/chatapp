package server;

import java.io.PrintWriter;
import java.util.Vector;

public class ServerHandler {

    // Thread-safe
    private Vector<ClientInfo> clients;

    public ServerHandler() {
        this.clients = new Vector<>();
    }

    /**
     * Adds a new user.
     *
     * @param clientInfo the user information.
     */
    public void addUser(ClientInfo clientInfo) {
        clients.add(clientInfo);
    }

    /**
     * Removes user.
     *
     * @param clientInfo the user information.
     */
    public void removeUser(ClientInfo clientInfo) {
        clients.remove(clientInfo);
    }

    /**
     * Gets user client info.
     *
     * @param username the username.
     * @return
     */
    public ClientInfo getUserClientInfo(String username) {
        for (ClientInfo clientInfo : clients) {
            if (clientInfo.getUser().getUsername().equals(username))
                return clientInfo;
        }
        return null;
    }

    /**
     * Checks whether the username exists or not.
     *
     * @param username the username.
     * @return
     */
    public boolean usernameExists(String username) {
        for (ClientInfo clientInfo : clients) {
            if (clientInfo.getUser().getUsername().equals(username))
                return true;
        }
        return false;
    }

    /**
     * Generates a random username.
     *
     * @return the randomized username.
     */
    public String generateUsername() {
        String username;
        while (true) {
            username = Util.getInstance().getRandomString(10);

            if (!usernameExists(username))
                break;
        }
        return username;
    }

    /**
     * Returns all users.
     *
     * @return the users.
     */
    public Vector<ClientInfo> getUsers() {
        return clients;
    }

    /**
     * Finds specific user.
     *
     * @param username the username.
     * @return the user object.
     */
    public User findByUsername(String username) {
        for (ClientInfo clientInfo : clients) {
            User user = clientInfo.getUser();
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    /**
     * Changes username.
     *
     * @param oldUsername the old username.
     * @param newUsername the new username.
     * @return
     */
    public boolean changeUsername(String oldUsername, String newUsername) {
        if (!usernameExists(newUsername)) {
            User user = findByUsername(oldUsername);
            if (user != null) {
                user.setUsername(newUsername);
                return true;
            }
        }
        return false;
    }

    /**
     * Sends a message.
     *
     * @param writer  the writer buffer.
     * @param message the message.
     */
    public void sendMessage(PrintWriter writer, String message) {
        writer.println(message);
    }

    /**
     * Sends message to everyone.
     *
     * @param from    the sender.
     * @param message the message.
     */
    public void sendMessageToAll(String from, String message) {
        for (ClientInfo clientInfo : clients) {
            sendMessage(clientInfo.getWriter(), from + ": " + message);
        }
    }
}
