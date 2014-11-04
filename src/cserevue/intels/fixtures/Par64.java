package cserevue.intels.fixtures;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import cserevue.intels.models.Model;

/**
 * Par64 Fixture
 * @author Tim
 */
public class Par64 extends RGBFixture {
    
    // Dimensions
    public static final int   PAR_DOME_PLANES       = 20;
    public static final float PAR_HEIGHT            = 0.5f;
    public static final float PAR_RADIUS            = 0.2f;
    public static final int   PAR_SAMPLES_AXIS      = 20;
    public static final int   PAR_SAMPLES_RADIAL    = 20;
    
    public static final float PAR_DISC_HEIGHT       = 0.01f;
    public static final float PAR_DISC_RADIUS       = PAR_RADIUS - 0.03f;
    
    // Meshes
    private static final Cylinder cylinderMesh = new Cylinder(PAR_SAMPLES_AXIS, PAR_SAMPLES_RADIAL, PAR_RADIUS, PAR_HEIGHT, false);
    private static final Cylinder discMesh     = new Cylinder(PAR_SAMPLES_AXIS, PAR_SAMPLES_RADIAL, PAR_DISC_RADIUS, PAR_DISC_HEIGHT, true);
    private static final Dome     backMesh     = new Dome(PAR_DOME_PLANES, PAR_SAMPLES_RADIAL, PAR_RADIUS);
    
    // Disc
    Material discMat;
    
    // Lamp
    private SpotLight lamp;
    
    /**
     * Create a new Par64 fixture
     * @param rootNode 
     */
    public Par64(String id, int universe, int address, 
            Vector3f position, Quaternion rotation, 
            Node rootNode, AssetManager assetManager, ViewPort viewPort) {
        super(id, universe, address);
        
        // Par64 Materials
        ColorRGBA blackColour = new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f);
        Material blackMat = new Material(assetManager, Model.MAT_LIGHTING);
        blackMat.setBoolean("UseMaterialColors",true);
        blackMat.setColor("Ambient", blackColour);
        blackMat.setColor("Diffuse", blackColour);
        blackMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
               
        // Model (as basic cylinder for now, with top missing)
        Node par64 = new Node("Par64-" + id);
        Geometry cylinder = new Geometry("Par64-cylinder-" + id, cylinderMesh);
        Geometry back = new Geometry("Par64-back-" + id, backMesh); 
        back.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.PI / 2, Vector3f.UNIT_X));
        back.setLocalTranslation(0, 0, -PAR_HEIGHT / 2);
        par64.attachChild(cylinder);
        par64.attachChild(back);
        par64.setLocalRotation(rotation);
        par64.setLocalTranslation(position);
        par64.setMaterial(blackMat);
        par64.setShadowMode(RenderQueue.ShadowMode.Cast);
        rootNode.attachChild(par64);
        
        // Front coloured disc for effect
        discMat = new Material(assetManager, Model.MAT_UNSHADED);
        //discMat.setBoolean("UseMaterialColors",true);
        //discMat.setColor("Ambient", blackColour);
        //discMat.setColor("Diffuse", blackColour);
        discMat.setColor("Color", blackColour);
        Geometry disc = new Geometry("Par64-disc-" + id, discMesh);
        disc.setLocalTranslation(0, 0, PAR_HEIGHT / 2);
        disc.setMaterial(discMat);
        par64.attachChild(disc);
        
        // Light Source
        lamp = new SpotLight();
        lamp.setSpotRange(30f);
        lamp.setSpotInnerAngle(5f * FastMath.DEG_TO_RAD);
        lamp.setSpotOuterAngle(15f * FastMath.DEG_TO_RAD);
        lamp.setColor(dmxColour);
        lamp.setPosition(position);
        lamp.setDirection(rotation.mult(Vector3f.UNIT_Z));
        rootNode.addLight(lamp);
    }

    @Override
    public void controlUpdate(float tpf) {
        // Set par colour
        if (newDMXPacket) {
            synchronized(this) {
                //discMat.setColor("Ambient", colour);
                //discMat.setColor("Diffuse", colour);
                discMat.setColor("Color", dmxColour);
                newDMXPacket = false;
            }
        }
    }
    
}
