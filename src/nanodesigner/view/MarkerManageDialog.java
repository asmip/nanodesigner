/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import nanodesigner.control.MarkerManageControl;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class MarkerManageDialog extends JDialog{
        public enum MarkerColor{
            Blue,
            Green,
            Red,
            Magenta,
            Orange
        }
        public enum MarkerShape{
            Cube,
            Cylinder,
            Sphere,
            Dome,
            Torus
        }
    
        private MarkerManageControl control = null;
        private JList markerList = null;
        private VectorPanel markerVectorPanel = null;
        private JTextField markerIDField = null;
        private JComboBox colorBox = null;
        private JComboBox shapeBox = null;
        private JComboBox forceBox = null;
        private JComboBox segmentBox = null;
        
        MarkerManageDialog() {
        control = new MarkerManageControl(this);
        
        setModal(false);
        setContentPane(initContent());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     
        setLocationRelativeTo(AppManager.sharedManager.getMainFrame().getMainPanel());
        setLocation(new Point(100, 50));
        setPreferredSize(new Dimension(260, 390));
        setMaximumSize(new Dimension(260, 390));
        setMinimumSize(new Dimension(260, 390));
        setResizable(false);
        setVisible(true);
        setAlwaysOnTop(true);
    }
        
        
        private JPanel initContent(){
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(GUIUtilities.getJLabel("Marker Manager", 12), BorderLayout.NORTH);
    
            JPanel divPanel = new JPanel(new GridLayout(1,2));
            JPanel leftParentPanel = new JPanel (new BorderLayout());
            
            markerVectorPanel = new VectorPanel(control, "MARKER", new Dimension(90, 45));
            leftParentPanel.add(markerVectorPanel,BorderLayout.NORTH);
                    
            JPanel leftPanel = new JPanel(new GridLayout(0,1));
            leftPanel.add(GUIUtilities.getJLabel("Unique Identifier", 10));
            markerIDField = new JTextField("identifier");
            leftPanel.add(markerIDField);
            
            leftPanel.add(GUIUtilities.getJLabel("Color", 10));
            String colors[] = {"Blue", "Green", "Red", "Magenta", "Orange"};
            colorBox = new JComboBox(colors);
            colorBox.setSelectedIndex(2);
            leftPanel.add(colorBox);
                         
            leftPanel.add(GUIUtilities.getJLabel("Shape", 10));
            String shapes[] = {"Cube", "Cylinder", "Sphere", "Dome", "Torus"};
            shapeBox = new JComboBox(shapes);
            shapeBox.setSelectedIndex(0);
            leftPanel.add(shapeBox);
            
            leftPanel.add(GUIUtilities.getJLabel("Assign Segment", 10));
            ArrayList<String> segs = AppManager.getSharedManager().getScene().getEnvironmentNode().getSegmentStrings();
            segs.add(0, "ALL");
            segmentBox = new JComboBox(segs.toArray());
            leftPanel.add(segmentBox);
            
            leftPanel.add(GUIUtilities.getJLabel("Applied Force", 10));
            String forces[] = {"Nothing","Attraction", "Repulsion"};
            forceBox = new JComboBox(forces);
            forceBox.setSelectedIndex(0);
            leftPanel.add(forceBox);
            
            leftParentPanel.add(leftPanel,BorderLayout.CENTER);
            divPanel.add(leftParentPanel);
           
            markerList = new JList();
            markerList.addListSelectionListener(control);
            
            JScrollPane markerScrollPane = new JScrollPane(markerList);
            
            Scene scene = AppManager.getSharedManager().getScene();
            if (scene.getEnvironmentNode().markers.isEmpty()){
                String string[] = {"No Markers Found"};
                markerList.setListData(string);
            }
            else{
                control.updateMarkerList();
            }
            
            divPanel.add(markerScrollPane);
        
            mainPanel.add(divPanel,BorderLayout.CENTER);
            
            JPanel bottomPanel = new JPanel(new GridLayout(0,1));
            bottomPanel.add(GUIUtilities.getJButton("Update Marker", "UPDATE_MARKER", control, new Dimension(300,25), null));
            bottomPanel.add(GUIUtilities.getJButton("Add Marker", "ADD_MARKER", control, new Dimension(300,25), null));
            bottomPanel.add(GUIUtilities.getJButton("Delete Marker", "DEL_MARKER", control, new Dimension(300,25), null));

            mainPanel.add(bottomPanel,BorderLayout.SOUTH);
        
            pack();
            
            return mainPanel;
            
        }

    public JComboBox getForceBox() {
        return forceBox;
    }

    public void setForceBox(JComboBox forceBox) {
        this.forceBox = forceBox;
    }

    public MarkerManageControl getControl() {
        return control;
    }

    public void setControl(MarkerManageControl control) {
        this.control = control;
    }

    public JList getMarkerList() {
        return markerList;
    }

    public void setMarkerList(JList markerList) {
        this.markerList = markerList;
    }

    public VectorPanel getMarkerVectorPanel() {
        return markerVectorPanel;
    }

    public void setMarkerVectorPanel(VectorPanel markerVectorPanel) {
        this.markerVectorPanel = markerVectorPanel;
    }

    public JTextField getMarkerIDField() {
        return markerIDField;
    }

    public void setMarkerIDField(JTextField markerIDField) {
        this.markerIDField = markerIDField;
    }

    public JComboBox getColorBox() {
        return colorBox;
    }

    public void setColorBox(JComboBox colorBox) {
        this.colorBox = colorBox;
    }

    public JComboBox getShapeBox() {
        return shapeBox;
    }

    public void setShapeBox(JComboBox shapeBox) {
        this.shapeBox = shapeBox;
    }
        
    public JComboBox getMarkerSegmentField() {
        return this.segmentBox;
    }    
}
