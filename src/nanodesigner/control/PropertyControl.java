/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nanodesigner.manager.AppManager;
import nanodesigner.model.AtomModel;
import nanodesigner.model.EnvironmentNode;
import nanodesigner.model.MoleculeNode;
import nanodesigner.view.ExplorerPanel;
import nanodesigner.view.PropertyPanel;
import nanodesigner.view.VectorPanel;

/**
 *
 * @author Oliver
 */
public class PropertyControl implements ActionListener {

    private PropertyPanel panel;

    public PropertyControl() {
    }

    public PropertyPanel getPanel() {
        return panel;
    }

    public void setPanel(PropertyPanel panel) {
        this.panel = panel;
    }

    public void updateFromSceneThread(float tpf) {
        //Here we update the property panel by retrieving the currently selected molecule in the 
        //Explorer panel and populate the contents of the vector textfields.
        ExplorerPanel explorerPanel = AppManager.sharedManager.getMainFrame().getExplorerPanel();
        int index = explorerPanel.getMoleculeList().getSelectedIndex();
        if (index < 0) {
            return;
        }
        EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();
        MoleculeNode mol = env.getMolecules().get(index);

        VectorPanel positionPanel = panel.getMoleVectorPanel();
        positionPanel.updatePanel("" + mol.getWorldTranslation().x, "" + mol.getWorldTranslation().y, "" + mol.getWorldTranslation().z);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("MOL_DELETE")) {
            ExplorerPanel explorerPanel = AppManager.sharedManager.getMainFrame().getExplorerPanel();
            if (explorerPanel != null) {
                int indicies[] = explorerPanel.getMoleculeList().getSelectedIndices();
                if (indicies.length < 0) {
                    return;
                }
                EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();

                for (int i = 0; i < indicies.length; i++) {
                    int index = indicies[i];
                    System.out.println("Deleing mol at index: " + index);
                    env.trashMolecules.add(env.molecules.get(index));
                }
                env.emptyTrash();
                explorerPanel.getControl().populateAtomList(null);
            }
        }

        if (ae.getActionCommand().equals("MOL_APPLY")) {
            ExplorerPanel explorerPanel = AppManager.sharedManager.getMainFrame().getExplorerPanel();

            if (explorerPanel != null) {
                //Now lets update this molecule in the array and refresh our GUI. 
                int indicies[] = explorerPanel.getMoleculeList().getSelectedIndices();
                if (indicies.length < 0) {
                    return;
                }

                for (int i = 0; i < indicies.length; i++) {
                    int index = indicies[i];

                    System.out.println("Applying mol at index: " + index);

                    EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();
                    MoleculeNode mol = env.getMolecules().get(index);

                    mol.setExpendable(panel.getMoleIsExpendableBox().isSelected());

                    if (!panel.getChainIDField().getText().equals("?")) {
                        mol.setChainID(panel.getChainIDTextField().getText());
                    }
                    if (!panel.getResidueNameField().getText().equals("?")) {
                        mol.setResidueName(panel.getResidueNameField().getText());
                    }

                    if (!panel.getMoleVectorPanel().getxTextField().getText().equals("?")
                            && !panel.getMoleVectorPanel().getyTextField().getText().equals("?")
                            && !panel.getMoleVectorPanel().getzTextField().getText().equals("?")) {
                        float posx = Float.parseFloat(panel.getMoleVectorPanel().getxTextField().getText());
                        float posy = Float.parseFloat(panel.getMoleVectorPanel().getyTextField().getText());
                        float posz = Float.parseFloat(panel.getMoleVectorPanel().getzTextField().getText());
                        mol.setMoleculeTranslation(new Vector3f(posx, posy, posz));
                    }

                    if (!panel.getMolRotVectorPanel().getxTextField().getText().equals("?")
                            && !panel.getMolRotVectorPanel().getyTextField().getText().equals("?")
                            && !panel.getMolRotVectorPanel().getzTextField().getText().equals("?")) {
                        float rotx = Float.parseFloat(panel.getMolRotVectorPanel().getxTextField().getText());
                        float roty = Float.parseFloat(panel.getMolRotVectorPanel().getyTextField().getText());
                        float rotz = Float.parseFloat(panel.getMolRotVectorPanel().getzTextField().getText());
                        mol.setMoleculeRotation(new Vector3f(rotx, roty, rotz));
                    }
                }

                //Refresh the Explorer GUI!
                //Reset the selected indicies after applying.
                explorerPanel.getControl().populateMoleculeList();
                explorerPanel.getMoleculeList().setSelectedIndices(indicies);
            }
        }

        if (ae.getActionCommand().equals("ATOM_APPLY")) {
            ExplorerPanel explorerPanel = AppManager.sharedManager.getMainFrame().getExplorerPanel();

            if (explorerPanel != null) {
                //Now lets update this molecule in the array and refresh our GUI. 
                int atomIndex = explorerPanel.getAtomList().getSelectedIndex();
                int molIndex = explorerPanel.getMoleculeList().getSelectedIndex();
                if (atomIndex < 0 || molIndex < 0) {
                    return;
                }
                System.out.println("Applying atom at index: " + atomIndex);

                EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();
                MoleculeNode molecule = env.getMolecules().get(molIndex);
                AtomModel atom = molecule.getAtoms().get(atomIndex);

                if (!panel.getAtomVectorPanel().getxTextField().getText().equals("?")
                        && !panel.getAtomVectorPanel().getyTextField().getText().equals("?")
                        && !panel.getAtomVectorPanel().getzTextField().getText().equals("?")) {
                    float posx = Float.parseFloat(panel.getAtomVectorPanel().getxTextField().getText());
                    float posy = Float.parseFloat(panel.getAtomVectorPanel().getyTextField().getText());
                    float posz = Float.parseFloat(panel.getAtomVectorPanel().getzTextField().getText());
                    atom.setAtomTranslation(new Vector3f(posx, posy, posz));
                }
                if (!panel.getChargeTextField().getText().equals("?")) {
                    atom.setCharge(panel.getChargeTextField().getText());
                }
                if (!panel.getTempFactorTextField().getText().equals("?")) {
                    atom.setTempfactor(panel.getTempFactorTextField().getText());
                }
                if (!panel.getOccupancyTextField().getText().equals("?")) {
                    atom.setOccupancy(panel.getOccupancyTextField().getText());
                }

                explorerPanel.getAtomList().setSelectedIndex(atomIndex);
            }
        }
    }
}
