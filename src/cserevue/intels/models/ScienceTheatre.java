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

    public final static float DRESS_CIRCLE_Y        = 8.0f;
    public final static float DRESS_CIRCLE_Z        = 25.0f;
    public final static float FRONT_TO_FOHC         = 2.0f;
    public final static float STAGE_FLOOR_HEIGHT    = 1.5f;
    public final static float STAGE_DEPTH           = 8.0f;
    public final static float STAGE_HEIGHT          = 6.0f;
    public final static float STAGE_WIDTH           = 16.0f;
    public final static float WALL_WIDTH            = 0.2f;
    public final static float WINGS_WIDTH           = 2.0f;
    
    private static final String MAT_UNSHADED        = "Common/MatDefs/Misc/Unshaded.j3md";
    private static final String MAT_LIGHTING        = "Common/MatDefs/Light/Lighting.j3md";
    
    public void addModel(Node rootNode, AssetManager assetManager) {
        stage(rootNode, assetManager);
        theatre(rootNode, assetManager);
        curtains(rootNode, assetManager);
    }
    
    // Models for the stage
    private void stage(Node rootNode, AssetManager assetManager) {
        // Common Materials
        Material blackMat = new Material(assetManager, MAT_LIGHTING);
        blackMat.setBoolean("UseMaterialColors",true);
        blackMat.setColor("Ambient", ColorRGBA.Black);
        blackMat.setColor("Diffuse", ColorRGBA.Black);
        
        Material greyMat = new Material(assetManager, MAT_LIGHTING);
        greyMat.setBoolean("UseMaterialColors",true);
        greyMat.setColor("Ambient", ColorRGBA.DarkGray);
        greyMat.setColor("Diffuse", ColorRGBA.DarkGray);
        
        // Stage floor
        Box stageFloorBox = new Box( (STAGE_WIDTH + 2 * WINGS_WIDTH) / 2, STAGE_FLOOR_HEIGHT / 2, STAGE_DEPTH / 2 );
        Geometry stageFloor = new Geometry("StageFloor", stageFloorBox);
        stageFloor.setLocalTranslation(0, stageFloorBox.getYExtent(), 0);
        stageFloor.setMaterial(blackMat);
        rootNode.attachChild(stageFloor);
        
        // Rear wall
        Box rearWallBox = new Box( stageFloorBox.getXExtent(), (STAGE_HEIGHT + STAGE_FLOOR_HEIGHT) / 2, WALL_WIDTH / 2 );
        Geometry rearWall = new Geometry("StageRearWall", rearWallBox);
        rearWall.setLocalTranslation(0, rearWallBox.getYExtent(), -(rearWallBox.getZExtent() + stageFloorBox.getZExtent()) );
        rearWall.setMaterial(greyMat);
        rootNode.attachChild(rearWall);
        
        // Stage Side Walls
        Box sideBox = new Box( WALL_WIDTH / 2, rearWallBox.getYExtent(), stageFloorBox.getZExtent() - FRONT_TO_FOHC / 2 );
        Geometry side1 = new Geometry("SideStageWall1", sideBox);
        Geometry side2 = new Geometry("SideStageWall2", sideBox);
        side1.setLocalTranslation(-(stageFloorBox.getXExtent() + sideBox.getXExtent()), rearWallBox.getYExtent(), -FRONT_TO_FOHC / 2);
        side2.setLocalTranslation(-side1.getLocalTranslation().x, side1.getLocalTranslation().y, side1.getLocalTranslation().z);
        side1.setMaterial(greyMat);
        side2.setMaterial(greyMat);
        rootNode.attachChild(side1);
        rootNode.attachChild(side2);
    }
    
    private void theatre(Node rootNode, AssetManager assetManager) {
        // Materials
        Material floorMat = new Material(assetManager, MAT_LIGHTING); 
        Texture floorTex = assetManager.loadTexture("Textures/carpet01.jpg");
        floorTex.setWrap(Texture.WrapMode.Repeat);
        floorMat.setTexture("DiffuseMap", floorTex);
        
        Material wallMat = new Material(assetManager, MAT_LIGHTING); 
        Texture wallTex = assetManager.loadTexture("Textures/wood01.jpg");
        wallTex.setWrap(Texture.WrapMode.Repeat);
        wallMat.setTexture("DiffuseMap", wallTex);
        
        // Floor
        Box floorBox = new Box( 100, WALL_WIDTH / 2, 100 );
        Geometry floor = new Geometry("TheatreFloor", floorBox);
        floor.setLocalTranslation(0, -floorBox.getYExtent(), 0);
        floor.setMaterial(floorMat);
        floorBox.scaleTextureCoordinates(new Vector2f(100f,100f));
        rootNode.attachChild(floor);
        
        // Front Walls
        // TDOD: Fixup texture co-ordinates - must be done manually - lookup jme3 texture tutorials
        Box shortFrontBox = new Box( (STAGE_WIDTH + 2 * WINGS_WIDTH + 2 * WALL_WIDTH) / 2, STAGE_FLOOR_HEIGHT / 2, WALL_WIDTH );
        Geometry shortFront = new Geometry("TheatreShortFront", shortFrontBox);
        shortFront.setLocalTranslation(0, shortFrontBox.getYExtent(), shortFrontBox.getZExtent() + STAGE_DEPTH / 2);
        shortFront.setMaterial(wallMat);
        shortFrontBox.scaleTextureCoordinates(new Vector2f(50f,50f));
        rootNode.attachChild(shortFront);
    }
    
    private void curtains(Node rootNode, AssetManager assetManager) {
        // Materials
        ColorRGBA fohcColour = new ColorRGBA(0.25f, 0.0f, 0.0f, 1.0f);
        Material fohcMat = new Material(assetManager, MAT_LIGHTING);
        fohcMat.setBoolean("UseMaterialColors", true);
        fohcMat.setColor("Ambient", fohcColour);
        fohcMat.setColor("Diffuse", fohcColour);
        
        Box fohcBox = new Box( (WINGS_WIDTH + WALL_WIDTH) / 2, STAGE_HEIGHT / 2, WALL_WIDTH );
        Geometry fohc1 = new Geometry("FOHC1", fohcBox);
        Geometry fohc2 = new Geometry("FOHC2", fohcBox);
        fohc1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + STAGE_DEPTH / 2 - FRONT_TO_FOHC);
        fohc2.setLocalTranslation(-fohc1.getLocalTranslation().x, fohc1.getLocalTranslation().y, fohc1.getLocalTranslation().z);
        fohc1.setMaterial(fohcMat);
        fohc2.setMaterial(fohcMat);
        rootNode.attachChild(fohc1);
        rootNode.attachChild(fohc2);
    }
}
