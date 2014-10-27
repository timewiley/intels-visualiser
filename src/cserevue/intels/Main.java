package cserevue.intels;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import cserevue.intels.cameras.TheatreFlyCam;
import cserevue.intels.fixtures.Fixture;
import cserevue.intels.models.ScienceTheatre;
import java.util.ArrayList;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    // Models
    private ScienceTheatre theatre;
    
    // Fixtures
    private ArrayList<Fixture> fixtures;
    
    // Camera/View
    private TheatreFlyCam theatreCam;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Setup Window defaults
        setupWindow();
        
        // Setup models
        setupModels();
        
        // Fixtures
        setupFixtures();
        
        // Setup Camera(s)
        setupCameras();
        
        // Setup and enable ArtNet DMX
        setupArtNet();
        
        // REMOVE IF NOT DEBUGGING
        //viewPort.addProcessor(new WireProcessor(assetManager));
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    protected void setupWindow() {
        // Ambient Light
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(al);
        
        //DirectionalLight sun = new DirectionalLight();
        //sun.setColor(ColorRGBA.White);
        //sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        //rootNode.addLight(sun);
        
        // Shadows
        rootNode.setShadowMode(RenderQueue.ShadowMode.Off);
    }
    
    protected void setupModels() {
        // Add Science Theatre Model
        theatre = new ScienceTheatre();
        theatre.addModel(rootNode, assetManager);
        
        // Setup bgcolour/skybox
        viewPort.setBackgroundColor(new ColorRGBA(0.15f, 0.15f, 0.15f, 1.0f));
        //rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        //rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Terrain/Rock2/rock.jpg", true));
    }
    
    protected void setupFixtures() {
        // Fixture list
        fixtures = new ArrayList<Fixture>();
                
        // Science Theatre Fixtures
        ArrayList<Fixture> scienceTheatre = theatre.createFixtures(rootNode, assetManager, viewPort);
        fixtures.addAll(scienceTheatre);
    }
    
    protected void setupCameras() {
        // Create theatre cam and position
        theatreCam = new TheatreFlyCam(cam);
        theatreCam.positionDressCircle();
        
        // Configure Cursor movements
        inputManager.setCursorVisible(true);
        flyCam.setDragToRotate(true);
        
        // Set camera movement speed
        flyCam.setMoveSpeed(10);
    }
    
    protected void setupArtNet() {
        // TODO
    }
}
