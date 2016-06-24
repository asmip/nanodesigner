/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import nanodesigner.manager.AppManager;
import nanodesigner.model.AtomModel;
import nanodesigner.model.EnvironmentNode;
import nanodesigner.model.MoleculeNode;
import nanodesigner.view.ExplorerPanel;
import nanodesigner.view.PropertyPanel;

/**
 *
 * @author Oliver
 */
public final class ExplorerControl implements MouseListener {

    private ExplorerPanel panel;
    //private int selectedIndicies[];

    public ExplorerControl() {
    }

    public void setExplorerPanel(ExplorerPanel panel) {
        this.panel = panel;
    }

    public void populateMoleculeList() {

        if (AppManager.sharedManager.getScene().getEnvironmentNode() == null) {
            return;
        }


        EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();
        DefaultListModel model = new DefaultListModel();
        String savedGroup = "";

        int min = 0;
        int max = 190;

        Random rn = new Random();
        int red = rn.nextInt(max - min + 1) + min;
        int green = rn.nextInt(max - min + 1) + min;
        int blue = rn.nextInt(max - min + 1) + min;

        for (int i = 0; i < env.getMolecules().size(); i++) {
            MoleculeNode mol = (MoleculeNode) env.getMolecules().get(i);
            String currentGroup = mol.getChainID();
            if (!savedGroup.equalsIgnoreCase(currentGroup)) {
                savedGroup = currentGroup;
                red = rn.nextInt(max - min + 1) + min;
                green = rn.nextInt(max - min + 1) + min;
                blue = rn.nextInt(max - min + 1) + min;

            }
            String elementString = i + " " + mol.getMoleculeName() + "\t" + mol.getResidueName() + " : " + mol.getChainID();
            Object element[] = {new Font("Helvetica", Font.BOLD, 10), new Color(0.2f, 0.6f, 0.3f), null, elementString};
            model.addElement(element);
        }

        panel.getMoleculeList().setModel(model);
    }

    public void populateAtomList(MoleculeNode molecule) {
        DefaultListModel model = new DefaultListModel();

        //No molecule was selected, clear the ListModel and return.
        if (molecule == null){
            panel.getAtomList().setModel(model);
            return;
        }

        for (int i = 0; i < molecule.getAtoms().size(); i++) {
            AtomModel atom = (AtomModel) molecule.getAtoms().get(i);
            Color c = new Color(atom.getColor().r, atom.getColor().g, atom.getColor().b);
            Object element[] = {new Font("Helvetica", Font.BOLD, 10), c, null, i + " " + atom.getNameString()};
            model.addElement(element);
        }

        panel.getAtomList().setModel(model);
    }

    synchronized public void selectedMolecules(int selectedIndicies[]) {
        this.getPanel().getMoleculeList().setSelectedIndices(selectedIndicies);
        this.getPanel().getMoleculeList().ensureIndexIsVisible(selectedIndicies[0]);

        //Perhaps calculate to see if multiple fields exist. In a later version maybe.

        PropertyPanel propertyPanel = AppManager.sharedManager.getMainFrame().getPropertyPanel();
        if (propertyPanel != null) {

            propertyPanel.getMoleVectorPanel().getxTextField().setText("?");
            propertyPanel.getMoleVectorPanel().getyTextField().setText("?");
            propertyPanel.getMoleVectorPanel().getzTextField().setText("?");

            propertyPanel.getChainIDTextField().setText("?");
            propertyPanel.getResidueNameField().setText("?");
            propertyPanel.getMolRotVectorPanel().getxTextField().setText("?");
            propertyPanel.getMolRotVectorPanel().getyTextField().setText("?");
            propertyPanel.getMolRotVectorPanel().getzTextField().setText("?");
        }
    }

    synchronized public void selectedMolecule(int index) {
        //We selected a molecule(s), lets update the atom list.
        //Go back and doublecheck the molecules selected.
        this.getPanel().getMoleculeList().ensureIndexIsVisible(index);

        EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();
        final MoleculeNode mol = (MoleculeNode) env.getMolecules().get(index);
        populateAtomList(mol);

        //Update the PropertyPanel with the neccessary values.
        //The PropertyPanel may be null (because the window has been released).
        PropertyPanel propertyPanel = AppManager.sharedManager.getMainFrame().getPropertyPanel();
        
        if (propertyPanel != null) {
            String xString = Float.toString(mol.getWorldTranslation().x);
            propertyPanel.getMoleVectorPanel().getxTextField().setText(xString);
            String yString = Float.toString(mol.getWorldTranslation().y);
            propertyPanel.getMoleVectorPanel().getyTextField().setText(yString);
            String zString = Float.toString(mol.getWorldTranslation().z);
            propertyPanel.getMoleVectorPanel().getzTextField().setText(zString);
            propertyPanel.getChainIDTextField().setText(mol.getChainID());
            propertyPanel.getResidueNameField().setText(mol.getResidueName());
            propertyPanel.getMoleIsExpendableBox().setSelected(mol.isExpendable());

            //angle is in radians, convert it to degrees.

            float angles[] = mol.getWorldRotation().toAngles(null);
            DecimalFormat twoForm = new DecimalFormat("#.###");
            float rotx = Float.valueOf(twoForm.format(angles[0] * (float) (180 / Math.PI)));
            float roty = Float.valueOf(twoForm.format(angles[1] * (float) (180 / Math.PI)));
            float rotz = Float.valueOf(twoForm.format(angles[2] * (float) (180 / Math.PI)));

            propertyPanel.getMolRotVectorPanel().getxTextField().setText(Float.toString(rotx));
            propertyPanel.getMolRotVectorPanel().getyTextField().setText(Float.toString(roty));
            propertyPanel.getMolRotVectorPanel().getzTextField().setText(Float.toString(rotz));
        }

        //Update the Camera to fix itself onto the molecule
        final Scene scene = AppManager.sharedManager.getScene();
        scene.setChaseCamSpatial(mol);
    }

    public void selectedAtom(int index, int molindex) {
        this.getPanel().getMoleculeList().ensureIndexIsVisible(index);

        EnvironmentNode env = AppManager.sharedManager.getScene().getEnvironmentNode();
        MoleculeNode mol = (MoleculeNode) env.getMolecules().get(molindex);
        AtomModel atom = mol.getAtoms().get(index);

        //Update the PropertyPanel with the neccessary values.
        //The PropertyPanel may be null (because the window has been released).
        PropertyPanel propertyPanel = AppManager.sharedManager.getMainFrame().getPropertyPanel();
        
        if (propertyPanel != null) {
            String xString = Float.toString(atom.getWorldTranslation().x);
            propertyPanel.getAtomVectorPanel().getxTextField().setText(xString);
            String yString = Float.toString(atom.getWorldTranslation().y);
            propertyPanel.getAtomVectorPanel().getyTextField().setText(yString);
            String zString = Float.toString(atom.getWorldTranslation().z);
            propertyPanel.getAtomVectorPanel().getzTextField().setText(zString);

            propertyPanel.getChargeTextField().setText(atom.getCharge());
            propertyPanel.getTempFactorTextField().setText(atom.getTempfactor());
            propertyPanel.getOccupancyTextField().setText(atom.getOccupancy());
        }
    }

    public void selectedAtoms(int selectedIndicies[], int molindex) {
        this.getPanel().getAtomList().setSelectedIndices(selectedIndicies);
        this.getPanel().getAtomList().ensureIndexIsVisible(selectedIndicies[0]);

        //Perhaps calculate to see if multiple fields exist. In a later version maybe.

        PropertyPanel propertyPanel = AppManager.sharedManager.getMainFrame().getPropertyPanel();
        
        if (propertyPanel != null) {
            propertyPanel.getAtomVectorPanel().getxTextField().setText("?");
            propertyPanel.getAtomVectorPanel().getyTextField().setText("?");
            propertyPanel.getAtomVectorPanel().getzTextField().setText("?");

            propertyPanel.getChargeTextField().setText("?");
            propertyPanel.getTempFactorTextField().setText("?");
            propertyPanel.getOccupancyTextField().setText("?");
            propertyPanel.getElementComboBox().setSelectedIndex(0);
        }
    }

    public void mouseClicked(MouseEvent me) {

        JList list = (JList) me.getSource();

        //have 2 indexes for both atom and molecule

        if (list.getName().equals("MOL_LIST")) {
            if (me.getClickCount() >= 1) {
                int selectedIndicies[] = list.getSelectedIndices();
                if (selectedIndicies.length > 1) {
                    selectedMolecules(selectedIndicies);
                } else if (selectedIndicies.length == 1) {
                    selectedMolecule(selectedIndicies[0]);
                }
            }
        } else if (list.getName().equals("ATOM_LIST")) {
            if (me.getClickCount() >= 1) {
                int selectedIndicies[] = list.getSelectedIndices();
                int molIndex[] = panel.getMoleculeList().getSelectedIndices();

                if (molIndex.length == 1) {

                    if (selectedIndicies.length > 1) {
                        selectedAtoms(selectedIndicies, molIndex[0]);
                    } else if (selectedIndicies.length == 1) {
                        selectedAtom(selectedIndicies[0], molIndex[0]);
                    }
                }
                if (molIndex.length <= 0){
                    //No molecule is selected (eg it got deleted), lets clear the atomlist..
                    populateAtomList(null);
                }
            }
        }
    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public ExplorerPanel getPanel() {
        return panel;
    }

    public void setPanel(ExplorerPanel panel) {
        this.panel = panel;
    }
}
