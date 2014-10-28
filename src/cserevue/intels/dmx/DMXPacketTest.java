
package cserevue.intels.dmx;

import com.jme3.math.ColorRGBA;
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
    
    /**
     * Fill the colour at the start address the given number of iterations
     * @param colour
     * @param addr 
     * @param iterations 
     */
    public void fill(ColorRGBA colour, int addr, int iterations) {
        fill(colour, addr, iterations, 3);
    }
    
    /**
     * Fill the colour at the start address the given number of iterations,
     * given the number of addresses for the fixture
     * @param colour
     * @param addr 
     * @param iterations 
     */
    public void fill(ColorRGBA colour, int addr, int iterations, int fixtureAddrs) {
        for(int i = 0; i != iterations; ++i) {
            int offset = fixtureAddrs * i;
            data[addr + offset] = floatToByte(colour.r);
            data[addr + offset + 1] = floatToByte(colour.g);
            data[addr + offset + 2] = floatToByte(colour.b);
        }
    }
}
