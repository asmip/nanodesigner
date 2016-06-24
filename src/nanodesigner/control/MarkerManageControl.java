/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nanodesigner.manager.AppManager;
import nanodesigner.model.AtomModel;
import nanodesigner.model.MarkerModel;
import nanodesigner.view.MarkerManageDialog;
import nanodesigner.view.MarkerManageDialog.MarkerColor;
import nanodesigner.view.MarkerManageDialog.MarkerShape;

/**
 *
 * @author Oliver
 */
public class MarkerManageControl implements ActionListener, ListSelectionListener {

    MarkerManageDialog dialog = null;
    int selectedListIndex = 0;
    
    public MarkerManageControl(MarkerManageDialog aThis) {
        dialog = aThis;
    }

    public void actionPerformed(ActionEvent ae) {
        Scene scene = AppManager.getSharedManager().getScene();

        if (ae.getActionCommand().equals("ADD_MARKER")) {
            if (checkMarkerExistance()) {
                JOptionPane.showMessageDialog(null, "Unique Markers Only!");
                return;
            }

            float x = Float.parseFloat(dialog.getMarkerVectorPanel().getxTextField().getText());
            float y = Float.parseFloat(dialog.getMarkerVectorPanel().getyTextField().getText());
            float z = Float.parseFloat(dialog.getMarkerVectorPanel().getzTextField().getText());

            MarkerColor markerColor = MarkerColor.values()[dialog.getColorBox().getSelectedIndex()];
            MarkerShape markerShape = MarkerShape.values()[dialog.getShapeBox().getSelectedIndex()];
            MarkerModel markerModel = new MarkerModel(
                    new Vector3f(x, y, z),
                    markerColor,
                    markerShape,
                    (String)dialog.getMarkerSegmentField().getSelectedItem(),
                    (String)dialog.getForceBox().getSelectedItem(),
                    dialog.getMarkerIDField().getText());

            scene.getEnvironmentNode().addMarkerModel(markerModel);
            updateMarkerList();
        } 
        else if (ae.getActionCommand().equals("UPDATE_MARKER")) {
            float x = Float.parseFloat(dialog.getMarkerVectorPanel().getxTextField().getText());
            float y = Float.parseFloat(dialog.getMarkerVectorPanel().getyTextField().getText());
            float z = Float.parseFloat(dialog.getMarkerVectorPanel().getzTextField().getText());
            MarkerColor markerColor = MarkerColor.values()[dialog.getColorBox().getSelectedIndex()];
            MarkerShape markerShape = MarkerShape.values()[dialog.getShapeBox().getSelectedIndex()];

            int selectedIndex = selectedListIndex;

            if (selectedIndex >= 0) {
                MarkerModel markerModel = scene.getEnvironmentNode().markers.get(selectedIndex);

                if (checkMarkerExistance() && !dialog.getMarkerIDField().getText().equals(markerModel.getIdentifier())) {
                    JOptionPane.showMessageDialog(null, "Unique Markers Only!");
                    return;
                }

                markerModel.updateContents(new Vector3f(x, y, z), markerColor, markerShape, dialog.getMarkerIDField().getText());
                updateMarkerList();
            }
        } 
        else if (ae.getActionCommand().equals("DEL_MARKER")) {
            int selectedIndex = dialog.getMarkerList().getSelectedIndex();
            if (selectedIndex >= 0) {
                MarkerModel markerModel = scene.getEnvironmentNode().markers.get(selectedIndex);
                scene.getEnvironmentNode().removeMarkerModel(markerModel);
                updateMarkerList();
            }
        }
    }

    public boolean checkMarkerExistance() {
        Scene scene = AppManager.getSharedManager().getScene();
        boolean exist = false;

        for (int i = 0; i < scene.environmentNode.markers.size(); i++) {
            MarkerModel marker = (MarkerModel) scene.environmentNode.markers.get(i);
            if (marker.getIdentifier().equals(dialog.getMarkerIDField().getText())) {
                exist = true;
            }
        }
        return exist;
    }

    public void updateMarkerList() {
        Scene scene = AppManager.getSharedManager().getScene();
        DefaultListModel model = new DefaultListModel();

        for (int i = 0; i < scene.environmentNode.markers.size(); i++) {
            MarkerModel marker = (MarkerModel) scene.environmentNode.markers.get(i);

            Color c = new Color(marker.getColor().r, marker.getColor().g, marker.getColor().b);
            Object element = marker.getIdentifier();

            model.addElement(element);
        }

        dialog.getMarkerList().setModel(model);
    }

    public void valueChanged(ListSelectionEvent lse) {
        JList list = (JList) lse.getSource();
        Scene scene = AppManager.getSharedManager().getScene();

        selectedListIndex = list.getSelectedIndex();
        
        if (list.getSelectedIndex() >= 0 && !scene.getEnvironmentNode().markers.isEmpty()) {
            MarkerModel markerModel = (MarkerModel) scene.getEnvironmentNode().markers.get(list.getSelectedIndex());

            scene.setChaseCamSpatial(markerModel);
            
            int colorIndex = MarkerColor.valueOf(markerModel.getMarkerColor().toString()).ordinal();
            int shapeIndex = MarkerShape.valueOf(markerModel.getMarkerShape().toString()).ordinal();

            dialog.getMarkerIDField().setText(markerModel.getIdentifier());
            dialog.getMarkerVectorPanel().getxTextField().setText("" + markerModel.getWorldTranslation().x);
            dialog.getMarkerVectorPanel().getyTextField().setText("" + markerModel.getWorldTranslation().y);
            dialog.getMarkerVectorPanel().getzTextField().setText("" + markerModel.getWorldTranslation().z);
            dialog.getColorBox().setSelectedIndex(colorIndex);
            dialog.getShapeBox().setSelectedIndex(shapeIndex);
        }
    }
}
