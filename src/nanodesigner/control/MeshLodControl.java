package nanodesigner.control;

import com.jme3.bounding.BoundingVolume;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.AreaUtils;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 * Determines what Level of Detail a spatial should be, based on how many pixels
 * on the screen the spatial is taking up, or how far away from the mesh the
 * camera is located. The more pixels covered or the smaller distance, the more
 * detailed the spatial should be. It calculates the area of the screen that the
 * spatial covers by using its bounding box. When initializing, it will ask the
 * spatial for how many triangles it has for each LOD. It then uses that, along
 * with the trisPerPixel value to determine what LOD it should be at. It
 * requires the camera to do this. Alternatively if only considering the
 * distance, the cameras location will be used to calculate the distance between
 * the mesh and the cameara. Level of Detail will be chosen based on this
 * distance.
 * 
* The controlRender method is called each frame and will update the spatial’s
 * LOD if the camera has moved by a specified amount.
 * 
* @author thomasw
 * 
*/
public class MeshLodControl extends AbstractControl implements Cloneable {

    private float trisPerPixel = 1f;
    private float distTolerance = 1f;
    private float lastDistance = 0f;
    private int lastLevel = 0;
    private int numLevels;
    private int[] numTris;
    private Mesh[] meshes;
    private boolean ignoreTrisPerPixel = false;
    Vector3f fixedCamLocation = null;

    /**
     * Creates a new
     * <code>MeshLodControl</code>.
     */
    public MeshLodControl(Mesh[] meshes) {
        this.meshes = meshes;
    }

    /**
     * Returns the distance tolerance for changing LOD.
     *     
* @return the distance tolerance for changing LOD.
     *     
* @see #setDistTolerance(float)
     */
    public float getDistTolerance() {
        return distTolerance;
    }

    /**
     * Specifies the distance tolerance for changing the LOD level on the
     * geometry. The LOD level will only get changed if the geometry has moved
     * this distance beyond the current LOD level.
     *     
* @param distTolerance distance tolerance for changing LOD
     */
    public void setDistTolerance(float distTolerance) {
        this.distTolerance = distTolerance;
    }

    /**
     * Returns the triangles per pixel value.
     *     
* @return the triangles per pixel value.
     *     
* @see #setTrisPerPixel(float)
     */
    public float getTrisPerPixel() {
        return trisPerPixel;
    }

    /**
     * Sets the triangles per pixel value. The
     * <code>MeshLodControl</code> will use this value as an error metric to
     * determine which LOD level to use based on the geometry’s area on the
     * screen.
     *     
* @param trisPerPixel triangles per pixel
     */
    public void setTrisPerPixel(float trisPerPixel) {
        this.trisPerPixel = trisPerPixel;
    }

    /**
     * Sets whether the triangles per pixel should be used for calculating the
     * Level of Detail, or not. The
     * <code>MeshLodControl</code> will only use the distance between the camera
     * and the mesh for calculating the LOD if trisPerPixel is ignored.
     *     
* @param ignoreTrisPerPixel ignore triangles per pixel
     */
    public void setIgnoreTrisPerPixel(boolean ignoreTrisPerPixel) {
        this.ignoreTrisPerPixel = ignoreTrisPerPixel;
    }

    /**
     * Sets a fixed location for the camera (do not affect the actual camera),
     * that this
     * <code>MeshLodControl</code> will calculate the LOD based on.
     *     
* @param fixedCamLocation ignore triangles per pixel
     */
    public void setFixedCamLocation(Vector3f fixedCamLocation) {
        this.fixedCamLocation = fixedCamLocation;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if (!(spatial instanceof Geometry)) {
            throw new IllegalArgumentException("LodControl can only be attached to Geometry!");
        }

        super.setSpatial(spatial);
        numLevels = meshes.length;
        numTris = new int[numLevels];


        for (int i = numLevels - 1; i >= 0; i--) {
            numTris[i] = meshes[i].getTriangleCount();

        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        try {
            MeshLodControl clone = (MeshLodControl) super.clone();
            clone.lastDistance = 0;
            clone.lastLevel = 0;
            clone.numTris = numTris != null ? numTris.clone() : null;
            return clone;
        } 
        catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        float newDistance;
        int level;

        if (ignoreTrisPerPixel) {
            Vector3f pos = spatial.getWorldTranslation();
            if (fixedCamLocation != null) {
                newDistance = pos.distance(fixedCamLocation);
            } else {
                newDistance = pos.distance(vp.getCamera().getLocation());
            }

            level = 0;
            while (newDistance > 0 && level < numLevels - 1) {
                newDistance -= distTolerance;
                if (newDistance > 0) {
                    level++;
                }
            }
        } else {
            BoundingVolume bv = spatial.getWorldBound();

            Camera cam = vp.getCamera();
            float atanNH = FastMath.atan(cam.getFrustumNear() * cam.getFrustumTop());
            float ratio = (FastMath.PI / (8f * atanNH));
            newDistance = bv.distanceTo(vp.getCamera().getLocation()) / ratio;

            if (Math.abs(newDistance - lastDistance) <= distTolerance) {
                level = lastLevel; // we haven’t moved relative to the model, send the old measurement back.
            } else if (lastDistance > newDistance && lastLevel == 0) {
                level = lastLevel; // we’re already at the lowest setting and we just got closer to the model, no need to keep trying.
            } else if (lastDistance < newDistance && lastLevel == numLevels - 1) {
                level = lastLevel; // we’re already at the highest setting and we just got further from the model, no need to keep trying.
            } else {
                lastDistance = newDistance;
                // estimate area of polygon via bounding volume
                float area = AreaUtils.calcScreenArea(bv, lastDistance, cam.getWidth());
                float trisToDraw = area * trisPerPixel;
                level = numLevels - 1;
                for (int i = numLevels; -i >= 0;) {
                    if (trisToDraw - numTris[i] < 0) {
                        break;
                    }
                    level = i;
                }
            }
        }

        lastLevel = level;
        ((Geometry) spatial).setMesh(meshes[level]);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule oc = ex.getCapsule(this);
        oc.write(trisPerPixel, "trisPerPixel", 1f);
        oc.write(distTolerance, "distTolerance", 1f);
        oc.write(numLevels, "numLevels", 0);
        oc.write(numTris, "numTris", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        trisPerPixel = ic.readFloat("trisPerPixel", 1f);
        distTolerance = ic.readFloat("distTolerance", 1f);
        numLevels = ic.readInt("numLevels", 0);
        numTris = ic.readIntArray("numTris", null);
    }
}
