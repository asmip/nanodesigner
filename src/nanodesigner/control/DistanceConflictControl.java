/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import nanodesigner.manager.AppManager;
import nanodesigner.model.AtomModel;
import nanodesigner.model.EnvironmentNode;
import nanodesigner.model.MoleculeNode;

/**
 *
 * @author Oliver
 *
 *
 * ConflictFinder will scan all the molecules and move them if the minimum
 * distance between molecules is not achieved. This is to prevent VMD from
 * loading bad bonds with its auto-bond feature.
 */
public class DistanceConflictControl {

    ArrayList<MoleculeNode> list = null;
    EnvironmentNode envNode = null;

    public DistanceConflictControl(ArrayList<MoleculeNode> moleculeList) {
       this.list = moleculeList;    //Does not retain ownership/
       envNode = AppManager.getSharedManager().getScene().getEnvironmentNode();
    }
    
    public boolean checkForConflicts() {
        boolean conflict = false;
        
        //Set the state of conflicts off (if already on).
        for (MoleculeNode mol : list){
            mol.setConflicted(false);
        }
        
        
        for (int a = 0; a < list.size(); a++) {
            MoleculeNode molA = (MoleculeNode) list.get(a);
            
            for (int b = 0; b < list.size(); b++) {
                MoleculeNode molB = (MoleculeNode) list.get(b);
                
                if (a != b) {
                    //Scan through the molecules atoms

                    atomSearch:
                    {
                        for (AtomModel atomA : molA.getAtoms()) {
                            for (AtomModel atomB : molB.getAtoms()) {
                                //Check the distance between each atom ensuring minimum safe distance for VMD.
                                conflict = checkMinimumSafeDistance(atomA, atomB);
                                if (conflict) {
                                    //We know theres a conflict, no point to continue scanning
                                    molA.setConflicted(true);
                                    molB.setConflicted(true);
                                    Vector3f[] vectors = {atomA.getBatchWorldTranslation(),atomB.getBatchWorldTranslation()};
                                    envNode.addLineBetweenVectors(vectors);

                                    break atomSearch;
                                }
                            }
                        }
                    }                   
                }
            }
        }

        return conflict;
    }

    private boolean checkMinimumSafeDistance(AtomModel atomA, AtomModel atomB) {
        double lhs = 1.0f;        
        double rhs = atomA.getBatchWorldTranslation().distance(atomB.getBatchWorldTranslation());

        if (lhs >= rhs) {
            System.out.println("AtomA: " + atomA.getBatchWorldTranslation() + " AtomB: " + atomB.getBatchWorldTranslation());
            System.out.println("lhs: " + lhs + " rhs: " + rhs + "AtomA: " + atomA.getSymbolString() + " AtomB: " + atomB.getSymbolString());
            return true;
        }
        return false;
    }
}