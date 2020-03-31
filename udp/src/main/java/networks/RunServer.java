package networks;


public class RunServer {
    private Client client;


    public static void main(String[] args) {
        new Server().start();
    }

    @Before
    public void setup() throws IOException {
        
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