package networks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Random;

public class CommLayer extends DatagramSocket {
    
    public CommLayer() throws SocketException {
        super();
    }

    public CommLayer(int port) throws SocketException {
        super(port);
    }

    public CommLayer(SocketAddress bindaddr) throws SocketException {
        super(bindaddr);
    }

    public CommLayer(int port, InetAddress laddr) throws SocketException {
        super(port, laddr);
    }

    public void sendPacket(DatagramPacket p) throws IOException {
        boolean lost = new Random().nextInt(5)==0; 
        boolean duplicate = new Random().nextInt(10)==0;
        if (lost)
            System.out.println("Lost");
        if (!lost) { // Discards a packet with 20% probability
            if(duplicate) { // Sends a duplicated packet with 10% probability 
                System.out.println("Duplicated"); 
                this.send(p);
                this.send(p);
            }else{
                // System.out.println("Sent");
                this.send(p);
            }
        } 
        return;
    }

    public void receivePacket(DatagramPacket p) throws IOException {
        this.receive(p);
        return;
    }
}