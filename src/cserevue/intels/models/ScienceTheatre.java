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

    public final static float CURTAIN_WIDTH         = 0.05f;
    public final static float DRESS_CIRCLE_Y        = 8.0f;
    public final static float DRESS_CIRCLE_Z        = 25.0f;
    public final static float FRONT_TO_FOHC         = 3.0f;
    public final static float FRONT_TO_HALF         = 6.0f;
    public final static float FRONT_TO_QTR          = 4.5f;
    public final static float STAGE_FLOOR_HEIGHT    = 1.5f;
    public final static float STAGE_DEPTH           = 9.0f;
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
        ColorRGBA blackColour = new ColorRGBA(0.1f, 0.1f, 0.1f, 1.0f);
        Material blackMat = new Material(assetManager, MAT_LIGHTING);
        blackMat.setBoolean("UseMaterialColors",true);
        blackMat.setColor("Ambient", blackColour);
        blackMat.setColor("Diffuse", blackColour);
        
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
        Box rearWallBox = new Box( stageFloorBox.getXExtent() + (2 * WALL_WIDTH) / 2, (STAGE_HEIGHT + STAGE_FLOOR_HEIGHT) / 2, WALL_WIDTH / 2 );
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
        ColorRGBA qtrColour = ColorRGBA.Black;
        Material qtrMat = new Material(assetManager, MAT_LIGHTING);
        qtrMat.setBoolean("UseMaterialColors",true);
        qtrMat.setColor("Ambient", qtrColour);
        qtrMat.setColor("Diffuse", qtrColour);
        ColorRGBA cycColour = ColorRGBA.White;
        Material cycMat = new Material(assetManager, MAT_LIGHTING);
        cycMat.setBoolean("UseMaterialColors",true);
        cycMat.setColor("Ambient", cycColour);
        cycMat.setColor("Diffuse", cycColour);
        
        // FOHC
        Box fohcBox = new Box( (WINGS_WIDTH + WALL_WIDTH) / 2, STAGE_HEIGHT / 2, CURTAIN_WIDTH );
        Geometry fohc1 = new Geometry("FOHC1", fohcBox);
        Geometry fohc2 = new Geometry("FOHC2", fohcBox);
        fohc1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + STAGE_DEPTH / 2 - FRONT_TO_FOHC);
        fohc2.setLocalTranslation(-fohc1.getLocalTranslation().x, fohc1.getLocalTranslation().y, fohc1.getLocalTranslation().z);
        fohc1.setMaterial(fohcMat);
        fohc2.setMaterial(fohcMat);
        rootNode.attachChild(fohc1);
        rootNode.attachChild(fohc2);
        
        // QTR
        Geometry qtr1 = new Geometry("QTR1", fohcBox);
        Geometry qtr2 = new Geometry("QTR2", fohcBox);
        qtr1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + STAGE_DEPTH / 2 - FRONT_TO_QTR);
        qtr2.setLocalTranslation(-qtr1.getLocalTranslation().x, qtr1.getLocalTranslation().y, qtr1.getLocalTranslation().z);
        qtr1.setMaterial(qtrMat);
        qtr2.setMaterial(qtrMat);
        rootNode.attachChild(qtr1);
        rootNode.attachChild(qtr2);
        
        // Half
        Geometry half1 = new Geometry("Half1", fohcBox);
        Geometry half2 = new Geometry("Half2", fohcBox);
        half1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + STAGE_DEPTH / 2 - FRONT_TO_HALF);
        half2.setLocalTranslation(-half1.getLocalTranslation().x, half1.getLocalTranslation().y, half1.getLocalTranslation().z);
        half1.setMaterial(qtrMat);
        half2.setMaterial(qtrMat);
        rootNode.attachChild(half1);
        rootNode.attachChild(half2);
        
        // Traveller
        Geometry traveller1 = new Geometry("Traveller1", fohcBox);
        Geometry traveller2 = new Geometry("Traveller2", fohcBox);
        traveller1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() - STAGE_DEPTH / 2);
        traveller2.setLocalTranslation(-traveller1.getLocalTranslation().x, traveller1.getLocalTranslation().y, traveller1.getLocalTranslation().z);
        traveller1.setMaterial(qtrMat);
        traveller2.setMaterial(qtrMat);
        rootNode.attachChild(traveller1);
        rootNode.attachChild(traveller2);
        // Cyc
        Box cycBox = new Box( STAGE_WIDTH / 2, STAGE_HEIGHT / 2, CURTAIN_WIDTH );
        Geometry cyc = new Geometry("Half", cycBox);
        cyc.setLocalTranslation(0, cycBox.getYExtent() + STAGE_FLOOR_HEIGHT, cycBox.getZExtent() - STAGE_DEPTH / 2);
        cyc.setMaterial(cycMat);
        rootNode.attachChild(cyc);
    }
}
