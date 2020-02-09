package server;

import java.io.IOException;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles the client.
 * It basically listens for client messages and it executes logic based on the client message/request.
 * It handles /changename, /w and /quit commands.
 */
public class ClientHandler implements Runnable {

    private ClientInfo clientInfo;

    public ClientHandler(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public void run() {
        User currentUser = clientInfo.getUser();

        try {
            while (clientInfo.getClient().isConnected()) {
                try {
                    String input = clientInfo.getReader().readLine();

                    // Pattern matching
                    Pattern pattern = Pattern.compile("^/w\\s+(?<username>\\w+)\\s+(?<message>.+)$");
                    Matcher matcher = pattern.matcher(input);

                    Pattern pattern2 = Pattern.compile("^/changename\\s+(?<username>\\w+)$");
                    Matcher matcher2 = pattern2.matcher(input);

                    // /w username message
                    if (matcher.matches()) {
                        String recipient = matcher.group("username");
                        String message = matcher.group("message");

                        sendPrivateMessage(currentUser.getUsername(), recipient, message);
                        // /changename new-username
                    } else if (matcher2.matches()) {
                        String newUsername = matcher2.group("username");

                        if (clientInfo.getHandler().changeUsername(currentUser.getUsername(), newUsername)) {
                            clientInfo.getHandler().sendMessage(clientInfo.getWriter(), "Successfully changed username to: " + newUsername + ".");
                        } else {
                            clientInfo.getHandler().sendMessage(clientInfo.getWriter(), "Failed to change username because it already exists.");
                        }
                    } else if (input.toLowerCase().startsWith("/quit")) {
                        clientInfo.getHandler().sendMessage(clientInfo.getWriter(), "You have been disconnected from the server.");
                        break;
                    } else {
                        clientInfo.getHandler().sendMessageToAll(currentUser.getUsername(), input);
                    }
                } catch (SocketException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close writer
            clientInfo.getWriter().close();
            try {
                // Close reader
                clientInfo.getReader().close();

                // Close client connection
                clientInfo.getClient().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Remove user
            clientInfo.getHandler().removeUser(clientInfo);

            // Print information
            System.out.println(currentUser.getUsername() + " [" + clientInfo.getClient().getInetAddress().getHostAddress() + "] has left.");
        }
    }

    /**
     * Sends a private message to user.
     *
     * @param sender    the sender.
     * @param recipient the recipient.
     * @param message   the message.
     */
    private void sendPrivateMessage(String sender, String recipient, String message) {
        if (clientInfo.getHandler().usernameExists(recipient)) {
            // Colors
            // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
            String redColor = "\033[0;31m";
            String resetColor = "\033[0m";

            // Find recipient user object
            User recipientUser = clientInfo.getHandler().findByUsername(recipient);

            // Send the private message to the recipient
            clientInfo.getHandler().sendMessage(clientInfo.getHandler().getUserClientInfo(recipient).getWriter(), redColor + "(Private Message) " + resetColor + sender + ": " + message);

            // Notify sender that his message has been sent
            clientInfo.getHandler().sendMessage(clientInfo.getWriter(), "Private message was sent.");
        } else {
            // Notify sender that the recipient user was not found
            clientInfo.getHandler().sendMessage(clientInfo.getWriter(), "The user does not exist.");
        }
    }
}
