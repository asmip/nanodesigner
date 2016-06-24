/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.Vector3f;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import nanodesigner.manager.AppManager;
import nanodesigner.model.MoleculeNode;
import nanodesigner.utilities.parser.SPDBMolecule;
import nanodesigner.utilities.parser.SPDBReader;
import nanodesigner.view.ConsoleArea;
import nanodesigner.view.MainFrame;
import nanodesigner.view.SolvationDialog;

/**
 *
 * @author Oliver
 */
public class SolvationControl implements ActionListener, ChangeListener {

    MainFrame mainFrame;
    SolvationDialog dialog;

    public SolvationControl(SolvationDialog dialog, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dialog = dialog;
    }

    public void actionPerformed(ActionEvent ae) {


        if (ae.getActionCommand().equals("ACT_IMPORT")) {

            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && file.getName().toLowerCase().endsWith(".pdb");
                }

                @Override
                public String getDescription() {
                    return "Protein Databank (*.pdb)";
                }
            };

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(fileFilter);


            int option = fileChooser.showOpenDialog(mainFrame);

            if (option == JFileChooser.APPROVE_OPTION) {
                String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                ConsoleArea.LogLn("Solvating with Molecule from: " + filepath);

                SPDBReader reader = new SPDBReader(true);
                SPDBMolecule molecule = reader.readPDBMolecule(filepath);

                float distanceW = Float.parseFloat(dialog.getDistanceWTextField().getText());
                float distanceH = Float.parseFloat(dialog.getDistanceHTextField().getText());
                float distanceD = Float.parseFloat(dialog.getDistanceDTextField().getText());

                boolean isExpendable = dialog.getExpendableCheckBox().isSelected();
                String chainID = "";
                String residueName = "";
                if (!dialog.getUsePDBInfoCheckBox().isSelected()){
                    chainID = dialog.getChainIDTextField().getText();
                    residueName = dialog.getResidueTextField().getText();
                                        System.out.println("YES" + residueName);

                }
                else{
                    chainID = molecule.getAtoms().get(0).getChainId();
                    residueName = molecule.getAtoms().get(0).getResName();
                }
                
                
                float x = Float.parseFloat(dialog.getdPanel().getxTextField().getText());
                float y = Float.parseFloat(dialog.getdPanel().getyTextField().getText());
                float z = Float.parseFloat(dialog.getdPanel().getzTextField().getText());
                float w = Float.parseFloat(dialog.getdPanel().getwTextField().getText());
                float h = Float.parseFloat(dialog.getdPanel().gethTextField().getText());
                float d = Float.parseFloat(dialog.getdPanel().getdTextField().getText());


                initBoxWithSPDBMolecule(x, y, z, w, h, d, molecule, distanceW, distanceH, distanceD, isExpendable, chainID, residueName);

                //Resign it
                dialog.dispose();
            }
        }


    }

    synchronized private void initBoxWithSPDBMolecule(float x, float y, float z, float w, float h, float d, SPDBMolecule spdbMolecule, float distanceW, float distanceH, float distanceD, boolean isExpendable, String segmentID, String residueName) {
        final Scene scene = AppManager.sharedManager.getScene();


        float nX = w / distanceW;
        float nY = h / distanceH;
        float nZ = d / distanceD;
        
        
        int count = 0;
          for (int xc = 0; xc < nX; xc++) {
            for (int yc = 0; yc < nY; yc++) {
                for (int zc = 0; zc < nZ; zc++) {
                    count++;
                }
            }
          }
        ConsoleArea.LogLn("Generating " + count + " Molecules... ");

        AppManager.getSharedManager().getMainFrame().addLoadingPanel(count);
  
          
        for (int xc = 0; xc < nX; xc++) {
            for (int yc = 0; yc < nY; yc++) {
                for (int zc = 0; zc < nZ; zc++) {
                    Vector3f molpos = new Vector3f(xc * distanceW - w / 2 + x, yc * distanceH - h / 2 + y, zc * distanceD - d / 2 + z);
                    System.out.println(molpos);
                    MoleculeNode moleculeNode = MoleculeNode.buildMoleculeFromSPDB(spdbMolecule, molpos);
                    
                    moleculeNode.setExpendable(isExpendable);
                    moleculeNode.setChainID(segmentID);
                    moleculeNode.setResidueName(residueName);
                    scene.getEnvironmentNode().addMoleculeNode(moleculeNode);
                }
            }
        }

        ConsoleArea.Log("Done!");
    }

    public void stateChanged(ChangeEvent ce) {
        JCheckBox checkBox = (JCheckBox) ce.getSource();
        if (checkBox.getActionCommand().equals("UsePDBChanged")){
            if (checkBox.isSelected()){
                dialog.getChainIDTextField().setEnabled(false);
                dialog.getResidueTextField().setEnabled(false);
            }
            else{
                 dialog.getChainIDTextField().setEnabled(true);
                 dialog.getResidueTextField().setEnabled(true);
            }
        }
    }
}
