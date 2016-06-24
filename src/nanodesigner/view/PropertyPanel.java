/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nanodesigner.control.PropertyControl;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class PropertyPanel extends JPanel {

    private VectorPanel moleVectorPanel;
    private VectorPanel atomVectorPanel;
    private VectorPanel molRotVectorPanel;
    private JTextField moleNameField;
    private JTextField chainIDField;
    private JTextField residueNameField;
    private JCheckBox moleIsExpendableBox;
    private JButton moleExportButton;
    private JButton moleDeleteButton;
    private JButton moleApplyButton;
    private PropertyControl control;
    private JTextField chargeTextField, tempFactorTextField, occupancyTextField;
    private JComboBox elementComboBox;

    public PropertyPanel(Dimension dimension) {
        this.setLayout(new BorderLayout());
        this.add(GUIUtilities.getJLabel("Properties View", 10), BorderLayout.NORTH);

        control = new PropertyControl();
        control.setPanel(this);


        Dimension panelSize = new Dimension(200, 50);

        JPanel editorPanels = new JPanel(new GridLayout(0, 1));

        JPanel molePanel = new JPanel(new BorderLayout());
        molePanel.setPreferredSize(panelSize);
        molePanel.add(GUIUtilities.getJLabel("Molecule Properties", 10), BorderLayout.NORTH);

        JPanel moleVPanel = new JPanel(new GridLayout(2, 1));

        moleVPanel.add(moleVectorPanel = new VectorPanel(control, "MOL", panelSize));
        moleVPanel.add(molRotVectorPanel = new VectorPanel(control, "MOLANG", panelSize));
        molRotVectorPanel.getxLabel().setText("φ(X˚)");
        molRotVectorPanel.getyLabel().setText("φ(Y˚)");
        molRotVectorPanel.getzLabel().setText("φ(Z˚)");


        molePanel.add(moleVPanel, BorderLayout.CENTER);

        JPanel molePropertyParentPanel = new JPanel(new BorderLayout());

        JPanel molePropertyPanel = new JPanel(new GridLayout(0, 2));
        // molePropertyPanel.add(GUIUtilities.getJLabel("Molecule Name", 10));
        // moleNameField = new JTextField("");
        // molePropertyPanel.add(moleNameField);

        molePropertyPanel.add(GUIUtilities.getJLabel("ChainID ", 10));
        chainIDField = new JTextField("");
        chainIDField.setDocument(new JTextFieldLimit(1));
        molePropertyPanel.add(chainIDField);

        molePropertyPanel.add(GUIUtilities.getJLabel("Residue Name ", 10));
        residueNameField = new JTextField("");
        residueNameField.setDocument(new JTextFieldLimit(3));
        molePropertyPanel.add(residueNameField);

        molePropertyPanel.add(GUIUtilities.getJLabel("Is Expendable", 10));
        molePropertyPanel.add(moleIsExpendableBox = new JCheckBox(""));
        molePropertyPanel.add(moleExportButton = GUIUtilities.getJButton("Export", null, null, null, null));
        moleExportButton.setEnabled(false);
        molePropertyPanel.add(moleDeleteButton = GUIUtilities.getJButton("Delete", "MOL_DELETE", control, null, null));


        molePropertyParentPanel.add(molePropertyPanel, BorderLayout.NORTH);
        molePropertyParentPanel.add(GUIUtilities.getJButton("Apply", "MOL_APPLY", control, null, null), BorderLayout.CENTER);


        molePanel.add(molePropertyParentPanel, BorderLayout.SOUTH);

        editorPanels.add(molePanel);


        JPanel atomPanel = new JPanel(new BorderLayout());
        atomPanel.setPreferredSize(panelSize);
        atomPanel.add(GUIUtilities.getJLabel("Atomic Properties", 10), BorderLayout.NORTH);
        atomPanel.add(atomVectorPanel = new VectorPanel(control, "ATOM", panelSize), BorderLayout.CENTER);
        atomVectorPanel.setIsEditable(false);
       

        JPanel boxPanel = new JPanel(new GridLayout(0, 2));
       
        boxPanel.add(GUIUtilities.getJLabel("Atomic Element", 10));
        String atomsStr[] = {"","Hydrogen", "Carbon", "Nitrogen", "Oxygen"};
        elementComboBox = new JComboBox(atomsStr);
        boxPanel.add(elementComboBox);        
        
        boxPanel.add(GUIUtilities.getJLabel("Atomic Charge", 10));
        chargeTextField = new JTextField("");
        chargeTextField.setDocument(new JTextFieldLimit(2));
        boxPanel.add(chargeTextField);
        
       
        boxPanel.add(GUIUtilities.getJLabel("Temp. Factor", 10));
        tempFactorTextField = new JTextField("");
        tempFactorTextField.setDocument(new JTextFieldLimit(5));
        boxPanel.add(tempFactorTextField);

        boxPanel.add(GUIUtilities.getJLabel("Occupancy", 10));
        occupancyTextField = new JTextField("");
        occupancyTextField.setDocument(new JTextFieldLimit(5));
        boxPanel.add(occupancyTextField);

        boxPanel.add(GUIUtilities.getJButton("Apply" , "ATOM_APPLY", control, null, null));
        boxPanel.add(GUIUtilities.getJButton("Delete", "ATOM_DELETE", control, null, null));

        atomPanel.add(boxPanel, BorderLayout.SOUTH);

        editorPanels.add(atomPanel);



        this.add(editorPanels, BorderLayout.CENTER);
    }

    public void updatePropertyPanelFromSceneThread(float tpf) {
        control.updateFromSceneThread(tpf);
    }

    public VectorPanel getMolRotVectorPanel() {
        return molRotVectorPanel;
    }

    public void setMolRotVectorPanel(VectorPanel molRotVectorPanel) {
        this.molRotVectorPanel = molRotVectorPanel;
    }

    public JButton getMoleApplyButton() {
        return moleApplyButton;
    }

    public void setMoleApplyButton(JButton moleApplyButton) {
        this.moleApplyButton = moleApplyButton;
    }

    public VectorPanel getMoleVectorPanel() {
        return moleVectorPanel;
    }

    public void setMoleVectorPanel(VectorPanel moleVectorPanel) {
        this.moleVectorPanel = moleVectorPanel;
    }

    public VectorPanel getAtomVectorPanel() {
        return atomVectorPanel;
    }

    public void setAtomVectorPanel(VectorPanel atomVectorPanel) {
        this.atomVectorPanel = atomVectorPanel;
    }

    public JTextField getMoleNameField() {
        return moleNameField;
    }

    public void setMoleNameField(JTextField moleNameField) {
        this.moleNameField = moleNameField;
    }

    public JTextField getChainIDTextField() {
        return chainIDField;
    }

    public void setMoleGroupField(JTextField moleGroupField) {
        this.chainIDField = moleGroupField;
    }

    public JCheckBox getMoleIsExpendableBox() {
        return moleIsExpendableBox;
    }

    public void setMoleIsExpendableBox(JCheckBox moleIsExpendableBox) {
        this.moleIsExpendableBox = moleIsExpendableBox;
    }

    public JButton getMoleExportButton() {
        return moleExportButton;
    }

    public void setMoleExportButton(JButton moleExportButton) {
        this.moleExportButton = moleExportButton;
    }

    public JButton getMoleDeleteButton() {
        return moleDeleteButton;
    }

    public void setMoleDeleteButton(JButton moleDeleteButton) {
        this.moleDeleteButton = moleDeleteButton;
    }

    public JTextField getChainIDField() {
        return chainIDField;
    }

    public void setChainIDField(JTextField chainIDField) {
        this.chainIDField = chainIDField;
    }

    public JTextField getResidueNameField() {
        return residueNameField;
    }

    public void setResidueNameField(JTextField residueNameField) {
        this.residueNameField = residueNameField;
    }

    public PropertyControl getControl() {
        return control;
    }

    public void setControl(PropertyControl control) {
        this.control = control;
    }

    public JTextField getChargeTextField() {
        return chargeTextField;
    }

    public void setChargeTextField(JTextField chargeTextField) {
        this.chargeTextField = chargeTextField;
    }

    public JTextField getTempFactorTextField() {
        return tempFactorTextField;
    }

    public void setTempFactorTextField(JTextField tempFactorTextField) {
        this.tempFactorTextField = tempFactorTextField;
    }

    public JTextField getOccupancyTextField() {
        return occupancyTextField;
    }

    public void setOccupancyTextField(JTextField occupancyTextField) {
        this.occupancyTextField = occupancyTextField;
    }

    public JComboBox getElementComboBox() {
        return elementComboBox;
    }

    public void setElementComboBox(JComboBox elementComboBox) {
        this.elementComboBox = elementComboBox;
    }
    
    
}
