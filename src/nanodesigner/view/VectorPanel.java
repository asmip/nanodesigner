/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class VectorPanel extends JPanel {

    public class VectorTextField extends JTextField {

        public VectorTextField(String string) {
            super(string);
        }
        
        
        
        @Override
        public void setText(String textString){
            super.setText(textString);
            this.setCaretPosition(0);
        }
    }
    
    private VectorTextField xTextField = null;
    private VectorTextField yTextField = null;
    private VectorTextField zTextField = null;
    private JLabel xLabel = null;
    private JLabel yLabel = null;
    private JLabel zLabel = null;
    private boolean isEditable;
    private ActionListener listener = null;
    private String tag = null;

    public boolean xUpdatable,yUpdatable,zUpdatable;
    
    VectorPanel(ActionListener listener, String name, Dimension dimension) {
        this.tag = name;
        this.listener = listener;
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setMinimumSize(dimension);

        setLayout(new GridLayout(0, 3));

        add(xLabel = GUIUtilities.getJLabel("X", 10));
        add(yLabel = GUIUtilities.getJLabel("Y", 10));
        add(zLabel = GUIUtilities.getJLabel("Z", 10));
        xTextField = new VectorTextField("0");
        xTextField.setHorizontalAlignment(JTextField.LEFT);
        xTextField.addFocusListener(new FocusListener() {
        
            public void focusGained(FocusEvent fe) {
                xUpdatable = false;
            
            }

            public void focusLost(FocusEvent fe) {
            }
        });

        xTextField.setActionCommand(name + "_X");
        xTextField.addActionListener(listener);
        yTextField = new VectorTextField("0");
        yTextField.setActionCommand(name + "_Y");
        yTextField.setHorizontalAlignment(JTextField.LEFT);

        yTextField.addActionListener(listener);
        zTextField = new VectorTextField("0");
        zTextField.setActionCommand(name + "_Z");
        zTextField.setHorizontalAlignment(JTextField.LEFT);
        zTextField.addActionListener(listener);
        add(xTextField);
        add(yTextField);
        add(zTextField);
    }
    public void updatePanel(String xString, String yString, String zString) {
        if (xUpdatable){
            xTextField.setText(xString);
        }        
    }
    public void updateFlags(){
        xUpdatable = true;
        yUpdatable = true;
        zUpdatable = true;
    }

    public boolean isIsEnabled() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
        
        xTextField.setEditable(false);
        yTextField.setEditable(false);
        zTextField.setEditable(false);
        
    }

    public VectorTextField getxTextField() {
        return xTextField;
    }

    public void setxTextField(VectorTextField xTextField) {
        this.xTextField = xTextField;
    }

    public VectorTextField getyTextField() {
        return yTextField;
    }

    public void setyTextField(VectorTextField yTextField) {
        this.yTextField = yTextField;
    }

    public VectorTextField getzTextField() {
        return zTextField;
    }

    public void setzTextField(VectorTextField zTextField) {
        this.zTextField = zTextField;
    }

    public ActionListener getListener() {
        return listener;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public JLabel getxLabel() {
        return xLabel;
    }

    public void setxLabel(JLabel xLabel) {
        this.xLabel = xLabel;
    }

    public JLabel getyLabel() {
        return yLabel;
    }

    public void setyLabel(JLabel yLabel) {
        this.yLabel = yLabel;
    }

    public JLabel getzLabel() {
        return zLabel;
    }

    public void setzLabel(JLabel zLabel) {
        this.zLabel = zLabel;
    }
}
