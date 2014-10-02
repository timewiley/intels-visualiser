package cserevue.intels.artnet;

import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Parse an ArtNet Packet
 * @author Tim
 */
public class Parser {
    
    private static final byte[] HEADER               = "Art-Net\0".getBytes();
    private static final int    PACKET_MIN_LENGTH    = 12;
    private static final int    PROTOCOL_VERSION     = 14;
    
    // Byte offsets
    private static final int    DATA_OFFSET          = 18;
    private static final int    HEADER_OFFSET        = 0;
    private static final int    LENGTH_OFFSET        = 16;
    private static final int    OPCODE_OFFSET        = 8;
    private static final int    PROTOCOL_OFFSET      = 10;
    private static final int    SEQUENCE_OFFSET      = 12;
    private static final int    UNIVERSE_OFFSET      = 14;
    
    public static ArtNetPacket parse(DatagramPacket datagram) {
        // ArtNet Packet
        ArtNetPacket packet = null;
        
        // Get packet data
        byte[] datagramData = datagram.getData();
        //System.out.println("data: " + new String(datagram.getData(), 0, datagram.getLength() ));
        //System.out.println("datagram length: " + datagram.getLength());
        
        if (isValidPacket(datagram)) {
            // Get Opcode
            int opcode = int16LEFromBytes(datagramData, OPCODE_OFFSET);
            OpcodeType opcodeType = OpcodeType.fromInt(opcode);
            //System.out.println("Got packet opcode: " + opcode);
            
            // Get Sequece id
            int sequence = 0;
            
            // Get Universe
            int universe = 0;
            
            // Get Data
            byte[] data = new byte[0];
            if (datagramData.length > LENGTH_OFFSET) {
                int length = int16FromBytes(datagramData, LENGTH_OFFSET);
                data = Arrays.copyOfRange(datagramData, DATA_OFFSET, DATA_OFFSET + length);
            }
            
            // Create ArtNetPacket object
            packet = new ArtNetPacket(opcodeType, sequence, universe, data);
        }
        
        return packet;
    }
    
    /**
     * Check minimum requirements for an artnet packet
     */
    private static boolean isValidPacket(DatagramPacket packet) {
        boolean valid = true;
        byte[] data = packet.getData();
        
        // Check
        //  - packet length
        //  - packet header
        //  - ArtNet protocol version
        if (packet.getLength() < PACKET_MIN_LENGTH) {
            valid = false;
            System.out.println("Bad Length: " + packet.getLength());
        } else if (!matchBytes(data, HEADER, HEADER_OFFSET)) {
            valid = false;
            System.out.println("Bad Header");
        } else if (int16FromBytes(data, PROTOCOL_OFFSET) != PROTOCOL_VERSION) {
            valid = false;
            System.out.println("Bad protocol: " + int16FromBytes(data, PROTOCOL_OFFSET));
        }
        
        return valid;
    }
    
    /**
     * Compare the array of bytes in 'match' to the array of bytes in 'data'
     * beginning at index 'offset' to evaluate if the sequence of bytes match.
     * @param data Data to find bytes in
     * @param match Bytes to match to
     * @param offset Offset of starting sequence of bytes in 'data'
     * @return True if the bytes match
     */
    private static boolean matchBytes(byte[] data, byte[] match, int offset) {
        boolean matched = true;
        
        // Check for enough bytes in data
        if (data.length < match.length + offset) {
            matched = false;
        } else {
            for (int i = 0; i != match.length; ++i) {
                if (data[i + offset] != match[i]) {
                    matched = false;
                }
            }
        }
        
        return matched;
    }
    
    /**
     * Load 8-bit int from the byte 'data', beginning at 'offset'
     * @param data
     * @param offset
     * @return 
     */
    private static int int8FromBytes(byte data[], int offset) {
        return (int) data[offset];
    }
    
    /**
     * Load 16-bit int from the byte 'data', beginning at 'offset'
     * @param data
     * @param offset
     * @return 
     */
    private static int int16FromBytes(byte data[], int offset) {
        return (((int) data[offset] << 8) + ((int) data[offset + 1]));
    }
    
    /**
     * Load 16-bit int from the byte 'data', beginning at 'offset', using
     * Little Endien notation
     * @param data
     * @param offset
     * @return 
     */
    private static int int16LEFromBytes(byte data[], int offset) {
        return (((int) data[offset+1] << 8) + ((int) data[offset]));
    }
}
