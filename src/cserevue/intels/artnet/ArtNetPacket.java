package cserevue.intels.artnet;

/**
 * Basic parsed ArtNet Packet
 * @author Tim
 */
public class ArtNetPacket {
    private OpcodeType opcode;
    private int sequence;
    private int universe;
    private byte[] data;

    public ArtNetPacket(OpcodeType opcode, int sequence, int universe, byte[] data) {
        this.opcode = opcode;
        this.sequence = sequence;
        this.universe = universe;
        this.data = data;
    }

    public OpcodeType getOpcode() {
        return opcode;
    }

    public int getSequence() {
        return sequence;
    }

    public int getUniverse() {
        return universe;
    }

    public byte[] getData() {
        return data;
    }
}
