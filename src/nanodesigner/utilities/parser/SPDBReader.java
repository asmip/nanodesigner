/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Oliver
 */

/*         1         2         3         4         5         6         7         8
 12345678901234567890123456789012345678901234567890123456789012345678901234567890
 ATOM     32  N  AARG A  -3      11.281  86.699  94.383  0.50 35.88           N  
 ATOM     33  N  BARG A  -3      11.296  86.721  94.521  0.50 35.60           N
 ATOM     34  CA AARG A  -3      12.353  85.696  94.456  0.50 36.67           C
 ATOM      1  C4' ADE A   1     169.343 119.309 166.981  1.00  0.00      A    C 
 ATOM     12  C           1      -0.039  -0.182   0.294
 */
/*COLUMNS        DATA  TYPE    FIELD        DEFINITION
 -------------------------------------------------------------------------------------
 1 -  6        Record name   "ATOM  "
 7 - 11        Integer       serial       Atom  serial number.
 13 - 16        Atom          name         Atom name.
 17             Character     altLoc       Alternate location indicator.
 18 - 20        Residue name  resName      Residue name.
 22             Character     chainID      Chain identifier.
 23 - 26        Integer       resSeq       Residue sequence number.
 27             AChar         iCode        Code for insertion of residues.
 31 - 38        Real(8.3)     x            Orthogonal coordinates for X in Angstroms.
 39 - 46        Real(8.3)     y            Orthogonal coordinates for Y in Angstroms.
 47 - 54        Real(8.3)     z            Orthogonal coordinates for Z in Angstroms.
 55 - 60        RDeal(6.2)     occupancy    Occupancy.
 61 - 66        Real(6.2)     tempFactor   Temperature  factor.
 77 - 78        LString(2)    element      Element symbol, right-justified.
 79 - 80        LString(2)    charge       Charge  on the atom.
 */
public class SPDBReader {

    boolean isNormalised;

    public SPDBReader() {
    }

    public SPDBReader(boolean isNormalised) {
        this.isNormalised = isNormalised;
    }

    public SPDBMolecule readPDBMolecule(String fileString) {
       
        SPDBMolecule molecule = new SPDBMolecule();
        
        try {
            File file = new File(fileString);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            molecule.setFileName(file.getName());
            molecule.setName(file.getName());
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() > 6) {
                    String recordName = line.substring(0, 6);
                    recordName = recordName.replaceAll("\\s", "");
                    if (recordName.contains("ATOM")) {
                        String serial = line.substring(6, 11).replaceAll("\\s", "");
                        String name = line.substring(12, 16).replaceAll("\\s", "");
                        String altLoc = line.substring(16, 17).replaceAll("\\s", "");
                        String resName = line.substring(17, 20).replaceAll("\\s", "");
                        String chainID = line.substring(20, 22).replaceAll("\\s", "");
                        String resSeq = line.substring(22, 26).replaceAll("\\s", "");
                        String iCode = line.substring(26, 27).replaceAll("\\s", "");
                        String x = line.substring(30, 38).replaceAll("\\s", "");
                        String y = line.substring(38, 46).replaceAll("\\s", "");
                        String z = line.substring(46, 54).replaceAll("\\s", "");
                        String occupancy = "";
                        if (line.length() > 60){
                            occupancy = line.substring(54, 60).replaceAll("\\s", "");
                        }
                        String tempFactor = "";
                        if (line.length() > 66){
                           tempFactor = line.substring(60, 66).replaceAll("\\s", "");
                        }
                        //CHARGE and ELEMENT?

                        //Now build the atom and add it to the molecule
                        float xf = Float.parseFloat(x);
                        float yf = Float.parseFloat(y);
                        float zf = Float.parseFloat(z);
                        molecule.getAtoms().add(new SPDBAtom(serial, name, altLoc, resName, chainID, resSeq, iCode, occupancy, tempFactor, chainID, xf, yf, zf));
                    }
                }
            }
            fileReader.close();
            
            calculateLengths(molecule);
            moveToOrigin(molecule);
            centerAtOrigin(molecule);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return molecule;
    }

    private void calculateLengths(SPDBMolecule molecule) {
        System.out.println("Calculating Molecule Lengths");

        float minX = 99999, minY = 99999, minZ = 99999;
        float maxX =-99999, maxY =-99999, maxZ =-99999;

        //get mix and max values
        for (int i = 0; i < molecule.getAtoms().size(); i++) {
            SPDBAtom atom = molecule.getAtoms().get(i);

            float cX, cY, cZ;
            cX = atom.getX();
            cY = atom.getY();
            cZ = atom.getZ();

            //minimum
            if (cX < minX) {
                minX = cX;
            }
            if (cY < minY) {
                minY = cY;
            }
            if (cZ < minZ) {
                minZ = cZ;
            }
            //maximum
            if (cX > maxX) {
                maxX = cX;
            }
            if (cY > maxY) {
                maxY = cY;
            }
            if (cZ > maxZ) {
                maxZ = cZ;
            }
        }
        molecule.maxX = maxX;
        molecule.minX = minX;
        molecule.maxY = maxY;
        molecule.minY = minY;
        molecule.maxZ = maxZ;
        molecule.minZ = minZ;
        
       
        molecule.dX = maxX-minX;
        molecule.dY = maxY-minY;
        molecule.dZ = maxZ-minZ;
        System.out.println("Molecule MinX " + molecule.minX + " MaxX " + molecule.maxX);
        System.out.println("Molecule MinY " + molecule.minY + " MaxX " + molecule.maxY);
        System.out.println("Molecule MinZ " + molecule.minZ + " MaxX " + molecule.maxZ);
     
        System.out.println("Molecule Lengths: " + molecule.dX + " " + molecule.dY + " " + molecule.dZ);
    }
    private void moveToOrigin(SPDBMolecule molecule) {
        
        for (int i = 0; i < molecule.getAtoms().size(); i++) {
            SPDBAtom atom = (SPDBAtom) molecule.getAtoms().get(i);
            atom.setX(atom.getX() - molecule.minX);
            atom.setY(atom.getY() - molecule.minY);
            atom.setZ(atom.getZ() - molecule.minZ);
        }
    }
    private void centerAtOrigin(SPDBMolecule molecule) {
       for (int i = 0; i < molecule.getAtoms().size(); i++) {
            SPDBAtom atom = (SPDBAtom) molecule.getAtoms().get(i);
            //We are at {0,0,0}
            atom.setX(atom.getX() - molecule.dX/2);
            atom.setY(atom.getY() - molecule.dY/2);
            atom.setZ(atom.getZ() - molecule.dZ/2);
        }
    }
}
