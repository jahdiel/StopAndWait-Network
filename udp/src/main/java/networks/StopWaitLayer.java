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
        StopWaitPacket receivePacket = new StopWaitPacket(buf, buf.length);
        this.addSequenceNumber(this.sequenceNumber, p.getPacket());
        boolean sending = true;
        while (sending) {
            socket.sendPacket(p.getPacket());
            try {
                boolean receiving = true;
                while (receiving) {
                    socket.receive(receivePacket.getPacket());
                    if ((this.sequenceNumber % 2 == 0 ? 1 : 0) == receivePacket.getSequenceNumber()) {
                        this.sequenceNumber = this.sequenceNumber % 2 == 0 ? 1 : 0; // Switch sequence
                        sending = false;
                        receiving = false;
                    }
                }
            } catch (SocketTimeoutException e) {
                // System.out.println("Timeout!"); // Send again
            }
        }
        this.socket.setSoTimeout(0); // Disable timeout for receiver
    }

    public void receive(StopWaitPacket p) throws IOException {
        boolean receiving = true;
        while (receiving) {
            socket.receive(p.getPacket());
            int newSequenceNumber = p.getSequenceNumber() % 2 == 0 ? 1 : 0; // Return the opposite
            if (this.sequenceNumber < 0) {
                this.sequenceNumber = newSequenceNumber;
                receiving = false;
            } else if (this.sequenceNumber == p.getSequenceNumber()) {
                this.sequenceNumber = this.sequenceNumber % 2 == 0 ? 1 : 0;
                receiving = false;
            }
            InetAddress address = p.getAddress();
            int port = p.getPort();
            StopWaitPacket ack = new StopWaitPacket(this.buf, this.buf.length, address, port);
            ack.setSequenceNumber(newSequenceNumber);
            socket.sendPacket(ack.getPacket());
        }
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