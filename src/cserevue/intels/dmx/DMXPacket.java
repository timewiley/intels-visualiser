package cserevue.intels.dmx;

import cserevue.intels.artnet.ArtNetPacket;
import java.util.Arrays;

/**
 * Representation of a DMX Packet
 * @author Tim
 */
public class DMXPacket {
    public static final int     DMX_ADDRESSES   = 512;
    
    protected int sequence;
    protected int universe;
    protected byte[] data;

    /**
     * Internal constructor for sub-classes to initialise data
     * @param sequence
     * @param universe 
     */
    protected DMXPacket(int sequence, int universe) {
        this.sequence = sequence;
        this.universe = universe;
        this.data = new byte[DMX_ADDRESSES];
    }
    
    public DMXPacket(int sequence, int universe, byte[] data) {
        this.sequence = sequence;
        this.universe = universe;
        this.data = Arrays.copyOf(data, DMX_ADDRESSES);
    }
    
    public DMXPacket(ArtNetPacket packet) {
        this(packet.getSequence(), packet.getUniverse(), packet.getData());
    }

    public int getSequence() {
        return sequence;
    }

    public int getUniverse() {
        return universe;
    }

    /**
     * Get the DMX value at the given address
     * @param address given address
     * @return 
     */
    public byte getValue(int address) {
        return data[address % DMX_ADDRESSES];
    }
    
    /**
     * Get the value at the address as an int [0-255]
     * @param address
     * @return 
     */
    public int getValueInt(int address) {
        return getValue(address) & 0xFF;
    }
    
    /**
     * Get the value at the address as a float [0-1] which is the value / 255
     * @param address
     * @return 
     */
    public float getValueFloat(int address) {
        return getValueInt(address) / 255.0f;
    }
}
