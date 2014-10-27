package cserevue.intels.artnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Listen to packets over the ArtNet protocol and generate DMX packets
 * @author Tim
 */
public class ArtNet {

    public static final String DEFAULT_ADDRESS  = "127.0.0.1";
    public static final int    DEFAULT_PORT     = 6454;
    public static final int    BUFFER_SIZE      = 1024;
    
    // Connection details
    private String address;
    private int port;
    
    // Operation
    private boolean operating;
    
    /**
     * New ArtNet connection with default connection details.
     * By Default the server starts disabled
     */
    public ArtNet() {
        address = DEFAULT_ADDRESS;
        port = DEFAULT_PORT;
        operating = false;
    }
    
    /**
     * New ArtNet connection with custom connection.
     * By Default the server starts disabled
     * @param address IP Address
     * @param port Port number
     */
    public ArtNet(String address, int port) {
        this.address = address;
        this.port = port;
        operating = false;
    }
    
    /**
     * Enable the server - does not cause the server to run
     */
    public void enable() {
        operating = true;
    }
    
    /**
     * Disable the server. If the server is running, it will stop.
     */
    public void disable() {
        operating = false;
    }
    
    /**
     * Run the server in the current thread. Method does not return
     * until the server is disabled
     */
    public void run() throws IOException {
        // Create a new socket on the address/port combination
        InetAddress iAddress = InetAddress.getByName(address);
        DatagramSocket socket = new DatagramSocket(port, iAddress);
        
        // Parse buffer
        byte[] buffer = new byte[BUFFER_SIZE];
        
        
        while (operating) {
            // Recieve next packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            
            // Parse
            ArtNetPacket artnetPacket = Parser.parse(packet);
            if (artnetPacket != null) {
                // display response
                System.out.println("Recieved Packet Opcode: " + artnetPacket.getOpcode());
                System.out.println("Data:" + artnetPacket.getData());
            } else {
                String recieved = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Packet parse failed: " + recieved);
            }

            
        }
     
        // Close the connection
        socket.close();
    }
    
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        ArtNet artnet = new ArtNet();
        artnet.enable();
        artnet.run();
    }
}
