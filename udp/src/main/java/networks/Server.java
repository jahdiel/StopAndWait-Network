package networks;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server extends Thread {

    private StopWaitLayer socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server() {
        try {
            socket = new StopWaitLayer(4445);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
 
    public void run() {
        running = true;
 
        while (running) {
            try {
                StopWaitPacket packet = new StopWaitPacket(buf, buf.length);
                socket.receive(packet);
                // System.out.println("SeqNum: " + packet.getSequenceNumber());
                String received = new String(packet.getData(), 0, packet.getLength());
                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                // System.out.println(received);
                // InetAddress address = packet.getAddress();
                // int port = packet.getPort();
                // packet = new StopWaitPacket(buf, buf.length, address, port);
                
                // socket.sendPacket(packet);
            } catch (Exception e) {
                if (!socket.isClosed()) {
                    System.out.println("Exception thrown "+e.getMessage());
                    socket.close();
                } 
            }
        }
        socket.close();
    }
}