/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.model;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;
import nanodesigner.model.ForceModel;
import nanodesigner.view.MarkerManageDialog;
import nanodesigner.view.MarkerManageDialog.MarkerColor;
import nanodesigner.view.MarkerManageDialog.MarkerShape;

/**
 *
 * @author Oliver
 */
public class MarkerModel extends Geometry {

    String identifier = null;
    MarkerColor markerColor;
    MarkerShape markerShape;
    Material mat;
    ArrayList<MoleculeNode> molecules = new ArrayList<MoleculeNode>();
    ForceModel force = null;
    String forceString = null;
    String segmentString = null;

    public MarkerModel(Vector3f location, MarkerColor color, MarkerShape shape, String identifier) {
    }

    public MarkerModel(Vector3f location, MarkerColor markerColor, MarkerShape markerShape, String segmentString, String forceString, String idString) {
        Scene scene = AppManager.getSharedManager().getScene();
        this.markerColor = markerColor;
        this.markerShape = markerShape;
        this.forceString = forceString;
        this.segmentString = segmentString;
        this.identifier = idString;

        this.setMesh(getMeshFromShape(markerShape));
        mat = new Material(scene.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", getColor());
        this.setMaterial(mat);

        populateMoleculesForGroup(this.segmentString);
        setForceForString(this.forceString);

        this.move(location);
    }

    public void updateContents(final Vector3f vector3f, final MarkerColor aMarkerColor, final MarkerShape aMarkerShape, final String text) {
        Scene scene = AppManager.sharedManager.getScene();
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                setLocalTranslation(vector3f);
                markerColor = aMarkerColor;
                mat.setColor("Color", getColor());
                markerShape = aMarkerShape;
                Mesh aMesh = getMeshFromShape(markerShape);
                setMesh(aMesh);
                identifier = text;
                return null;
            }
        });
    }

    public void updateFromPhysicsThread() {
    }

    public void updateFromSceneThread() {
        if (force != null) {
            force.updateFromSceneThread();
        }
    }

    public void repopulateMolecules() {
        molecules.clear();
        EnvironmentNode environmentNode = AppManager.sharedManager.getScene().getEnvironmentNode();

        for (int i = 0; i < environmentNode.molecules.size(); i++) {
            MoleculeNode molecule = (MoleculeNode) environmentNode.molecules.get(i);

            if (molecule.getChainID().equals(this.segmentString) || this.segmentString.equals("ALL")) {
                molecules.add(molecule);
            }
        }
    }
    
    public void populateMoleculesForGroup(String groupString) {
        EnvironmentNode environmentNode = AppManager.sharedManager.getScene().getEnvironmentNode();
        molecules.clear();

        for (int i = 0; i < environmentNode.molecules.size(); i++) {
            MoleculeNode molecule = (MoleculeNode) environmentNode.molecules.get(i);

            if (molecule.getChainID().equals(groupString) || groupString.equals("ALL")) {
                molecules.add(molecule);
            }
        }
    }

    public ColorRGBA getColor() {
        if (this.markerColor.toString().equals("Blue")) {
            return ColorRGBA.Blue;
        } else if (this.markerColor.toString().equals("Green")) {
            return ColorRGBA.Green;
        } else if (this.markerColor.toString().equals("Red")) {
            return ColorRGBA.Red;
        } else if (this.markerColor.toString().equals("Magenta")) {
            return ColorRGBA.Magenta;
        } else if (this.markerColor.toString().equals("Orange")) {
            return ColorRGBA.Orange;
        }
        return ColorRGBA.Green;
    }

    private Mesh getMeshFromShape(MarkerShape markerShape) {
        Mesh aShape = null;
        if (markerShape.toString().equals("Cube")) {
            aShape = new WireBox(5, 5, 5);
        } else if (markerShape.toString().equals("Cylinder")) {
            aShape = new Cylinder(10, 10, 5, 5);
        } else if (markerShape.toString().equals("Sphere")) {
            aShape = new Sphere(10, 10, 5);
        } else if (markerShape.toString().equals("Dome")) {
            aShape = new Dome(new Vector3f(3, 3, 3), 3, 10, 5);
        } else if (markerShape.toString().equals("Torus")) {
            aShape = new Torus(10, 10, 5, 10);
        }
        return aShape;
    }

    public ForceModel getForce() {
        return force;
    }

    public void setForceForType(ForceModel.ForceType type) {
        ForceModel forceModel = null;
        if (type.toString().equals("AttractForce")) {
            forceModel = new AttractForce(this);
        } else if (type.toString().equals("RepulseForce")) {
            forceModel = new RepulseForce(this);
        } else if (type.toString().equals("NullForce")) {
            forceModel = null;
        }
        this.force = forceModel;
    }

    private void setForceForString(String forceString) {
        ForceModel forceModel = null;
        if (forceString.equals("Attraction")) {
            forceModel = new AttractForce(this);
        } else if (forceString.equals("Repulsion")) {
            forceModel = new RepulseForce(this);
        } else if (forceString.equals("Nothing")) {
            forceModel = null;
        }
        this.force = forceModel;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MarkerColor getMarkerColor() {
        return markerColor;
    }

    public MarkerShape getMarkerShape() {
        return markerShape;
    }

    void reinitialise() {
        repopulateMolecules();
    }
}
