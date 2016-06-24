/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.model;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import nanodesigner.control.PhysicsEngineState;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;
import nanodesigner.utilities.parser.SPDBAtom;
import nanodesigner.utilities.parser.SPDBMolecule;

public final class MoleculeNode extends BatchNode {

    private String fileName, moleculeName;
    private String chainID, residueName;
    private boolean expendable;       //Lets the editor know that this molecule can be deleted by spatial sorting.
    private ArrayList<AtomModel> atoms = new ArrayList<AtomModel>();
    private CompoundCollisionShape compoundCollisionShape = null;
    RigidBodyControl rigidBodyControl = null;
    public Node compoundAtomNode = null;
    private boolean conflicted = false;
    private Geometry wireBoxGeometry;
    
    
    public MoleculeNode() {
        super("Molecule Node");
        fileName = "";
        chainID = "";
        moleculeName = "";
    }

    public static MoleculeNode buildMoleculeFromSPDB(SPDBMolecule spdbMolecule, Vector3f position) {
        MoleculeNode moleculeNode = new MoleculeNode();
        moleculeNode.setFileName(spdbMolecule.getFileName());
        moleculeNode.setMoleculeName(spdbMolecule.getName());
        moleculeNode.setResidueName(spdbMolecule.getAtoms().get(0).getResName());
        moleculeNode.move(position);
        moleculeNode.compoundAtomNode = new Node();

        for (int i = 0; i < spdbMolecule.getAtoms().size(); i++) {
            SPDBAtom spdbAtom = (SPDBAtom) spdbMolecule.getAtoms().get(i);
            AtomModel model = new AtomModel(spdbAtom.getName(), new Vector3f(spdbAtom.getX(), spdbAtom.getY(), spdbAtom.getZ()), moleculeNode);
            model.setOccupancy(spdbAtom.getOccupancy());
            model.setTempfactor(spdbAtom.getTempFactor());

            
            moleculeNode.addAtomModel(model);
        }
        
        moleculeNode.batch();
        return moleculeNode;
    }

    public void addAtomModel(AtomModel atomModel) {
        atoms.add(atomModel);
        attachChild(atomModel);
    }

    public void physicJoinAtoms() {
        PhysicsEngineState physicsState = AppManager.getSharedManager().getScene().getPhysicsEngineState();

        if (physicsState.getCollisionDetail() == PhysicsEngineState.CollisionDetail.SLOW
                || physicsState.getCollisionDetail() == PhysicsEngineState.CollisionDetail.NORMAL) {

            compoundCollisionShape = new CompoundCollisionShape();

            for (int i = 0; i < this.atoms.size(); i++) {
                AtomModel atomModel = (AtomModel) this.atoms.get(i);
                this.addControl(atomModel.getControl(RigidBodyControl.class));
                compoundCollisionShape.addChildShape(atomModel.getControl(RigidBodyControl.class).getCollisionShape(), atomModel.getLocalTranslation());
            }

            rigidBodyControl = new RigidBodyControl(compoundCollisionShape, 1.0f);
            this.addControl(rigidBodyControl);

        } else {
            BoundingBox boundingBox = (BoundingBox)getWorldBound();
            
            BoxCollisionShape boxCollisionShape = new BoxCollisionShape(new Vector3f(
                    boundingBox.getXExtent(),
                    boundingBox.getYExtent(),
                    boundingBox.getZExtent()));
            
            compoundCollisionShape = new CompoundCollisionShape();
            compoundCollisionShape.addChildShape(boxCollisionShape, new Vector3f().zero());
            rigidBodyControl = new RigidBodyControl(compoundCollisionShape, 2.0f);

        }
        this.addControl(rigidBodyControl);
        AppManager.getSharedManager().getScene().addRigidBodyToPhysicSpace(rigidBodyControl);

    }

    public void initialisePhysicBodies() {
        PhysicsEngineState physicsState = AppManager.getSharedManager().getScene().getPhysicsEngineState();

        if (physicsState.getCollisionDetail() == PhysicsEngineState.CollisionDetail.SLOW
                || physicsState.getCollisionDetail() == PhysicsEngineState.CollisionDetail.NORMAL) {

            for (int i = 0; i < this.atoms.size(); i++) {
                AtomModel atom = (AtomModel) this.atoms.get(i);
                atom.initialisePhysicBody();
            }
        }
        physicJoinAtoms();
    }

    void destroyPhysicBodies() {
        if (rigidBodyControl != null) {
            rigidBodyControl.setEnabled(false);
            rigidBodyControl.destroy();
        }

        for (int i = 0; i < this.atoms.size(); i++) {
            AtomModel atom = (AtomModel) this.atoms.get(i);
            atom.destroyPhysicBody();
        }
    }

    public ArrayList<AtomModel> getAtoms() {
        return atoms;
    }

    public void setAtoms(ArrayList<AtomModel> atoms) {
        this.atoms = atoms;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMoleculeName() {
        return moleculeName;
    }

    public void setMoleculeName(String moleculeName) {
        this.moleculeName = moleculeName;
    }

    public String getChainID() {
        return chainID;
    }

    public void setChainID(String chainID) {
        this.chainID = chainID;
    }
    public void setResidueName(String residueName) {
        this.residueName = residueName;
    }

    public String getResidueName() {
        return this.residueName;
    }
    
    public boolean isExpendable() {
        return expendable;
    }

    public void setExpendable(boolean expendable) {
        this.expendable = expendable;
    }

    public void setConflicted(boolean b) {

        if (this.conflicted != b) {
            this.conflicted = b;


            if (this.conflicted) {
                for (AtomModel atom : this.atoms) {

                    atom.getMaterial().setColor("Diffuse", ColorRGBA.Red);
                    atom.getMaterial().setColor("Specular", ColorRGBA.Red);

                }
            } else {
                for (AtomModel atom : this.atoms) {
                    atom.getMaterial().setColor("Diffuse", atom.getColor());
                    atom.getMaterial().setColor("Specular", atom.getColor());
                }
            }
        }
    }

    public boolean getConflicted() {
        return this.conflicted;

    }

    synchronized public void setMoleculeTranslation(final Vector3f pos) {
        final Scene scene = AppManager.getSharedManager().getScene();
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                setLocalTranslation(pos);
                return null;
            }
        });
    }

    public void setMoleculeRotation(final Vector3f rotationDegrees) {
        final Scene scene = AppManager.getSharedManager().getScene();
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                //Val should be in degrees, convert it to radians
                Vector3f angledegrees = rotationDegrees.mult(((float) Math.PI / 180));
                float angles[] = {angledegrees.x,angledegrees.y,angledegrees.z};
                Quaternion rot = new Quaternion(angles);
                setLocalRotation(rot);
                return null;
            }
        });
    }
}
