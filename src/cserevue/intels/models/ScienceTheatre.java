package cserevue.intels.models;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import cserevue.intels.dmx.DMXPacketTest;
import cserevue.intels.fixtures.Cyc;
import cserevue.intels.fixtures.Fixture;
import cserevue.intels.fixtures.FixtureList;
import cserevue.intels.fixtures.Par64;
import java.util.ArrayList;

/**
 *
 * Model of the Science Theatre
 * 
 * @author Timothy Wiley <timothyw@cse.unsw.edu.au>
 */
public class ScienceTheatre implements Model, FixtureList {

    // Model stage dimensions
    public final static float CURTAIN_WIDTH         = 0.05f;
    public final static float DRESS_CIRCLE_Y        = 8.0f;
    public final static float DRESS_CIRCLE_Z        = 25.0f;
    public final static float FRONT_TO_FOHC         = 2.0f;
    public final static float FRONT_TO_HALF         = 5.0f;
    public final static float FRONT_TO_QTR          = 3.5f;
    public final static float STAGE_FLOOR_HEIGHT    = 1.5f;
    public final static float STAGE_DEPTH           = 9.0f;
    public final static float STAGE_HEIGHT          = 6.0f;
    public final static float STAGE_WIDTH           = 16.0f;
    public final static float WALL_WIDTH            = 0.2f;
    public final static float WINGS_WIDTH           = 2.0f;
    
    // Light dimensions
    public final static float PAR_Y_1               = STAGE_FLOOR_HEIGHT + 4.5f;
    public final static float PAR_Y_2               = STAGE_FLOOR_HEIGHT + 3.75f;
    public final static float PAR_Y_3               = STAGE_FLOOR_HEIGHT + 3.0f;
    public final static float PAR_ROATION_1         = 75f * FastMath.DEG_TO_RAD;
    public final static float PAR_ROATION_2         = 60f * FastMath.DEG_TO_RAD;
    public final static float PAR_ROATION_3         = 45f * FastMath.DEG_TO_RAD;
    
    // Addresses
    public final static int PAR_DMX_START           = 337;
    public final static int PAR_DMX_NADDRS          = 5;
    
    public void addModel(Node rootNode, AssetManager assetManager) {
        stage(rootNode, assetManager);
        theatre(rootNode, assetManager);
        curtains(rootNode, assetManager);
    }
    
    /**
     * Z-position of the half curtain
     * @return 
     */
    public static float z_fohc() {
        return STAGE_DEPTH / 2 - FRONT_TO_FOHC;
    }
    
    /**
     * Z-position of the half curtain
     * @return 
     */
    public static float z_qtr() {
        return STAGE_DEPTH / 2 - FRONT_TO_QTR;
    }
    
    /**
     * Z-position of the half curtain
     * @return 
     */
    public static float z_half() {
        return STAGE_DEPTH / 2 - FRONT_TO_HALF;
    }
    
    // Models for the stage
    private void stage(Node rootNode, AssetManager assetManager) {
        // Common Materials
        ColorRGBA blackColour = new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f);
        Material blackMat = new Material(assetManager, MAT_LIGHTING);
        blackMat.setBoolean("UseMaterialColors",true);
        blackMat.setColor("Ambient", blackColour);
        blackMat.setColor("Diffuse", blackColour);
        //blackMat.setColor("Diffuse", ColorRGBA.White);
        
        Material greyMat = new Material(assetManager, MAT_LIGHTING);
        greyMat.setBoolean("UseMaterialColors",true);
        greyMat.setColor("Ambient", ColorRGBA.DarkGray);
        greyMat.setColor("Diffuse", ColorRGBA.DarkGray);
        
        // Stage Node
        Node stageNode = new Node("Stage");
        stageNode.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(stageNode);
        
        // Stage floor
        Box stageFloorBox = new Box( (STAGE_WIDTH + 2 * WINGS_WIDTH) / 2, STAGE_FLOOR_HEIGHT / 2, STAGE_DEPTH / 2 );
        Geometry stageFloor = new Geometry("StageFloor", stageFloorBox);
        stageFloor.setLocalTranslation(0, stageFloorBox.getYExtent(), 0);
        stageFloor.setMaterial(blackMat);
        stageFloor.setShadowMode(RenderQueue.ShadowMode.Receive);
        stageNode.attachChild(stageFloor);
        
        // Rear wall
        Box rearWallBox = new Box( stageFloorBox.getXExtent() + (2 * WALL_WIDTH) / 2, (STAGE_HEIGHT + STAGE_FLOOR_HEIGHT) / 2, WALL_WIDTH / 2 );
        Geometry rearWall = new Geometry("StageRearWall", rearWallBox);
        rearWall.setLocalTranslation(0, rearWallBox.getYExtent(), -(rearWallBox.getZExtent() + stageFloorBox.getZExtent()) );
        rearWall.setMaterial(greyMat);
        stageNode.attachChild(rearWall);
        
        // Stage Side Walls
        Box sideBox = new Box( WALL_WIDTH / 2, rearWallBox.getYExtent(), stageFloorBox.getZExtent() - FRONT_TO_FOHC / 2 );
        Geometry side1 = new Geometry("SideStageWall1", sideBox);
        Geometry side2 = new Geometry("SideStageWall2", sideBox);
        side1.setLocalTranslation(-(stageFloorBox.getXExtent() + sideBox.getXExtent()), rearWallBox.getYExtent(), -FRONT_TO_FOHC / 2);
        side2.setLocalTranslation(-side1.getLocalTranslation().x, side1.getLocalTranslation().y, side1.getLocalTranslation().z);
        side1.setMaterial(greyMat);
        side2.setMaterial(greyMat);
        stageNode.attachChild(side1);
        stageNode.attachChild(side2);
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
        
        // Theatre Node
        Node theatreNode = new Node("Theatre");
        rootNode.attachChild(theatreNode);
        
        // Floor
        Box floorBox = new Box( 100, WALL_WIDTH / 2, 100 );
        Geometry floor = new Geometry("TheatreFloor", floorBox);
        floor.setLocalTranslation(0, -floorBox.getYExtent(), 0);
        floor.setMaterial(floorMat);
        floorBox.scaleTextureCoordinates(new Vector2f(100f,100f));
        theatreNode.attachChild(floor);
        
        // Front Walls
        // TDOD: Fixup texture co-ordinates - must be done manually - lookup jme3 texture tutorials
        Box shortFrontBox = new Box( (STAGE_WIDTH + 2 * WINGS_WIDTH + 2 * WALL_WIDTH) / 2, STAGE_FLOOR_HEIGHT / 2, WALL_WIDTH );
        Geometry shortFront = new Geometry("TheatreShortFront", shortFrontBox);
        shortFrontBox.scaleTextureCoordinates(new Vector2f(50f,50f));
        shortFront.setLocalTranslation(0, shortFrontBox.getYExtent(), shortFrontBox.getZExtent() + STAGE_DEPTH / 2);
        shortFront.setMaterial(wallMat);
        shortFront.setShadowMode(RenderQueue.ShadowMode.Receive);
        theatreNode.attachChild(shortFront);
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
        cycMat.setColor("Ambient", new ColorRGBA(1.0f, 1.0f, 1.0f, 0.2f));
        cycMat.setColor("Diffuse", cycColour);
        
        // FOHC
        Box fohcBox = new Box( (WINGS_WIDTH + WALL_WIDTH) / 2, STAGE_HEIGHT / 2, CURTAIN_WIDTH );
        Geometry fohc1 = new Geometry("FOHC1", fohcBox);
        Geometry fohc2 = new Geometry("FOHC2", fohcBox);
        Node fohcs = new Node("FOHC");
        fohcs.setLocalTranslation(0, STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + z_fohc());
        fohcs.attachChild(fohc1);
        fohcs.attachChild(fohc2);
        fohc1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), 0, 0);
        fohc2.setLocalTranslation(-fohc1.getLocalTranslation().x, 0, 0);
        fohc1.setMaterial(fohcMat);
        fohc2.setMaterial(fohcMat);
        fohcs.setShadowMode(RenderQueue.ShadowMode.Cast);
        rootNode.attachChild(fohcs);
        
        // QTR
        Geometry qtr1 = new Geometry("QTR1", fohcBox);
        Geometry qtr2 = new Geometry("QTR2", fohcBox);
        Node qtrs = new Node("QTR");
        qtrs.setLocalTranslation(0, STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + z_qtr());
        qtrs.attachChild(qtr1);
        qtrs.attachChild(qtr2);
        qtr1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), 0, 0);
        qtr2.setLocalTranslation(-qtr1.getLocalTranslation().x, 0, 0);
        qtr1.setMaterial(qtrMat);
        qtr2.setMaterial(qtrMat);
        qtrs.setShadowMode(RenderQueue.ShadowMode.Cast);
        rootNode.attachChild(qtrs);
        
        // Half
        Geometry half1 = new Geometry("Half1", fohcBox);
        Geometry half2 = new Geometry("Half2", fohcBox);
        Node halfs = new Node("Half");
        halfs.setLocalTranslation(0, STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() + z_half());
        halfs.attachChild(half1);
        halfs.attachChild(half2);
        half1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), 0, 0);
        half2.setLocalTranslation(-half1.getLocalTranslation().x, 0, 0);
        half1.setMaterial(qtrMat);
        half2.setMaterial(qtrMat);
        halfs.setShadowMode(RenderQueue.ShadowMode.Cast);
        rootNode.attachChild(halfs);
        
        // Traveller
        Geometry traveller1 = new Geometry("Traveller1", fohcBox);
        Geometry traveller2 = new Geometry("Traveller2", fohcBox);
        traveller1.setLocalTranslation(-(fohcBox.getXExtent() + STAGE_WIDTH / 2), STAGE_FLOOR_HEIGHT + fohcBox.getYExtent(), fohcBox.getZExtent() - STAGE_DEPTH / 2);
        traveller2.setLocalTranslation(-traveller1.getLocalTranslation().x, traveller1.getLocalTranslation().y, traveller1.getLocalTranslation().z);
        traveller1.setMaterial(qtrMat);
        traveller2.setMaterial(qtrMat);
        rootNode.attachChild(traveller1);
        rootNode.attachChild(traveller2);
        
        // Cyc - now a fixture
        //Box cycBox = new Box( STAGE_WIDTH / 2, STAGE_HEIGHT / 2, CURTAIN_WIDTH );
        //Geometry cyc = new Geometry("Half", cycBox);
        //cyc.setLocalTranslation(0, cycBox.getYExtent() + STAGE_FLOOR_HEIGHT, cycBox.getZExtent() - STAGE_DEPTH / 2);
        //cyc.setMaterial(cycMat);
        //cyc.setShadowMode(RenderQueue.ShadowMode.Receive);
        //rootNode.attachChild(cyc);
    }

    public ArrayList<Fixture> createFixtures(Node rootNode, AssetManager assetManager,
            ViewPort viewPort) {
        ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
        
        // Positions
        float x_pos_op = STAGE_WIDTH / 2 + Par64.PAR_HEIGHT;
        float x_pos_p = -x_pos_op;
        float z_pos_fohc = (z_qtr() + z_fohc()) / 2;
        float z_pos_qtr = (z_half() + z_qtr()) / 2;
        float z_pos_half = z_half() + (z_half() - z_qtr()) / 2;
        
        // Rotations
        Quaternion rotation_p = new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_X);
        Quaternion rotation_p_1 = rotation_p.mult(new Quaternion().fromAngleAxis(PAR_ROATION_1, Vector3f.UNIT_Y));
        Quaternion rotation_p_2 = rotation_p.mult(new Quaternion().fromAngleAxis(PAR_ROATION_2, Vector3f.UNIT_Y));
        Quaternion rotation_p_3 = rotation_p.mult(new Quaternion().fromAngleAxis(PAR_ROATION_3, Vector3f.UNIT_Y));
        Quaternion rotation_op = new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_X);
        Quaternion rotation_op_1 = rotation_op.mult(new Quaternion().fromAngleAxis(-PAR_ROATION_1, Vector3f.UNIT_Y));
        Quaternion rotation_op_2 = rotation_op.mult(new Quaternion().fromAngleAxis(-PAR_ROATION_2, Vector3f.UNIT_Y));
        Quaternion rotation_op_3 = rotation_op.mult(new Quaternion().fromAngleAxis(-PAR_ROATION_3, Vector3f.UNIT_Y));
        
        // Starting address
        int universe = 1;
        
        // Temp variables
        Par64 par1, par2, par3;
        
        // FOHC Parcans
        par1 = new Par64("fohc-p-1", universe, PAR_DMX_START + 2*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_1, z_pos_fohc), rotation_p_1, rootNode, assetManager, viewPort);
        par2 = new Par64("fohc-p-2", universe, PAR_DMX_START + 1*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_2, z_pos_fohc), rotation_p_2, rootNode, assetManager, viewPort);
        par3 = new Par64("fohc-p-3", universe, PAR_DMX_START, new Vector3f(x_pos_p, PAR_Y_3, z_pos_fohc), rotation_p_3, rootNode, assetManager, viewPort);
        fixtures.add(par1);
        fixtures.add(par2);
        fixtures.add(par3);
        par1 = new Par64("fohc-op-1", universe, PAR_DMX_START + 17*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_1, z_pos_fohc), rotation_op_1, rootNode, assetManager, viewPort);
        par2 = new Par64("fohc-op-2", universe, PAR_DMX_START + 16*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_2, z_pos_fohc), rotation_op_2, rootNode, assetManager, viewPort);
        par3 = new Par64("fohc-op-3", universe, PAR_DMX_START + 15*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_3, z_pos_fohc), rotation_op_3, rootNode, assetManager, viewPort);
        fixtures.add(par1);
        fixtures.add(par2);
        fixtures.add(par3);
        
        // Qtr Parcans
        par1 = new Par64("qtr-p-1", universe, PAR_DMX_START + 5*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_1, z_pos_qtr), rotation_p_1, rootNode, assetManager, viewPort);
        par2 = new Par64("qtr-p-2", universe, PAR_DMX_START + 4*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_2, z_pos_qtr), rotation_p_2, rootNode, assetManager, viewPort);
        par3 = new Par64("qtr-p-3", universe, PAR_DMX_START + 3*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_3, z_pos_qtr), rotation_p_3, rootNode, assetManager, viewPort);
        fixtures.add(par1);
        fixtures.add(par2);
        fixtures.add(par3);
        par1 = new Par64("qtr-op-1", universe, PAR_DMX_START + 14*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_1, z_pos_qtr), rotation_op_1, rootNode, assetManager, viewPort);
        par2 = new Par64("qtr-op-2", universe, PAR_DMX_START + 13*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_2, z_pos_qtr), rotation_op_2, rootNode, assetManager, viewPort);
        par3 = new Par64("qtr-op-3", universe, PAR_DMX_START + 12*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_3, z_pos_qtr), rotation_op_3, rootNode, assetManager, viewPort);
        fixtures.add(par1);
        fixtures.add(par2);
        fixtures.add(par3);
        
        // Half Parcans
        par1 = new Par64("half-p-1", universe, PAR_DMX_START + 8*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_1, z_pos_half), rotation_p_1, rootNode, assetManager, viewPort);
        par2 = new Par64("half-p-2", universe, PAR_DMX_START + 7*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_2, z_pos_half), rotation_p_2, rootNode, assetManager, viewPort);
        par3 = new Par64("half-p-3", universe, PAR_DMX_START + 6*PAR_DMX_NADDRS, new Vector3f(x_pos_p, PAR_Y_3, z_pos_half), rotation_p_3, rootNode, assetManager, viewPort);
        fixtures.add(par1);
        fixtures.add(par2);
        fixtures.add(par3);
        par1 = new Par64("half-op-1", universe, PAR_DMX_START + 11*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_1, z_pos_half), rotation_op_1, rootNode, assetManager, viewPort);
        par2 = new Par64("half-op-2", universe, PAR_DMX_START + 10*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_2, z_pos_half), rotation_op_2, rootNode, assetManager, viewPort);
        par3 = new Par64("half-op-3", universe, PAR_DMX_START + 9*PAR_DMX_NADDRS, new Vector3f(x_pos_op, PAR_Y_3, z_pos_half), rotation_op_3, rootNode, assetManager, viewPort);
        fixtures.add(par1);
        fixtures.add(par2);
        fixtures.add(par3);
        
        // Cyc
        Cyc cyc = new Cyc(rootNode, assetManager);
        fixtures.add(cyc);
        
        // TODO - Remove
        DMXPacketTest dmx = new DMXPacketTest(1, 1, (byte) 255);
        dmx.fill(ColorRGBA.Blue, PAR_DMX_START, 18, PAR_DMX_NADDRS);
        for (int i = 0; i != Cyc.N_LIGHTS; ++i) {
            float value = (1.0f / Cyc.N_LIGHTS) * i;
            int addr = Cyc.DMX_ADDR + i*3;
            ColorRGBA colour = new ColorRGBA(1.0f, value, 0.0f, 1.0f);
            dmx.fill(colour, addr, 1);
        }
        //dmx.fill(ColorRGBA.Red, Cyc.DMX_ADDR, 30, Cyc.N_CHANNELS);
        //dmx.fill(ColorRGBA.Green, Cyc.DMX_ADDR + 30*Cyc.N_CHANNELS, 30, Cyc.N_CHANNELS);
        //dmx.fill(ColorRGBA.Blue, Cyc.DMX_ADDR + 60*Cyc.N_CHANNELS, 30, Cyc.N_CHANNELS);
        for (Fixture f : fixtures) {
            f.dmx_signal(dmx);
        }
        
        return fixtures;
    }
}
