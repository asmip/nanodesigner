/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import java.util.ArrayList;
import nanodesigner.model.AtomModel;
import nanodesigner.model.MoleculeNode;
import nanodesigner.view.ConsoleArea;

/**
 *
 * @author Oliver
 */
public class BondControl {

    ArrayList<MoleculeNode> moleculeList = null;
    float minimumDistance = 1.0f;
    
    
    public BondControl(ArrayList<MoleculeNode> moleculeList) {
        this.moleculeList = moleculeList;
    }
    
    public void clearAllBondInformation(){
        //Clears the bond information for all molecules
        ConsoleArea.LogLn("Clearing all bond information... ");
         for (MoleculeNode mol : moleculeList){
             for (AtomModel atom : mol.getAtoms()){
                 atom.getBonds().clear();
             }
         }
        ConsoleArea.Log("Done!");
    }
    
    public void computeMoleculeScaleBonds(){
        //Computes the internal bonds of a given molecule. 
        //Does not compute bonds of molecule independant atoms.
        
        for (MoleculeNode mol : moleculeList){
            for (int i = 0; i < mol.getAtoms().size(); i++){
                for (int z = 0; z < mol.getAtoms().size(); z++){
                    if (i != z){
                        AtomModel atomA = mol.getAtoms().get(i);
                        AtomModel atomB = mol.getAtoms().get(z);
                        
                        if (atomA.getWorldTranslation().distance(atomB.getWorldTranslation()) < minimumDistance){
                            System.out.println("Found Bonds");
                            atomA.getBonds().add(atomB);
                            atomB.getBonds().add(atomA);
                        }
                    }
                }
            }
        }
    }
}
