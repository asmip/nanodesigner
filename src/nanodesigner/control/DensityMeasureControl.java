/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.Vector3f;
import nanodesigner.manager.AppManager;
import nanodesigner.model.AtomModel;
import nanodesigner.model.EnvironmentNode;
import nanodesigner.model.MoleculeNode;
import nanodesigner.view.ConsoleArea;

/**
 *
 * @author Oliver
 */
public class DensityMeasureControl {

    Scene scene;
    EnvironmentNode environmentNode;
    static float DEFAULT_MIN = 99999999;
    static float DEFAULT_MAX = -99999999;

    public DensityMeasureControl() {
        scene = AppManager.getSharedManager().getScene();
        environmentNode = scene.getEnvironmentNode();

        computeDensity();

    }
    
    private void computeDensity(){
        Vector3f mixmax[] = measureMinMax();
        double volume = calculateVolumeCm(mixmax); //cm^3
        double weightAMU = calculateAtomicWeight();
        double weightGram = convertAMUWeightToGrams(weightAMU);
        
        double density = weightGram / volume; 
        ConsoleArea.LogLn("Measure: System Density: " + density + " g/cm^3");
    } 

    public double calculateVolumeCm(Vector3f[] minMax) {
        Vector3f min = minMax[0];
        Vector3f max = minMax[1];
        Vector3f delta = new Vector3f(Math.abs(min.x),Math.abs(min.y),Math.abs(min.z)).add(
                                      Math.abs(max.x),Math.abs(max.y),Math.abs(max.z));
        System.out.println("Delta : " + delta);
        
        double volumeAngst = delta.x * delta.y * delta.z;
        double volume = volumeAngst * 1E-24;
        ConsoleArea.LogLn("Measure: System Volume: " + volume + " cm^3");
        return volume;
    }

    public double convertAMUWeightToGrams(double weightAMU){
        //double unit = 1.66*10.0^-24.0;
        
        double unit = 1.66053892173e-24;
        double weightGram = unit * weightAMU;
        ConsoleArea.LogLn("Measure: System Mass: " + weightGram + " g");
        return weightGram;
        
    }
    
    public double calculateAtomicWeight() {
        double totalWeightAMU = 0.0;

        for (int i = 0; i < environmentNode.molecules.size(); i++) {
            MoleculeNode moleculeNode = (MoleculeNode) environmentNode.getMolecules().get(i);

            for (int z = 0; z < moleculeNode.getAtoms().size(); z++) {
                AtomModel atomModel = (AtomModel) moleculeNode.getAtoms().get(z);
                System.out.println("Atom: " + atomModel.getSymbolString() + " AMU: " + atomModel.getAtomMassAmu());
                
                totalWeightAMU += atomModel.getAtomMassAmu();
            }
        }
        System.out.println("Total Weight (AMU): " + totalWeightAMU);
        return totalWeightAMU;
    }

    public Vector3f[] measureMinMax() {

    
        float minX = DEFAULT_MIN, minY = DEFAULT_MIN, minZ = DEFAULT_MIN;
        float maxX = DEFAULT_MAX, maxY = DEFAULT_MAX, maxZ = DEFAULT_MAX;
        
        for (int i = 0; i < environmentNode.molecules.size(); i++) {
            //Loop through and find the lowest X coordinate that is an atoms position(x).
            MoleculeNode moleculeNode = (MoleculeNode) environmentNode.getMolecules().get(i);

            for (int z = 0; z < moleculeNode.getAtoms().size(); z++) {
                AtomModel atomModel = (AtomModel) moleculeNode.getAtoms().get(z);

                float curX = atomModel.getBatchWorldTranslation().x;//atomModel.getBatchWorldTranslation().x;
                float curY = atomModel.getBatchWorldTranslation().y;//atomModel.getBatchWorldTranslation().y;
                float curZ = atomModel.getBatchWorldTranslation().z;//atomModel.getBatchWorldTranslation().z;

                if (curX < minX) {
                    minX = curX;
                }
                if (curX > maxX) {
                    maxX = curX;
                }
                if (curY < minY) {
                    minY = curY;
                }
                if (curY > maxY) {
                    maxY = curY;
                }
                if (curZ < minZ) {
                    minZ = curZ;
                }
                if (curZ > maxZ) {
                    maxZ = curZ;
                }
            }
        }
        
        ConsoleArea.LogLn("Measure: System MinMax: {" + minX + " " + minY + " " + minZ + "} , {" + maxX + " " + maxY + " " + maxZ + "}");

        Vector3f min = new Vector3f(minX, minY, minZ);
        Vector3f max = new Vector3f(maxX, maxY, maxZ);

        Vector3f minmax[] = {min, max};
        return minmax;
    }
}
