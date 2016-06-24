/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities.parser;

import java.util.ArrayList;

/**
 *
 * @author Oliver
 */
public class SPDBMolecule {

    private ArrayList<SPDBAtom> atoms = new ArrayList<SPDBAtom>();
    private String fileName;
    private String name;
    float maxX = 0, minX = 0, dX = 0;
    float maxY = 0, minY = 0, dY = 0;
    float maxZ = 0, minZ = 0, dZ = 0;

    public ArrayList<SPDBAtom> getAtoms() {
        return atoms;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
