package client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ClientTest {

    /**
     * Connects to the server.
     * If it cannot establish the connection, the test fails.
     */
    @Test
    public void testServerStart() {
        assertDoesNotThrow(() -> {
            Client client = new Client("localhost", 1200);
            client.connect();
        });
    }
}
