/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.TextAction;

public class GUIUtilities {

    public static JButton getJButton(String label, String cmd, ActionListener al, Dimension size, JPanel aPanel) {
        JButton button = new JButton(label);
        button.setActionCommand(cmd);
        button.addActionListener(al);

        if (size != null) {

            button.setSize(size);
            button.setMinimumSize(size);
            button.setMaximumSize(size);
            button.setPreferredSize(size);
        }

        if (aPanel != null) {
            aPanel.add(button);
        }
        return button;
    }

    public static JLabel getJLabel(String string, int size) {
        JLabel label = new JLabel(string);
        Font font = new Font("Dialog", Font.PLAIN, size);
        label.setFont(font);

        return label;
    }

    public static void configureTabJTextField(JTextField textField) {
        
        
        ActionMap map = textField.getActionMap();
        // get a reference to the default binding
        final Action notify = map.get(textField.notifyAction);
        while (map.getParent() != null) {
            // walk up the parent chain to reach the top-most shared ancestor
            map = map.getParent();
        }
        // custom notify action
        TextAction tab = new TextAction(JTextField.notifyAction) {
            @Override
            public void actionPerformed(ActionEvent e) {
                // delegate to default if enabled
                if (notify.isEnabled()) {
                    notify.actionPerformed(e);
                }
                // trigger a focus transfer
                getTextComponent(e).transferFocus();
            }
        };
        // replace default with augmented custom action
        map.put(JTextField.notifyAction, tab);
    }
}
