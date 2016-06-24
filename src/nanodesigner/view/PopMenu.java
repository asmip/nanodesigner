/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import nanodesigner.manager.AppManager;
import nanodesigner.model.MoleculeNode;
import nanodesigner.tool.Tool;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class PopMenu extends JPopupMenu {

    MainFrame mainFrame = null;
    MoleculeNode selectedMol = null;
    Point initialPoint = null;

    public PopMenu(MainFrame mainFrame, Point aInitialPoint) {
        this.mainFrame = mainFrame;


        this.add(GUIUtilities.getJLabel("Translate", 12));
        JMenuItem translateX = new JMenuItem(" X");
        translateX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AppManager.getSharedManager().getScene().mousePointer.initToolForType(Tool.ToolType.TranslateToolType, "X", selectedMol);

            }
        });
        JMenuItem translateY = new JMenuItem(" Y");
        translateY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AppManager.getSharedManager().getScene().mousePointer.initToolForType(Tool.ToolType.TranslateToolType, "Y", selectedMol);

            }
        });
        JMenuItem translateZ = new JMenuItem(" Z");
        translateZ.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AppManager.getSharedManager().getScene().mousePointer.initToolForType(Tool.ToolType.TranslateToolType, "Z", selectedMol);

            }
        });
        this.add(translateX);
        this.add(translateY);
        this.add(translateZ);



        this.add(GUIUtilities.getJLabel("Rotate", 12));
        JMenuItem rotX = new JMenuItem(" X");
        rotX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AppManager.getSharedManager().getScene().mousePointer.initToolForType(Tool.ToolType.RotateToolType, "X", selectedMol);
            }
        });
        JMenuItem rotY = new JMenuItem(" Y");
        rotY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AppManager.getSharedManager().getScene().mousePointer.initToolForType(Tool.ToolType.RotateToolType, "Y", selectedMol);
            }
        });
        JMenuItem rotZ = new JMenuItem(" Z");
        rotZ.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AppManager.getSharedManager().getScene().mousePointer.initToolForType(Tool.ToolType.RotateToolType, "Z", selectedMol);
            }
        });
        this.add(rotX);
        this.add(rotY);
        this.add(rotZ);

    }

    public void displayPopMenu(int x, int y, MoleculeNode selectedMol) {
        show(mainFrame.ctx.getCanvas(), x, y);
        this.selectedMol = selectedMol;

    }
}
