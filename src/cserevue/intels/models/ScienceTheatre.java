package cserevue.intels.models;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * Model of the Science Theatre
 * 
 * @author Timothy Wiley <timothyw@cse.unsw.edu.au>
 */
public class ScienceTheatre implements Model {

    public final static float DRESS_CIRCLE_Y        = 5.0f;
    public final static float DRESS_CIRCLE_Z        = 18.0f;
    public final static float STAGE_FLOOR_HEIGHT    = 1.5f;
    public final static float STAGE_DEPTH           = 6.0f;
    public final static float STAGE_WIDTH           = 15.0f;
    
    private static final String MAT_UNSHADED        = "Common/MatDefs/Misc/Unshaded.j3md";
    private static final String MAT_LIGHTING        = "Common/MatDefs/Light/Lighting.j3md";
    
    public void addModel(Node rootNode, AssetManager assetManager) {
        // Common Materials
        Material black = new Material(assetManager, MAT_LIGHTING);
        black.setBoolean("UseMaterialColors",true);
        black.setColor("Ambient", ColorRGBA.Black);
        black.setColor("Diffuse", ColorRGBA.Black);
        
        // Stage floor
        Geometry stageFloor = new Geometry("StageFloor",
                new Box(new Vector3f(0, -STAGE_FLOOR_HEIGHT, 0), 
                    STAGE_WIDTH / 2, STAGE_FLOOR_HEIGHT / 2, STAGE_DEPTH / 2) );
        stageFloor.setMaterial(black);
        rootNode.attachChild(stageFloor);
        
        // Theatre surrounds
        // Floor
        float floorHeight = 0.1f;
        Box floorBox = new Box(new Vector3f(0, -(STAGE_FLOOR_HEIGHT+floorHeight), 0), 
                    100, floorHeight / 2, 100);
        Geometry floor = new Geometry("Floor", floorBox);
        Material floorMat = new Material(assetManager, MAT_LIGHTING); 
        Texture floorTex = assetManager.loadTexture("Textures/carpet01.jpg"); 
        floorTex.setWrap(Texture.WrapMode.Repeat);
        //floorMat.setTexture("AmbientMap", floorTex); 
        floorMat.setTexture("DiffuseMap", floorTex); 
        floor.setMaterial(floorMat);
        floorBox.scaleTextureCoordinates(new Vector2f(100f,100f));
        rootNode.attachChild(floor);
    }
}
