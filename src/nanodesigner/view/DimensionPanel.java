/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class DimensionPanel extends JPanel {
    
    private JTextField xTextField = null;
    private JTextField yTextField = null;
    private JTextField zTextField = null;
    private JTextField wTextField = null;
    private JTextField hTextField = null;
    private JTextField dTextField = null;  

    DimensionPanel(Dimension dimension) {
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        
        
        setLayout(new GridLayout(0,3));
        
        add(GUIUtilities.getJLabel("X", 10));
        add(GUIUtilities.getJLabel("Y", 10));
        add(GUIUtilities.getJLabel("Z", 10));
        xTextField = new JTextField("0");
        yTextField = new JTextField("0");
        zTextField = new JTextField("0");
        add(xTextField);
        add(yTextField);
        add(zTextField);
        add(GUIUtilities.getJLabel("W", 10));
        add(GUIUtilities.getJLabel("H", 10));
        add(GUIUtilities.getJLabel("D", 10));
        wTextField = new JTextField("0");
        hTextField = new JTextField("0");
        dTextField = new JTextField("0");  
        add(wTextField);
        add(hTextField);
        add(dTextField);
        
        
    }

    public JTextField getxTextField() {
        return xTextField;
    }

    public void setxTextField(JTextField xTextField) {
        this.xTextField = xTextField;
    }

    public JTextField getyTextField() {
        return yTextField;
    }

    public void setyTextField(JTextField yTextField) {
        this.yTextField = yTextField;
    }

    public JTextField getzTextField() {
        return zTextField;
    }

    public void setzTextField(JTextField zTextField) {
        this.zTextField = zTextField;
    }

    public JTextField getwTextField() {
        return wTextField;
    }

    public void setwTextField(JTextField wTextField) {
        this.wTextField = wTextField;
    }

    public JTextField gethTextField() {
        return hTextField;
    }

    public void sethTextField(JTextField hTextField) {
        this.hTextField = hTextField;
    }

    public JTextField getdTextField() {
        return dTextField;
    }

    public void setdTextField(JTextField dTextField) {
        this.dTextField = dTextField;
    }

    
    
    
}
