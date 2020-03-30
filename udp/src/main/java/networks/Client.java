package networks;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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

    // public String sendEcho(String msg) {
    //     String received = null;
    //     try {
    //         buf = msg.getBytes();
    //         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
    //         socket.sendPacket(packet);
    //         packet = new DatagramPacket(buf, buf.length);
    //         socket.receivePacket(packet);
    //         received = new String(
    //         packet.getData(), 0, packet.getLength());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return received; 
    // }
}