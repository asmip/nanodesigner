/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import nanodesigner.control.ExportControl;

/**
 *
 * @author Oliver
 */
public class ExportFileChooser extends JFileChooser{
    ExportControl control;
    
    
    public ExportFileChooser(MainFrame mainFrame) {
        control = new ExportControl(this,mainFrame);
        
        FileFilter fileFilterPDB = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && file.getName().toLowerCase().endsWith(".pdb");
                }

                @Override
                public String getDescription() {
                    return "Protein Databank (*.pdb)";
                }
            };
        FileFilter fileFilterXYZ = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && file.getName().toLowerCase().endsWith(".xyz");
                }

                @Override
                public String getDescription() {
                    return "XYZ Coordinates (*.xyz)";
                }
            };
    
        addChoosableFileFilter(fileFilterPDB);
        addChoosableFileFilter(fileFilterXYZ);
        
        init();
    }

    void init(){
        int val = showSaveDialog(this);
        if (val == JFileChooser.APPROVE_OPTION){
            control.acceptSave();
        }
    }
}
