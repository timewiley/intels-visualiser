package cserevue.intels.dmx;

import cserevue.intels.artnet.ArtNetPacket;
import java.util.Arrays;

/**
 * Representation of a DMX Packet
 * @author Tim
 */
public class DMXPacket {
    public static final int     DMX_ADDRESSES   = 512;
    
    private int sequence;
    private int universe;
    private byte[] data;

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
    public int getValue(int address) {
        return data[address % DMX_ADDRESSES];
    }
}
