package networks;

import java.net.InetAddress;

public class Client {
    private StopWaitLayer socket;
    private InetAddress address;

    private byte[] buf;

    public Client() {
        try {
            socket = new StopWaitLayer();
            socket.setSequenceNumber(0);
            address = InetAddress.getByName("localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEcho(String msg) {
        try {
            buf = msg.getBytes();
            StopWaitPacket packet = new StopWaitPacket(buf, buf.length, address, 4445);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
        
    }
 
    public void close() {
        socket.close();
    }
}