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
    
    // DMX Controls
    protected ColorRGBA dmxColour;
    protected boolean newDMXPacket;
    
    public RGBFixture(String id, int universe, int address) {
        super(id, universe, address);
        dmxColour = new ColorRGBA(ColorRGBA.Black);
        newDMXPacket = false;
    }
    
    public void dmx_signal(DMXPacket dmx) {
        if (dmx.getUniverse() == universe) {
            //System.out.println("dmx_signal: " + dmx.getUniverse());
            synchronized(this) {
                dmxColour.r = dmx.getValueFloat(address);
                dmxColour.g = dmx.getValueFloat(address + 1);
                dmxColour.b = dmx.getValueFloat(address + 2);
                newDMXPacket = true;
            }
        } else {
            //System.out.println("Incorrect Universe: " + dmx.getUniverse());
        }
    }
}
