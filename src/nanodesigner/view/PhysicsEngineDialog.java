/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import com.sun.codemodel.internal.JFieldRef;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import nanodesigner.control.PhysicsEngineControl;
import nanodesigner.control.SolvationControl;
import nanodesigner.manager.AppManager;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class PhysicsEngineDialog extends JDialog{

    public PhysicsEngineControl control = null;
    public JTextField timeStepField = null;
    public JTextField durationField = null;
    public JComboBox collisionDetailBox = null;
    public JComboBox broadphaseBox = null;
    public JTextField collisionBoxMultField = null;
    public JComboBox debugBox = null;
    public JSlider speedSlider = null;
    private boolean physicsActive = false;
    public VectorPanel gravityVectorPanel = null;

    PhysicsEngineDialog() {
        control = new PhysicsEngineControl(this);
        
        setModal(false);
        
        setPhysicsActive(AppManager.sharedManager.getScene().isPhysicsEnabled());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                setVisible(false);
            }

            public void windowClosed(WindowEvent we) {
            }

            public void windowIconified(WindowEvent we) {
            }

            public void windowDeiconified(WindowEvent we) {
            }

            public void windowActivated(WindowEvent we) {
            }

            public void windowDeactivated(WindowEvent we) {
            }
        });
        
        setLocationRelativeTo(AppManager.sharedManager.getMainFrame().getMainPanel());
        setLocation(new Point(100, 50));
        setResizable(false);
        setVisible(true);
        setAlwaysOnTop(true);
    }
    
    
    private JPanel initContentForDisabled(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JLabel label = new JLabel("Physics Engine Configuration");
        mainPanel.add(label, BorderLayout.NORTH);
        
        JPanel aPanel = new JPanel(new GridLayout(0,2));
        
        aPanel.add(GUIUtilities.getJLabel("Timestep Accuracy", 10));
        aPanel.add(GUIUtilities.getJLabel("Steps (0=inf)", 10));

        aPanel.add(timeStepField = new JTextField("1/30"));
        aPanel.add(durationField = new JTextField("0"));

        //Other stuff like Gravity vector, or a Pressure point etc.
        aPanel.add(GUIUtilities.getJLabel("Collision Box Detail", 10));
        aPanel.add(GUIUtilities.getJLabel("Collision Broadphase", 10));
        
        String[] detailStrings = {"V.Fast", "Slow", "V.Slow"};
        String[] broadphaseStrings = {"Simple", "DBVT", "AXIS", "AXIS_32"};


        collisionDetailBox = new JComboBox(detailStrings);
        collisionDetailBox.setSelectedIndex(0);
        aPanel.add(collisionDetailBox);
        
        broadphaseBox = new JComboBox(broadphaseStrings);
        broadphaseBox.setSelectedIndex(1);
        aPanel.add(broadphaseBox);
        
        aPanel.add(GUIUtilities.getJLabel("Collision Box Size", 10));
        aPanel.add(GUIUtilities.getJLabel("Physics Draw", 10));
        
        collisionBoxMultField = new JTextField("1.2");
        aPanel.add(collisionBoxMultField);
        
        String[] drawStrings = {"Off","On"};
        debugBox = new JComboBox(drawStrings);
        debugBox.setSelectedIndex(0);
        aPanel.add(debugBox);
        
        mainPanel.add(aPanel,BorderLayout.CENTER);
        mainPanel.add(GUIUtilities.getJButton("Initialise", "CMD_PHYSDISABLED", control, new Dimension(100, 30), null), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    
    
    private JPanel initContentForEnabled(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Physics Engine Monitor");
        mainPanel.add(label, BorderLayout.NORTH);        
        JPanel internPanel = new JPanel(new GridLayout(0, 1));
        internPanel.add(GUIUtilities.getJLabel("Speed Multiplier", 10));
    
        int speedMaxima = 20,speedMinima = -10,speedDefault = (int)control.physicsEngineState.getSpeed();
        speedSlider = new JSlider(JSlider.HORIZONTAL, speedMinima,speedMaxima,speedDefault);
        speedSlider.setMajorTickSpacing(5);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPreferredSize(new Dimension(150,35));
        speedSlider.setMinimumSize(new Dimension(150,35));
        speedSlider.setMaximumSize(new Dimension(150,35));
        speedSlider.addChangeListener(control);
        
        internPanel.add(speedSlider);    
        internPanel.add(GUIUtilities.getJLabel("Gravity Vector", 10));
        internPanel.add(gravityVectorPanel = new VectorPanel(null, "GRAV",new Dimension(150,35)));
        internPanel.add(GUIUtilities.getJButton("Apply", "CMD_UPDATE", control, new Dimension(100, 30), null));

        mainPanel.add(internPanel,BorderLayout.CENTER);
        mainPanel.add(GUIUtilities.getJButton("Stop Engine", "CMD_PHYSENABLED", control, new Dimension(100, 30), null), BorderLayout.SOUTH);
        return mainPanel;
    }
        
    public boolean isPhysicsActive() {
        return physicsActive;
    }

    public void setPhysicsActive(boolean isPhysicsActive) {
        this.physicsActive = isPhysicsActive;
        
        if (physicsActive){
            this.setContentPane(initContentForEnabled());
        }
        else{
            this.setContentPane(initContentForDisabled());
        }    
       
        pack();
    }
}