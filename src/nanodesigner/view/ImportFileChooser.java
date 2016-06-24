/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import nanodesigner.control.ImportControl;

/**
 *
 * @author Oliver
 */
public class ImportFileChooser extends JFileChooser{
    ImportControl control;
    
    
    public ImportFileChooser(MainFrame mainFrame) {
        control = new ImportControl(this,mainFrame);
        
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

        addChoosableFileFilter(fileFilterXYZ);
        addChoosableFileFilter(fileFilterPDB);

        init(); 
    }

    void init(){
        int val = showOpenDialog(this);
        if (val == JFileChooser.APPROVE_OPTION){
            control.acceptImport();
        }
    }
}
