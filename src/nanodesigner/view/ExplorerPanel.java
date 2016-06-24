/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import nanodesigner.control.ExplorerControl;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class ExplorerPanel extends JPanel{

    private JList moleculeList = null;
    private JList atomList = null;
    private ExplorerControl control = null;
    
    public ExplorerPanel(ExplorerControl control, Dimension dimension) {
        this.control = control;
        control.setExplorerPanel(this);
        
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setLayout(new BorderLayout());

        add(GUIUtilities.getJLabel("Explorer View", 10),BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
                
        JPanel moleculePanel = new JPanel(new BorderLayout());
        moleculePanel.add(GUIUtilities.getJLabel("Molecule Explorer", 10),BorderLayout.NORTH);
        moleculeList = new JList();
        moleculeList.setName("MOL_LIST");
        moleculeList.setCellRenderer(new ComplexCellRenderer());
        moleculeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
       
        moleculeList.addMouseListener(control);
        JScrollPane moleculeScrollPane = new JScrollPane(moleculeList);
        moleculeScrollPane.setPreferredSize(new Dimension(200,200));
        moleculeScrollPane.setMaximumSize(new Dimension(200,200));
        moleculeScrollPane.setSize(200, 200);
        moleculePanel.add(moleculeScrollPane,BorderLayout.CENTER);
        
        
        JPanel atomPanel = new JPanel(new BorderLayout());
        atomPanel.add(GUIUtilities.getJLabel("Atomic Explorer", 10),BorderLayout.NORTH);
        atomList = new JList();
        atomList.setName("ATOM_LIST");
        atomList.setCellRenderer(new ComplexCellRenderer());
        atomList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        atomList.addMouseListener(control);
     
        JScrollPane atomScrollPane = new JScrollPane(atomList);
        atomScrollPane.setPreferredSize(new Dimension(200,200));
        atomScrollPane.setMaximumSize(new Dimension(200,200));
        atomScrollPane.setSize(200, 200);
        atomPanel.add(atomScrollPane,BorderLayout.CENTER);
       

        listPanel.add(moleculePanel);
        listPanel.add(atomPanel);
        
        add(listPanel,BorderLayout.CENTER);
        
        control.populateMoleculeList();
        
    }

    public ExplorerControl getControl() {
        return control;
    }

    public void setControl(ExplorerControl control) {
        this.control = control;
    }

    public JList getMoleculeList() {
        return moleculeList;
    }

    public void setMoleculeList(JList moleculeList) {
        this.moleculeList = moleculeList;
    }

    public JList getAtomList() {
        return atomList;
    }

    public void setAtomList(JList atomList) {
        this.atomList = atomList;
    }
    
    
    
    
    
    
}
