/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import nanodesigner.control.PhysicsEngineState.CollisionBroadPhase;
import nanodesigner.control.PhysicsEngineState.CollisionDetail;
import nanodesigner.manager.AppManager;
import nanodesigner.view.PhysicsEngineDialog;

/**
 *
 * @author Oliver
 */
public class PhysicsEngineControl implements ActionListener, ChangeListener{

    PhysicsEngineDialog dialog;
    public PhysicsEngineState physicsEngineState;
    
    public PhysicsEngineControl(PhysicsEngineDialog dialog) {
        super();
        this.dialog = dialog;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("CMD_PHYSDISABLED")) {

            boolean debugDraw = false;
            if (dialog.debugBox.getSelectedIndex() == 1) {
                debugDraw = true;
            }

            float accuracy = parse(dialog.timeStepField.getText());
            int numOfSteps = Integer.parseInt(dialog.durationField.getText());
            float speed = 1.0f;
            float collisionBoxSize = parse(dialog.collisionBoxMultField.getText());
            CollisionDetail collisionDetail = CollisionDetail.values()[dialog.collisionDetailBox.getSelectedIndex()];
            CollisionBroadPhase broadPhase = CollisionBroadPhase.values()[dialog.broadphaseBox.getSelectedIndex()];
            Vector3f gravityVector3f = Vector3f.ZERO;

            physicsEngineState = new PhysicsEngineState(
                    debugDraw,
                    accuracy,
                    numOfSteps,
                    speed,
                    gravityVector3f,
                    collisionBoxSize,
                    collisionDetail,
                    broadPhase
                    );
            dialog.setPhysicsActive(true);

            AppManager.sharedManager.getScene().setPhysicsEnabled(true, physicsEngineState);
            AppManager.sharedManager.getMainFrame().getPropertyPanel().getMoleDeleteButton().setEnabled(false);
            AppManager.getSharedManager().getMainFrame().toFront();
            dialog.toFront();
        } else if (ae.getActionCommand().equals("CMD_PHYSENABLED")) {
            AppManager.sharedManager.getScene().setPhysicsEnabled(false, null);
            AppManager.sharedManager.getMainFrame().getPropertyPanel().getMoleDeleteButton().setEnabled(true);

            dialog.setPhysicsActive(false);
            dialog.setVisible(false);
        }
        
        else if (ae.getActionCommand().equals("CMD_UPDATE")){
            //Update the physicsEngineState then apply it to the Scene
            
            float x = Float.parseFloat(dialog.gravityVectorPanel.getxTextField().getText());
            float y = Float.parseFloat(dialog.gravityVectorPanel.getyTextField().getText());
            float z = Float.parseFloat(dialog.gravityVectorPanel.getzTextField().getText());

            physicsEngineState.setGravity(new Vector3f(x,y,z));
            
            AppManager.sharedManager.getScene().setPhysicsEngineState(physicsEngineState);
        }

    }

    float parse(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Float.parseFloat(rat[0]) / Float.parseFloat(rat[1]);
        } else {
            return Float.parseFloat(ratio);
        }
    }

    public void stateChanged(ChangeEvent ce) {
        JSlider slider = (JSlider)ce.getSource();
        physicsEngineState.setSpeed(slider.getValue());
        
        //Update the Scene physics engine
        AppManager.sharedManager.getScene().setPhysicsEngineState(physicsEngineState);
        
    }
}
