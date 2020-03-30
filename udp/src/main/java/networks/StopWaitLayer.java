package networks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class StopWaitLayer {

    private CommLayer socket;
    private int sequenceNumber = -1;
    private byte[] buf = new byte[256];
    private int timeout = 1000; // in milliseconds

    public StopWaitLayer() throws SocketException {
        this.socket = new CommLayer();
    }

    public StopWaitLayer(int port) throws SocketException {
        this.socket = new CommLayer(port);
    }

    public StopWaitLayer(SocketAddress bindaddr) throws SocketException {
        this.socket = new CommLayer(bindaddr);
    }

    public StopWaitLayer(int port, InetAddress laddr) throws SocketException {
        this.socket = new CommLayer(port, laddr);
        
    }

    public void send(StopWaitPacket p) throws Exception {
        if (this.sequenceNumber < 0)
            throw new Exception("The sequence number should be defined before sending message.");
        this.socket.setSoTimeout(timeout); // Set timeout for receiver
        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
        DatagramPacket packet = p.getPacket();
        this.addSequenceNumber(this.sequenceNumber, packet);
        boolean sending = true;
        while (sending) {
            socket.sendPacket(packet);
            try {
                socket.receive(receivePacket);
                String received = new String(receivePacket.getData(), 0, receivePacket.getLength()-1);
                System.out.println(received);
                this.sequenceNumber = this.sequenceNumber % 2 == 0 ? 1 : 0; // Switch sequence
                sending = false;
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout!"); // Send again
            }
        }
        this.socket.setSoTimeout(0); // Disable timeout for receiver
    }

    public void receive(StopWaitPacket p) throws IOException {
        socket.receive(p.getPacket());

        // InetAddress address = p.getAddress();
        // int port = p.getPort();
        // DatagramPacket packet = new DatagramPacket(p.getData(), p.length, address, port);
        socket.send(p.getPacket()); // TODO: Change to sendPacket


        // DatagramPacket packet = p.getPacket();
        // System.out.println("New Length: "+ packet.getLength());
        // System.out.println("SeqNum: " + packet.getData()[packet.getLength()-1]);
        // String received = new String(packet.getData(), 0, packet.getLength()-1);
        // System.out.println(received);

        // if (this.sequenceNumber < 0) {
        //     this.sequenceNumber = p.getSequenceNumber() % 2 == 0 ? 1 : 0; // Return the opposite
        // } else {
        //     if (this.sequenceNumber == p.getSequenceNumber()) {
        //         DatagramPacket packet = p.getPacket();
        //         socket.receive(packet);
        //     }
        // }
    }

    public void close() {
        this.socket.close();
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    private void addSequenceNumber(int sequenceNumber, DatagramPacket p) {
        int length = p.getLength();
        byte[] data = p.getData();
        byte[] newData = new byte[length + 1];
        for (int i=0; i < length; i++)
            newData[i] = data[i];
        newData[length] = (byte) sequenceNumber;
        p.setData(newData);
        p.setLength(length + 1);
    }
}