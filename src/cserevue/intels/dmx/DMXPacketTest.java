
package cserevue.intels.dmx;

import java.util.Arrays;

/**
 * Testing DMX packet
 * @author Tim
 */
public class DMXPacketTest extends DMXPacket {
    /**
     * Construct packet with all addresses the same
     * @param value 
     */
    public DMXPacketTest(int sequence, int universe, byte value) {
        super(sequence, universe);
        Arrays.fill(data, value);
    }
}
