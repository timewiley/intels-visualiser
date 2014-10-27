package cserevue.intels.artnet;

import cserevue.intels.dmx.DMXPacket;

/**
 * Listener for ArtNet packets
 * @author Tim
 */
public interface ArtNetListener {
    /**
     * Recieve and process the next dmx packet.
     * If a lot of work is requried for processing, it should be done in a sepatate
     * thread after saving the dmx packet
     * @param dmx 
     */
    void receive_artnet(DMXPacket dmx);
}
