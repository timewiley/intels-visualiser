/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cserevue.intels.fixtures;

import com.jme3.math.ColorRGBA;
import cserevue.intels.dmx.DMXPacket;

/**
 *
 * @author Tim
 */
public abstract class RGBFixture extends Fixture {
    
    protected ColorRGBA colour;
    
    public RGBFixture(String id, int universe, int address) {
        super(id, universe, address);
        colour = new ColorRGBA(ColorRGBA.Black);
    }
    
    public void dmx_signal(DMXPacket dmx) {
        if (dmx.getUniverse() == universe) {
            colour.r = dmx.getValueFloat(address);
            colour.b = dmx.getValueFloat(address + 1);
            colour.g = dmx.getValueFloat(address + 2);
        }
    }
}
