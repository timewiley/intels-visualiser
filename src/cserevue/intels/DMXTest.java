/*
 * 
 * 
 */
package cserevue.intels;

import cserevue.intels.artnet.ArtNet;
import java.io.IOException;

/**
 *
 * @author Tim
 */
public class DMXTest {
    public static void main(String[] args) throws IOException {
        ArtNet artnet = new ArtNet();
        artnet.enable();
        artnet.run();
    }
}
