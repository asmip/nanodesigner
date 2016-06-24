/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.model;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import nanodesigner.control.MeshLodControl;
import nanodesigner.control.PhysicsEngineState;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;

public class AtomModel extends Geometry {

    final Material H_MATERIAL = new Material(AppManager.getSharedManager().getScene().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    final Material C_MATERIAL = new Material(AppManager.getSharedManager().getScene().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    final Material N_MATERIAL = new Material(AppManager.getSharedManager().getScene().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    final Material O_MATERIAL = new Material(AppManager.getSharedManager().getScene().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        
    public void setConflicted(boolean b) {
        if (b) {
            getMaterial().setColor("Diffuse", ColorRGBA.Red);
            getMaterial().setColor("Specular", ColorRGBA.Red);
        } else {
            getMaterial().setColor("Diffuse", getColor());
            getMaterial().setColor("Specular", getColor());
        }
    }

    public enum AtomType {

        AtomHydrogen, AtomNitrogen, AtomOxygen, AtomCarbon
    }
    /*The AtomModel is the Model of the Atom. 
     *It contains the atoms 3D coordinates
     *The atom type with predefined atomic values 
     *Expand for 3D model and rendering here
     * 
     */
    private AtomType type;
    private MoleculeNode parentMoleculeNode;
    private int periodicIndex = -1;
    private String nameString = "";
    private String symbolString = "";
    private String parseNameString = "";
    private float vdw_radius;
    public ColorRGBA color;
    private String occupancy = "", tempfactor = "", charge ="";
    RigidBodyControl rigidBodyControl = null;
    private float atomMassAmu = -1;
    private ArrayList<AtomModel> bonds = new ArrayList<AtomModel>();
    
    
    public float getAtomMassAmu() {
        return atomMassAmu;
    }

    public void setAtomMassAmu(float atomMassAmu) {
        this.atomMassAmu = atomMassAmu;
    }

    public AtomModel(String parseNameString, Vector3f position, MoleculeNode parentNode) {
        super(parseNameString);
        populateTypeFromString(parseNameString);

        Mesh meshs[] = new Mesh[2];
        meshs[0] = new Sphere(8, 8, this.vdw_radius, true, false);
        this.setMesh(meshs[0]);
        this.parentMoleculeNode = parentNode;
        
        this.move(position);
    }

    public void initialisePhysicBody() {
        PhysicsEngineState physicsState = AppManager.getSharedManager().getScene().getPhysicsEngineState();
        CollisionShape collisionShape = null;

        if (physicsState.getCollisionDetail() == PhysicsEngineState.CollisionDetail.SLOW) {
            collisionShape = new SphereCollisionShape(1.0f * physicsState.getCollisionBoxSize());
        } else if (physicsState.getCollisionDetail() == PhysicsEngineState.CollisionDetail.NORMAL) {
            collisionShape = new BoxCollisionShape(new Vector3f(1.0f, 1.0f, 1.0f).mult(physicsState.getCollisionBoxSize()));
        }

        rigidBodyControl = new RigidBodyControl(collisionShape, 1.0f);
        this.addControl(rigidBodyControl);
    }

    public void destroyPhysicBody() {
        if (rigidBodyControl != null) {
            rigidBodyControl.setEnabled(false);
            //   this.removeControl(rigidBodyControl);
            //    rigidBodyControl.destroy(); 
        }
    }

    private void populateTypeFromString(String parseNameString) {
        this.parseNameString = parseNameString;
        String frontSymbol = Character.toString(parseNameString.charAt(0));
        this.symbolString = frontSymbol;

        if (frontSymbol.equalsIgnoreCase("h")) {
            populateAtomFromType(AtomType.AtomHydrogen);
        } else if (frontSymbol.equalsIgnoreCase("c")) {
            populateAtomFromType(AtomType.AtomCarbon);
        } else if (frontSymbol.equalsIgnoreCase("n")) {
            populateAtomFromType(AtomType.AtomNitrogen);
        } else if (frontSymbol.equalsIgnoreCase("o")) {
            populateAtomFromType(AtomType.AtomOxygen);
        }

    }

    private void populateAtomFromType(AtomType type) {
        this.type = type;
        // System.out.println("Populate type: " + type);
        /*Hydrogen 	1.20 (1.09)[1]
         Carbon 	1.70
         Nitrogen 	1.55
         Oxygen 	1.52
         Fluorine 	1.47
         Phosphorus 	1.80
         Sulfur 	1.80
         Chlorine 	1.75
         Copper 	1.4
         */
        if (type == AtomType.AtomHydrogen) {
            this.color = ColorRGBA.Blue;
            this.periodicIndex = 1;
            this.vdw_radius = 1.2f;
            this.atomMassAmu = 1.00794f;
            this.nameString = "Hydrogen";
            this.symbolString = "H";
            setupMaterial(H_MATERIAL);
        } else if (type == AtomType.AtomCarbon) {
            this.color = ColorRGBA.Brown;
            this.periodicIndex = 0;
            this.vdw_radius = 1.7f;
            this.atomMassAmu = 12.0107f;
            this.nameString = "Carbon";
            this.symbolString = "C";
            setupMaterial(C_MATERIAL);
        } else if (type == AtomType.AtomNitrogen) {
            this.color = ColorRGBA.LightGray;
            this.periodicIndex = 0;
            this.vdw_radius = 1.55f;
            this.atomMassAmu = 14.00674f;
            this.nameString = "Nitrogen";
            this.symbolString = "N";
            setupMaterial(N_MATERIAL);
        } else if (type == AtomType.AtomOxygen) {
            this.color = ColorRGBA.Green;
            this.periodicIndex = 6;
            this.vdw_radius = 1.52f;
            this.atomMassAmu = 15.9994f;
            this.nameString = "Oxygen";
            this.symbolString = "O";
            setupMaterial(O_MATERIAL);
        }
    }
    
    public void setupMaterial(Material material){
        material.setFloat("Shininess", 15f);
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", ColorRGBA.White); //Using white here, but shouldn’t matter that much
        material.setColor("Diffuse", this.color);
        material.setColor("Specular", this.color); //Using yellow for example
        this.setMaterial(material);
    }

    synchronized public void setAtomTranslation(final Vector3f pos) {
        final Scene scene = AppManager.getSharedManager().getScene();
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                setLocalTranslation(pos);
                return null;
            }
        });
    }

    public Vector3f getBatchWorldTranslation() {
        //Get the parent compoundNode Position
        Vector3f batchedPos = this.getParentMoleculeNode().getWorldTranslation();
        //Offset the position with the AtomModels local translation and parent compoundNode Position
        Vector3f offset = batchedPos.add(this.getLocalTranslation());
        //Get the rotational state of the compoundNode
        Quaternion molRot = this.getParentMoleculeNode().getWorldTransform().getRotation(); //this.getParentMoleculeNode().compoundAtomNode.getWorldTransform().getRotation();
        //Mult the rotation with  offset to get position
        Vector3f newPos = molRot.mult(offset);

        return getWorldTranslation();
    }

    public AtomType getType() {
        return type;
    }

    public MoleculeNode getParentMoleculeNode() {
        return parentMoleculeNode;
    }

    public void setParentMoleculeNode(MoleculeNode parentMoleculeNode) {
        this.parentMoleculeNode = parentMoleculeNode;
    }

    public ArrayList<AtomModel> getBonds() {
        return bonds;
    }

    public void setBonds(ArrayList<AtomModel> bonds) {
        this.bonds = bonds;
    }
    
    public int getPeriodicIndex() {
        return periodicIndex;
    }

    public String getNameString() {
        return nameString;
    }

    public String getSymbolString() {
        return symbolString;
    }

    public float getVdw_radius() {
        return vdw_radius;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public String getParseNameString() {
        return parseNameString;
    }

    public void setParseNameString(String parseNameString) {
        this.parseNameString = parseNameString;
    }

    public String getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(String occupancy) {
        this.occupancy = occupancy;
    }

    public String getTempfactor() {
        return tempfactor;
    }

    public void setTempfactor(String tempfactor) {
        this.tempfactor = tempfactor;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }
}
