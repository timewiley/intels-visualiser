package cserevue.intels.cameras;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import cserevue.intels.models.ScienceTheatre;

/**
 * FlyCam with additional methods to automatically position the camera in
 * various common locations.
 * 
 * @author Timothy Wiley <timothyw@cse.unsw.edu.au>
 */
public class TheatreFlyCam  {
    
    Camera cam;
    
    /**
     * Replicate FlyByCamera constructor
     * @param cam 
     */
    public TheatreFlyCam(Camera cam) {
        this.cam = cam;
    }
    
    /**
     * Position the camera at the dress circle
     */
    public void positionDressCircle() {
        cam.setLocation(new Vector3f(0, ScienceTheatre.DRESS_CIRCLE_Y, ScienceTheatre.DRESS_CIRCLE_Z));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }
}
