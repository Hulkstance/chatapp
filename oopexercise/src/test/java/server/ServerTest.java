package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ServerTest {

    /**
     * Start listening on port 80.
     * It should not throw any exceptions.
     * If the port is in use, the test will fail.
     */
    @Test
    public void testServerStart() {
        assertDoesNotThrow(() -> {
            Server server = new Server(500);
            server.start();
        });
    }

    /**
     * It tests a few ServerHandler methods.
     * If everything works fine, the test should pass.
     */
    @Test
    public void testServerHandler() throws IOException {
        ServerHandler handler = new ServerHandler();

        // Add user
        String username = handler.generateUsername();
        User user = new User.UserBuilder()
                .hasUsername(username)
                .build();
        Socket socket = new Socket();
        ClientInfo clientInfo = new ClientInfo(user, socket, handler);
        handler.addUser(clientInfo);

        // Add another user
        String username2 = handler.generateUsername();
        User user2 = new User.UserBuilder()
                .hasUsername(username2)
                .build();
        Socket socket2 = new Socket();
        ClientInfo clientInfo2 = new ClientInfo(user2, socket2, handler);
        handler.addUser(clientInfo2);

        // Remove second user
        handler.removeUser(handler.getUserClientInfo(username2));

        // Get users
        Vector<ClientInfo> users = handler.getUsers();

        assertEquals(1, users.size());
    }
}
