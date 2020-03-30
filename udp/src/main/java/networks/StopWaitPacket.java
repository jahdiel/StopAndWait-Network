
package networks;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class StopWaitPacket {

    private DatagramPacket packet;

    public StopWaitPacket(byte[] buf, int length) {
        this.packet = new DatagramPacket(buf, length);
    }

    public StopWaitPacket(byte[] buf, int offset, int length) {
        this.packet = new DatagramPacket(buf, offset, length);
    }
    
    public StopWaitPacket(byte[] buf, int offset, int length, InetAddress address, int port) {
        this.packet = new DatagramPacket(buf, offset, length, address, port);
    }

    public StopWaitPacket(byte[] buf, int offset, int length, SocketAddress address) throws SocketException {
        this.packet = new DatagramPacket(buf, offset, length, address);
    }

    public StopWaitPacket(byte[] buf, int length, InetAddress address, int port) {
        this.packet = new DatagramPacket(buf, length, address, port);
    }

    public StopWaitPacket(byte[] buf, int length, SocketAddress address) throws SocketException {
        this.packet = new DatagramPacket(buf, length, address);
    }


    public byte getSequenceNumber() {
        return this.packet.getData()[this.packet.getLength()-1];
    }

    public void setSequenceNumber(int setSequenceNumber) {
        this.packet.getData()[this.packet.getLength()-1] = (byte) setSequenceNumber;
    }

    public DatagramPacket getPacket() {
        return this.packet;
    }
    
    public InetAddress getAddress() {
        return this.packet.getAddress();
    }

    public int getPort() {
        return this.packet.getPort();
    }

    public byte[] getData() {
        return this.packet.getData();
    }

    public int getLength() {
        return this.packet.getLength() - 1;
    }

}