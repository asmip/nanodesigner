/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import nanodesigner.manager.AppManager;
import nanodesigner.utilities.parser.SPDBWriter;
import nanodesigner.utilities.parser.SXYZWriter;
import nanodesigner.view.ExportFileChooser;
import nanodesigner.view.MainFrame;

/**
 *
 * @author Oliver
 */
public class ExportControl{

    ExportFileChooser dialog;
    MainFrame mainFrame;
    
    public ExportControl(ExportFileChooser dialog, MainFrame mainFrame) {
        this.dialog = dialog;
        this.mainFrame = mainFrame;
    }

    public void acceptSave() {
        FileFilter currentFileFilter = dialog.getFileFilter();
        File saveFile = dialog.getSelectedFile();    
        System.out.println(saveFile.getAbsolutePath());
    
        if (currentFileFilter.getDescription().equalsIgnoreCase("XYZ Coordinates (*.xyz)")){
            SXYZWriter xyzWriter = new SXYZWriter();
            
            
            xyzWriter.writeMoleculeListToFile(saveFile.getAbsolutePath(), AppManager.getSharedManager().getScene().getEnvironmentNode().molecules);
        }
        else if (currentFileFilter.getDescription().equalsIgnoreCase("Protein Databank (*.pdb)")){
            SPDBWriter spdbWriter = new SPDBWriter();
            spdbWriter.writeMoleculeListToPDBFile(saveFile.getAbsolutePath(),  AppManager.getSharedManager().getScene().getEnvironmentNode().molecules);
        }
    }
}
