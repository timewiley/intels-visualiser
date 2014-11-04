package cserevue.intels.artnet;

import cserevue.intels.dmx.DMXPacket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Listen to packets over the ArtNet protocol and generate DMX packets
 * @author Tim
 */
public class ArtNet extends Thread {

    public static final String DEFAULT_ADDRESS  = "192.168.0.2";//127.0.0.1";
    public static final int    DEFAULT_PORT     = 6454;
    public static final int    BUFFER_SIZE      = 1024;
    private static final int   SOCKET_TIMEOUT   = (int) 1.0 * 1000;
    
    // Connection details
    private String address;
    private int port;
    
    // Operation
    private boolean operating;
    
    // Listeners
    private ArrayList<ArtNetListener> listeners;
    
    /**
     * New ArtNet connection with default connection details.
     * By Default the server starts disabled
     */
    public ArtNet() {
        address = DEFAULT_ADDRESS;
        port = DEFAULT_PORT;
        operating = false;
        listeners = new ArrayList<ArtNetListener>();
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
        listeners = new ArrayList<ArtNetListener>();
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
     * Add ArtNetListener
     * @param listener 
     */
    public void addListener(ArtNetListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove ArtNetListener
     * @param listener 
     */
    public void removeListener(ArtNetListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Run the server in the current thread. Method does not return
     * until the server is disabled
     */
    @Override
    public void run() {
        // Create a new socket on the address/port combination
        InetAddress iAddress;
        DatagramSocket socket;
        try {
            iAddress = InetAddress.getByName(address);
            socket = new DatagramSocket(port, iAddress);
            socket.setSoTimeout(SOCKET_TIMEOUT);
            
            System.out.println("ArtNet listening on " + address + ":" + port);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }    
        
        // Parse buffer
        byte[] buffer = new byte[BUFFER_SIZE];
        int timeout = 0;
        
        while (operating) {
            // Recieve next packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                
                // Parse
                ArtNetPacket artnetPacket = Parser.parse(packet);
                if (artnetPacket != null) {
                    // Notify listeners
                    DMXPacket dmx = new DMXPacket(artnetPacket);
                    for (ArtNetListener listener : listeners) {
                        listener.receive_artnet(dmx);
                    }

                    // display response
                    //System.out.println("Recieved Packet Opcode: " + artnetPacket.getOpcode());
                    //System.out.println("Data:" + artnetPacket.getData());
                } else {
                    //String recieved = new String(packet.getData(), 0, packet.getLength());
                    //System.out.println("Packet parse failed: " + packet);
                }
            } catch (SocketTimeoutException e) {
                // It's ok, just wait for the next packet
                timeout++;
                System.out.println("Timeout: " + timeout);
            } catch (IOException e) {
                // Fail silently
            }
        }
     
        // Close the connection
        System.out.println("ArtNet closing");
        socket.close();
    }
    
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        ArtNet artnet = new ArtNet();
        artnet.enable();
        artnet.run();
    }
}
