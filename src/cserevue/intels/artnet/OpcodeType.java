package cserevue.intels.artnet;

/**
 * Packet Types for the ArtNet Protocol
 * @author Tim
 */
public enum OpcodeType {
    POLL(0x2000),
    POLL_REPLY(0x2100),
    DMX(0x5000),
    UNSUPPORTED(0x0000);
    
    private int opcode;
    
    private OpcodeType() {
        opcode = 0x0000;
    }
    
    /*private OpcodeType(int opcode) {
        // Initialise to Unsupported
        this();
        
        // If the type is located, then set - otherwise leave at unsupported
        OpcodeType[] types = OpcodeType.values();
        for(OpcodeType t : types) {
            if (t.getOpcodeValue() == opcode) {
                this.opcode = opcode;
            }
        }
    }*/
    
    private OpcodeType(int opcode) {
        this.opcode = opcode;
    }

    public static OpcodeType fromInt(int opcode) {
        OpcodeType type = UNSUPPORTED;
        for (OpcodeType t : OpcodeType.values()) {
            if (t.getOpcodeValue() == opcode) {
                type = t;
            }
        }
        return type;
    }
    
    public int getOpcodeValue() {
        return opcode;
    }
    
}
