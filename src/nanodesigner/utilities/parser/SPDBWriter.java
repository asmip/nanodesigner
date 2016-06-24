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
public class SPDBWriter {

    FileWriter fileWriter = null;
    int linecount = 0;

    /*         1         2         3         4         5         6         7         8
      12345678901234567890123456789012345678901234567890123456789012345678901234567890
      ATOM     32  N  AARG A  -3      11.281  86.699  94.383  0.50 35.88           N  
      ATOM     33  N  BARG A  -3      11.296  86.721  94.521  0.50 35.60           N
      ATOM     34  CA AARG A  -3      12.353  85.696  94.456  0.50 36.67           C
      ATOM      1  C4' ADE A   1     169.343 119.309 166.981  1.00  0.00      A    C 
      ATOM     32  H5T ADE A   1     169.483 117.274 165.698  0.00  0.00      A    H
      ATOM     33  C4' ADE A   2     179.733 117.099 221.572  1.00  0.00      A    C
     * ->   
      ATOM     20  N   BOX B   1     -10.817 -13.368  -5.657 -1111  -99.9     B    N     
      ATOM     39  H12 LIBOXG  1      -7.807 -11.994    -8.0  1.00  0.00      BOX                                                      
      ATOM     40  N   LIG B   2     -10.817 -13.368   1.343  1.00  0.00      B      
      ATOM      1  N   LIG     1     155.662   1.552   2.565  1.00  0.00      LIG 
      ATOM      2  C1  LIG     0     -10.008 -12.438  -13.45  1.00  0.00           C                                               
      ATOM   2235 C215 CNT   101       8.063   3.357  20.876  1.00  0.00           C                                                  
* 
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
     55 - 60        Real(6.2)     occupancy    Occupancy.
     61 - 66        Real(6.2)     tempFactor   Temperature  factor.
     77 - 78        LString(2)    element      Element symbol, right-justified.
     79 - 80        LString(2)    charge       Charge  on the atom.
     */
    public void writeMoleculeListToPDBFile(String filename, ArrayList<MoleculeNode> list) {
        System.out.println("Writing to PDB filename : " + filename);
        writeTextFile(filename, "REMARK generated in NanoDesigner.\n", false);

        for (int i = 0; i < list.size(); i++) {
            writeAtomListToPDBFile(filename, list.get(i), i);
        }

        writeTextFile(filename, "END", true);
    }

    private void writeAtomListToPDBFile(String filename, MoleculeNode moleculeNode, int molcount) {
        System.out.println("Writing Molecule #" + molcount + " to PDB at : " + filename);

        ArrayList<AtomModel> list = moleculeNode.getAtoms();

        int nCount = -1, cCount = -1, hCount = -1, oCount = -1;



        for (int i = 0; i < list.size(); i++) {

            //Build the PDB line by line:
            AtomModel model = list.get(i);

            int t = 0;

            if (model.getType() == AtomModel.AtomType.AtomHydrogen) {
                hCount++;
                t = hCount;
            } else if (model.getType() == AtomModel.AtomType.AtomCarbon) {
                cCount++;
                t = cCount;
            } else if (model.getType() == AtomModel.AtomType.AtomOxygen) {
                oCount++;
                t = oCount;
            } else if (model.getType() == AtomModel.AtomType.AtomNitrogen) {
                nCount++;
                t = nCount;
            }

            String recordName = "ATOM";
            String serialNumber = "" + linecount;
            String atomName;
            if (t > 0) {
                atomName = model.getSymbolString() + t;
            } else {
                atomName = model.getSymbolString();
            }
            String altLoc = "";
            String resname = moleculeNode.getResidueName();
            String chainID = moleculeNode.getChainID();
            String resSeq = "" + molcount;
            String iCode = "";
            String x = Double.toString(Math.round(model.getBatchWorldTranslation().x * 1000) / 1000.0).toString();
            String y = Double.toString(Math.round(model.getBatchWorldTranslation().y * 1000) / 1000.0).toString();
            String z = Double.toString(Math.round(model.getBatchWorldTranslation().z * 1000) / 1000.0).toString();
            String occupancy = model.getOccupancy();
            String tempfactor = model.getTempfactor();
            String element = model.getSymbolString();
            String charge = model.getCharge();


            //Create a string with 85 white spaces
            StringBuilder outputBuffer = new StringBuilder(80);
            for (int c = 0; c < 85; c++) {
                outputBuffer.append(" ");
            }

            String pdbAtomLine = outputBuffer.toString();

            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(0, recordName).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(11 - serialNumber.length(), serialNumber).toString();
            
            int offset = 0;
            if (t > 99){
                offset = 1;
            }
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(13 - offset, atomName).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(16, altLoc).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(17, resname).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(22 - chainID.length(), chainID).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(26 - resSeq.length(), resSeq).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(27, iCode).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(38 - x.length(), x).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(46 - y.length(), y).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(54 - z.length(), z).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(60 - occupancy.length(), occupancy).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(62, tempfactor).toString(); //!?//
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(72, chainID).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(77, element).toString();
            pdbAtomLine = new StringBuilder(pdbAtomLine).insert(79, charge).toString();
            pdbAtomLine = pdbAtomLine + "\n";


            writeTextFile(filename, pdbAtomLine, true);
            linecount++;
        }

    }

    private FileWriter writeTextFile(String filename, String s, boolean append) {
        BufferedWriter writer = null;

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
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                    }
                }
            }

        }
        return fileWriter;
    }
}