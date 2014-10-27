package cserevue.intels.fixtures;

import cserevue.intels.dmx.DMXPacket;

/**
 * Basic DMX controlled fixture
 * @author Tim
 */
public abstract class Fixture {
    
    protected final String name;
    protected final int universe;
    protected final int address;
    
    public Fixture(String name, int universe, int address) {
        this.name = name;
        this.universe = universe;
        this.address = address;
    }
    
    /**
     * Return the fixture name
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Return the fixture universe
     * @return 
     */
    public int getUniverse() {
        return universe;
    }
    
    /**
     * Return the fixture address
     * @return 
     */
    public int getAddress() {
        return address;
    }
    
    /**
     * Update the fixture using the dmx packet
     * @param dmx 
     */
    public abstract void dmx_signal(DMXPacket dmx);
}
