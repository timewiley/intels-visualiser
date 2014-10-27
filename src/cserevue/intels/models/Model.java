package cserevue.intels.models;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 * Interface for defining and adding models/renders to the display
 * @author Timothy Wiley <timothyw@cse.unsw.edu.au>
 */
public interface Model {
    public static final String MAT_UNSHADED        = "Common/MatDefs/Misc/Unshaded.j3md";
    public static final String MAT_LIGHTING        = "Common/MatDefs/Light/Lighting.j3md";
    
    /**
     * Add model to the render window
     */
    public void addModel(Node rootNode, AssetManager assetManager);
}
