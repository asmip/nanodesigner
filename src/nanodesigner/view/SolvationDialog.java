/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nanodesigner.control.SolvationControl;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class SolvationDialog extends JDialog {
    
    private SolvationControl control;
    private DimensionPanel dPanel;
    private JCheckBox expendableCheckBox;
    private JCheckBox usePDBInfoCheckBox;
    private JTextField chainIDTextField;
    private JTextField residueTextField;
    private JTextField distanceWTextField;
    private JTextField distanceHTextField;
    private JTextField distanceDTextField;

    private JButton waterButton, oilButton, importButton;
    
    
    public SolvationDialog(MainFrame mainFrame) {
        control = new SolvationControl(this,mainFrame);
        
        setModal(true);
        setContentPane(initContent());
        pack();
        setLocationRelativeTo(mainFrame.getMainPanel());
        setPreferredSize(new Dimension(200, 400));
        setMaximumSize(new Dimension(200, 400));
        setMinimumSize(new Dimension(200,400));
        setResizable(false);
        setVisible(true);
    }

    SolvationDialog(ActionListener aThis) {
    }

    JPanel initContent() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        //mainPanel.add(GUIUtilities.getJLabel("Environment Properties", 10));

        //First Panel        
        dPanel = new DimensionPanel(new Dimension(200, 50));
        mainPanel.add(dPanel);
        dPanel.getwTextField().setText("30");
        dPanel.gethTextField().setText("30");
        dPanel.getdTextField().setText("30");

        //Second
        JPanel propPanel = new JPanel(new GridLayout(0, 2));
        propPanel.setPreferredSize(new Dimension(200, 50));
        propPanel.setMaximumSize(new Dimension(200, 50));
        propPanel.setMinimumSize(new Dimension(200, 50));
        propPanel.add(GUIUtilities.getJLabel("Is Expendable", 10));
        propPanel.add(expendableCheckBox = new JCheckBox(""));
        expendableCheckBox.setSelected(true);
        
        propPanel.add(GUIUtilities.getJLabel("Use PDB File information", 10));
        propPanel.add(usePDBInfoCheckBox = new JCheckBox(""));
        usePDBInfoCheckBox.setSelected(false);
        usePDBInfoCheckBox.addChangeListener(control);
        usePDBInfoCheckBox.setActionCommand("UsePDBChanged");
        
        propPanel.add(GUIUtilities.getJLabel("Chain ID", 10));
        chainIDTextField = new JTextField();
        chainIDTextField.setDocument(new JTextFieldLimit(1));
        chainIDTextField.setText("B");
        propPanel.add(chainIDTextField);
        propPanel.add(GUIUtilities.getJLabel("Residue Name", 10));
        residueTextField = new JTextField();
        residueTextField.setDocument(new JTextFieldLimit(3));
        residueTextField.setText("BOX");
        propPanel.add(residueTextField);
        
        propPanel.add(GUIUtilities.getJLabel("W.Distance (a)", 10));
        propPanel.add(distanceWTextField = new JTextField("7"));
        propPanel.add(GUIUtilities.getJLabel("H.Distance (a)", 10));
        propPanel.add(distanceHTextField = new JTextField("7"));
        propPanel.add(GUIUtilities.getJLabel("D.Distance (a)", 10));
        propPanel.add(distanceDTextField = new JTextField("7"));
        mainPanel.add(propPanel);
        //Third Panel
        JPanel buttonPane = new JPanel(new GridLayout(0, 1));
        buttonPane.setPreferredSize(new Dimension(200, 50));
        buttonPane.setMaximumSize(new Dimension(200, 50));
        buttonPane.setMinimumSize(new Dimension(200, 50));
        buttonPane.add(waterButton = GUIUtilities.getJButton("Generate Water", "ACT_WATER",control, new Dimension(50, 50), null));
        buttonPane.add(oilButton = GUIUtilities.getJButton("Generate Oil", "ACT_OIL", control, new Dimension(50, 50), null));
        buttonPane.add(importButton = GUIUtilities.getJButton("Import", "ACT_IMPORT",control, new Dimension(50, 50), mainPanel));


        mainPanel.add(buttonPane);


        return mainPanel;
    }
    
    public DimensionPanel getdPanel() {
        return dPanel;
    }

    public void setdPanel(DimensionPanel dPanel) {
        this.dPanel = dPanel;
    }

    public JCheckBox getExpendableCheckBox() {
        return expendableCheckBox;
    }

    public void setExpendableCheckBox(JCheckBox expendableCheckBox) {
        this.expendableCheckBox = expendableCheckBox;
    }

    public SolvationControl getControl() {
        return control;
    }

    public void setControl(SolvationControl control) {
        this.control = control;
    }

    public JCheckBox getUsePDBInfoCheckBox() {
        return usePDBInfoCheckBox;
    }

    public void setUsePDBInfoCheckBox(JCheckBox usePDBInfoCheckBox) {
        this.usePDBInfoCheckBox = usePDBInfoCheckBox;
    }

    public JTextField getChainIDTextField() {
        return chainIDTextField;
    }

    public void setChainIDTextField(JTextField chainIDTextField) {
        this.chainIDTextField = chainIDTextField;
    }

    public JTextField getResidueTextField() {
        return residueTextField;
    }

    public void setResidueTextField(JTextField residueTextField) {
        this.residueTextField = residueTextField;
    }


    public JTextField getDistanceWTextField() {
        return distanceWTextField;
    }

    public void setDistanceWTextField(JTextField distanceWTextField) {
        this.distanceWTextField = distanceWTextField;
    }

    public JTextField getDistanceHTextField() {
        return distanceHTextField;
    }

    public void setDistanceHTextField(JTextField distanceHTextField) {
        this.distanceHTextField = distanceHTextField;
    }

    public JTextField getDistanceDTextField() {
        return distanceDTextField;
    }

    public void setDistanceDTextField(JTextField distanceDTextField) {
        this.distanceDTextField = distanceDTextField;
    }

    public JButton getWaterButton() {
        return waterButton;
    }

    public void setWaterButton(JButton waterButton) {
        this.waterButton = waterButton;
    }

    public JButton getOilButton() {
        return oilButton;
    }

    public void setOilButton(JButton oilButton) {
        this.oilButton = oilButton;
    }

    public JButton getImportButton() {
        return importButton;
    }

    public void setImportButton(JButton importButton) {
        this.importButton = importButton;
    }
    
    
    
    
}
