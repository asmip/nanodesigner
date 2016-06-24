/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.Vector3f;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import nanodesigner.manager.AppManager;
import nanodesigner.model.MoleculeNode;
import nanodesigner.utilities.parser.SPDBMolecule;
import nanodesigner.utilities.parser.SPDBReader;
import nanodesigner.view.ImportFileChooser;
import nanodesigner.view.MainFrame;

/**
 *
 * @author Oliver
 */
public class ImportControl {

    ImportFileChooser chooser;
    MainFrame mainFrame;
    
    
    public ImportControl(ImportFileChooser chooser, MainFrame mainFrame) {
        this.chooser = chooser;
        this.mainFrame = mainFrame;
    
    }
    
    public void acceptImport(){
        FileFilter currentFileFilter = chooser.getFileFilter();
        File saveFile = chooser.getSelectedFile();
    
        System.out.println(saveFile.getAbsolutePath());
            
        if (currentFileFilter.getDescription().equalsIgnoreCase("Protein Databank (*.pdb)")){
            SPDBReader reader = new SPDBReader(true);
            SPDBMolecule moleculeB = reader.readPDBMolecule(saveFile.getAbsolutePath());
            
            MoleculeNode molculeNode = MoleculeNode.buildMoleculeFromSPDB(moleculeB, Vector3f.ZERO);
            AppManager.sharedManager.getScene().environmentNode.addMoleculeNode(molculeNode);
        }     
    }
}
