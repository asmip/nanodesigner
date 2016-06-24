/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import nanodesigner.model.AtomModel;
import nanodesigner.model.MoleculeNode;

/**
 *
 * @author Oliver
 */
public class SXYZWriter {
   
    public SXYZWriter() {
    }

    public void writeMoleculeListToFile(String filename, ArrayList<MoleculeNode> list) {
        System.out.println("Writing to filename : " + filename);
        
        parseRequirements(filename,list);
        
        for (int i = 0; i < list.size(); i++) {
            MoleculeNode mol = list.get(i);
            writeAtomListToFile(filename, mol.getAtoms(),i);
        }
    }
    
    private void parseRequirements(String filename,ArrayList<MoleculeNode> list){
        //Parses the requirements of the xyz:
        int c=0;
        for (int i = 0; i < list.size(); i++) {
            MoleculeNode mol = list.get(i);
            for (int z = 0; z < mol.getAtoms().size(); z++){
                c++;
            }
            
        }
        
        String cString = ""+c+"\n";
        writeTextFile(filename, cString, true);
        writeTextFile(filename, " XYZ Generated in NanoDesigner.\n", true);   
    }

    private void writeAtomListToFile(String filename, ArrayList<AtomModel> list,int molcount) {
        System.out.println("Writing to filename : " + filename);
        for (int i = 0; i < list.size(); i++) {
            AtomModel atomModel = list.get(i);
            double newX = Math.round(atomModel.getWorldTranslation().x*100000.0)/100000.0;
            double newY = Math.round(atomModel.getWorldTranslation().y*100000.0)/100000.0;
            double newZ = Math.round(atomModel.getWorldTranslation().z*100000.0)/100000.0;
            String line = " " + atomModel.getParseNameString()+"_"+molcount + "\t\t" + newX + "\t\t" + newY + "\t\t" + newZ + "\n";
            writeTextFile(filename, line, true);
        }
    }

    private FileWriter writeTextFile(String filename, String s, boolean append) {
        BufferedWriter writer = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename, append);
        } catch (IOException e1) {
        }

        try {
            writer = new BufferedWriter(fileWriter);
            writer.write(s);
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                }

            }
        }
        return fileWriter;
    }
}
