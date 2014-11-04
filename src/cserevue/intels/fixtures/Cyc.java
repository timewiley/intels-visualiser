/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cserevue.intels.fixtures;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import cserevue.intels.dmx.DMXPacket;
import cserevue.intels.models.ScienceTheatre;

/**
 * Custom fixture for the Cyc which is also has the model for it
 *
 * @author Tim
 */
public class Cyc extends Fixture {

    // DMX Properties
    public static final int DMX_ADDR      = 1;
    public static final int DMX_UNIVERSE  = 1;
    public static final int N_CHANNELS    = 3;
    
    // Ligh properties
    public static final int N_LIGHTS      = 112;
    public static final int N_VERTICIES   = 2 * (N_LIGHTS + 2);
    
    // Colour blending
    private static final int COLOUR_WINDOW  = 10;
    private static final int WINDOW_NORM    = 1 + (COLOUR_WINDOW * 2);
    
    // Cyc Mesh
    private Mesh cycMesh;
    
    // Cyc Geometry
    private Geometry cyc;
    
    // Cyc material
    private Material cycMat;
    
    // Array for cyc vertex colours
    private float[] vertexColours;
    
    // DMX controls
    private boolean newDMXPacket = false;

    public Cyc(Node rootNode, AssetManager assetManager) {
        super("Cyc", DMX_UNIVERSE, DMX_ADDR);

        // Construct custom Cyc Mesh
        cycMesh = new Mesh();

        // Vertex positions in space
        Vector3f[] vertices = new Vector3f[N_VERTICIES];
        for (int i = 0; i != N_LIGHTS + 2; ++i) {
            int index = 2 * i;
            float x_pos = i * ScienceTheatre.STAGE_WIDTH / (N_LIGHTS + 1);
            vertices[index]     = new Vector3f(x_pos, 0, 0);
            vertices[index + 1] = new Vector3f(x_pos, ScienceTheatre.STAGE_HEIGHT, 0);
        }
        //vertices[0] = new Vector3f(0, 0, 0);
        //vertices[1] = new Vector3f(ScienceTheatre.STAGE_WIDTH, 0, 0);
        //vertices[2] = new Vector3f(0, ScienceTheatre.STAGE_HEIGHT, 0);
        //vertices[3] = new Vector3f(ScienceTheatre.STAGE_WIDTH, ScienceTheatre.STAGE_HEIGHT, 0);

        // Indexes - Define the order in which mesh triangles should be constructed
        //int[] indicies = {2, 0, 1, 1, 3, 2};
        int[] indicies = new int[6 * (N_LIGHTS + 1)];
        int indicies_index = 0;
        for (int i = 0; i != N_LIGHTS + 1; ++i) {
            int index_botLeft = 2 * i;
            int index_topLeft = 2 * i + 1;
            int index_botRight = 2 * (i + 1);
            int index_topRight = 2 * (i + 1) + 1;
            
            // Triangle 1
            indicies[indicies_index]     = index_botLeft;
            indicies[indicies_index + 1] = index_botRight;
            indicies[indicies_index + 2] = index_topLeft;
            
            // Triangle 2
            indicies[indicies_index + 3] = index_botRight;
            indicies[indicies_index + 4] = index_topRight;
            indicies[indicies_index + 5] = index_topLeft;
            
            indicies_index += 6;
        }

        // Set buffers for vertexes and indivicies - no texture, so ignored
        cycMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        //cycMesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        cycMesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indicies));
        cycMesh.updateBound();

        // Geometry for cyc
        cyc = new Geometry("Cyc", cycMesh);
        cyc.setLocalTranslation(-ScienceTheatre.STAGE_WIDTH / 2, ScienceTheatre.STAGE_FLOOR_HEIGHT, -ScienceTheatre.STAGE_DEPTH / 2 + ScienceTheatre.CURTAIN_WIDTH / 2);

        // Configure Cyc Material
        cycMat = new Material(assetManager, ScienceTheatre.MAT_UNSHADED);
        //cycMat.setBoolean("VertexLighting", true);
        //cycMat.setBoolean("UseVertexColor", true);
        cycMat.setBoolean("VertexColor", true);
        //cycMat.setBoolean("UseMaterialColors", true);
        //cycMat.setColor("Ambient", ColorRGBA.White.mult(1.0f));
        //cycMat.setColor("Diffuse", ColorRGBA.White);
        //cycMat.setFloat("Shininess", 100f);
        cyc.setMaterial(cycMat);

        // Initialise vertexColours array, and fill with black
        // We have 4 vertices and 4 color values for each of them.
        // If you have more vertices, you need 'new float[yourVertexCount * 4]' here!
        vertexColours = new float[N_VERTICIES * 4];
        int colourIndex = 0;

        // Set custom RGBA value for each Vertex. Values range from 0.0f to 1.0f
        for (int i = 0; i < N_VERTICIES; i++) {
            colourIndex = i * 4;

            vertexColours[colourIndex] = 1.0f;
            vertexColours[colourIndex + 1] = 1.0f;
            vertexColours[colourIndex + 2] = 1.0f;
            vertexColours[colourIndex + 3] = 1.0f;
        }

        // Set the color buffer
        cycMesh.setBuffer(VertexBuffer.Type.Color, 4, vertexColours);

        // Attach to root Node
        rootNode.attachChild(cyc);
        newDMXPacket = false;
    }

    @Override
    public void dmx_signal(DMXPacket dmx) {
        //System.out.println("Cyc dmx_signal: " + dmx.getUniverse());
        if (dmx.getUniverse() != getUniverse()) { return; }
        
        synchronized(this) {
            // Colours for blending
            Vector3f rgb = new Vector3f();//(0.5f, 0.5f, 0.5f);
            
            // Set initial colours
            int dmxOffset = 0;
            int forwardAddr = 0;
            int backAddr = 0;
            int colourScale = 1;
            rgb.x += (COLOUR_WINDOW + 1) * dmx.getValueFloat(address);
            rgb.y += (COLOUR_WINDOW + 1) * dmx.getValueFloat(address + 1);
            rgb.z += (COLOUR_WINDOW + 1) * dmx.getValueFloat(address + 2);
            for (int i = 0; i != COLOUR_WINDOW; ++i) {
                dmxOffset = calcDMXOffset(address, i + 1);
                rgb.x += dmx.getValueFloat(dmxOffset);
                rgb.y += dmx.getValueFloat(dmxOffset + 1);
                rgb.z += dmx.getValueFloat(dmxOffset + 2);
            }
            //float whiteAdjust = 0.2f * WINDOW_NORM;
            //r += whiteAdjust;
            //g += whiteAdjust;
            //b += whiteAdjust;
            
            // Set the first vertex colours & first light (rgba)
            int colourIndex = 0;
            Vector3f norm = rgb.normalize();
            for (int i = 0; i != 2; ++i) {
                colourIndex = i * 8;
                vertexColours[colourIndex]     = colourScale * norm.x;
                vertexColours[colourIndex + 1] = colourScale * norm.y;
                vertexColours[colourIndex + 2] = colourScale * norm.z;
                vertexColours[colourIndex + 3] = 1.0f;
                vertexColours[colourIndex + 4] = vertexColours[colourIndex];
                vertexColours[colourIndex + 5] = vertexColours[colourIndex + 1];
                vertexColours[colourIndex + 6] = vertexColours[colourIndex + 2];
                vertexColours[colourIndex + 7] = vertexColours[colourIndex + 3];
            }
            //System.out.println("Cyc: " + r + ", " + g + ", " + b);
            System.out.println("Cyc: " + norm.x + ", " + norm.y + ", " + norm.z);

            // Set custom RGBA value for each Vertex. Values range from 0.0f to 1.0f    
            for (int i = 1; i != N_LIGHTS; i++) {
                colourIndex = (i + 1) * 8;
                
                // Add next colour for window size (leading edge of window)
                forwardAddr = Math.min(i + COLOUR_WINDOW, N_LIGHTS - 1);
                dmxOffset = calcDMXOffset(address, forwardAddr);
                rgb.x += dmx.getValueFloat(dmxOffset);
                rgb.y += dmx.getValueFloat(dmxOffset + 1);
                rgb.z += dmx.getValueFloat(dmxOffset + 2);
                
                // Remove previous colour for window (-1 from trailing edge of window)
                backAddr = Math.max(0, i - COLOUR_WINDOW - 1);
                dmxOffset = calcDMXOffset(address, backAddr);
                rgb.x -= dmx.getValueFloat(dmxOffset);
                rgb.y -= dmx.getValueFloat(dmxOffset + 1);
                rgb.z -= dmx.getValueFloat(dmxOffset + 2);
                //System.out.println("Cyc: " + r + ", " + g + ", " + b);
                
                norm = rgb.normalize();
                System.out.println("Cyc: " + norm.x + ", " + norm.y + ", " + norm.z);
                
                // Bottom Vertex (rgba)
                vertexColours[colourIndex]     = colourScale * norm.x;
                vertexColours[colourIndex + 1] = colourScale * norm.y;
                vertexColours[colourIndex + 2] = colourScale * norm.z;
                vertexColours[colourIndex + 3] = 1.0f;

                // Top Vertex
                vertexColours[colourIndex + 4] = vertexColours[colourIndex];
                vertexColours[colourIndex + 5] = vertexColours[colourIndex + 1];
                vertexColours[colourIndex + 6] = vertexColours[colourIndex + 2];
                vertexColours[colourIndex + 7] = vertexColours[colourIndex + 3];
            }

            // Set the end vertex colours
            vertexColours[vertexColours.length - 8] = vertexColours[vertexColours.length - 12];
            vertexColours[vertexColours.length - 7] = vertexColours[vertexColours.length - 11];
            vertexColours[vertexColours.length - 6] = vertexColours[vertexColours.length - 10];
            vertexColours[vertexColours.length - 5] = vertexColours[vertexColours.length - 9];
            vertexColours[vertexColours.length - 4] = vertexColours[vertexColours.length - 8];
            vertexColours[vertexColours.length - 3] = vertexColours[vertexColours.length - 7];
            vertexColours[vertexColours.length - 2] = vertexColours[vertexColours.length - 6];
            vertexColours[vertexColours.length - 1] = vertexColours[vertexColours.length - 5];
            
            newDMXPacket = true;
        }
    }
    
    private static int calcDMXOffset(int addr, int index) {
        return addr + (index * N_CHANNELS);
    }

    @Override
    public void controlUpdate(float tpf) {
        if (newDMXPacket) {
            synchronized(this) {
                // Set the color buffer
                cycMesh.setBuffer(VertexBuffer.Type.Color, 4, vertexColours);
                newDMXPacket = false;
            }
        }
    }
}
