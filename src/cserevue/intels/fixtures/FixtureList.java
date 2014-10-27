/*
 * Creates a new set of Fixtures and returns them
 */
package cserevue.intels.fixtures;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public interface FixtureList {
    /**
     * Create and return the new list of fixtures, and add them to the root
     * node. 
     * @return Returns the list of created fixtures
     */
    ArrayList<Fixture> createFixtures(Node rootNode, AssetManager assetManager,
            ViewPort viewPort);
}
