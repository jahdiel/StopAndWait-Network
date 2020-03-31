package networks;


public class RunClient {
    
    public static void main(String[] args) {
        Client client = new Client();

        for (int i=0; i < 10; i++) {
            client.sendEcho("Message: " + i);
        }

        client.sendEcho("end");
        client.close();
    }
}