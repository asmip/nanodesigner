/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.model;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;

/**
 *
 * @author Oliver
 *
 * ForceModel is the abstract class of all forces
 */
public abstract class ForceModel {

    public enum ForceType {
        //A enumerator of all the types of forces.
        NullForce,
        AttractForce,
        RepulseForce
    };
    MarkerModel markerModel = null;
    float magnitude;

    abstract public void updateFromPhysicsThread();

    abstract public void updateFromSceneThread();

    abstract void applyForce();

    public ForceModel(MarkerModel markerModel) {
        this.markerModel = markerModel;
    }
}

class AttractForce extends ForceModel {

    public AttractForce(MarkerModel markerModel) {
        super(markerModel);
    }

    @Override
    public void updateFromPhysicsThread() {
        //applyForce();
    }

    @Override
    public void updateFromSceneThread() {
        applyForce();
    }

    @Override
    void applyForce() {

        for (int i = 0; i < this.markerModel.molecules.size(); i++) {
            //Reference and calculate force
            MoleculeNode molecule = (MoleculeNode) this.markerModel.molecules.get(i);
            Vector3f markerPos = markerModel.getWorldTranslation();
            float distance = molecule.getWorldTranslation().distance(markerPos);
            Vector3f direction = markerPos.subtract(molecule.getWorldTranslation()).normalize();

            //Apply the force to the Physics Body attached to the MoleculeNode
            RigidBodyControl control = molecule.getControl(RigidBodyControl.class);
            if (control != null) {
                float force = 5;// * (distance + 10)/ distance ;
                float mass = control.getMass();
                
                
                control.applyForce(direction.mult(force * mass), direction);
            }
        }
    }
}

class RepulseForce extends ForceModel {

    public RepulseForce(MarkerModel markerModel) {
        super(markerModel);
    }

    @Override
    public void updateFromPhysicsThread() {
    }

    @Override
    public void updateFromSceneThread() {
        applyForce();
    }

    @Override
    void applyForce() {
    }
}