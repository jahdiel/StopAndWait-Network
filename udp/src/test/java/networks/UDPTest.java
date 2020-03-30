package networks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class UDPTest {
    private Client client;

    @Before
    public void setup() throws IOException {
        new Server().start();
        client = new Client();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect1() {
        for (int i=0; i < 10; i++) {
            client.sendEcho("Message: " + i);
        }
    }

    @After
    public void tearDown() {
        stopEchoServer();
        client.close();
    }

    private void stopEchoServer() {
        client.sendEcho("end");
    }
}