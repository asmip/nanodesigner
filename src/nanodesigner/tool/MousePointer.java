 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.tool;

import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;
import nanodesigner.model.MoleculeNode;
import nanodesigner.tool.Tool.ToolType;

/**
 *
 * @author Oliver
 */
public class MousePointer {

    Scene scene = null;
    private Tool mouseTool = null;
    int doubleClickTime = 0;

    public MousePointer(Scene scene) {
        this.scene = scene;
        scene.getInputManager().addMapping("CloseTool", new KeyTrigger(KeyInput.KEY_ESCAPE));
        scene.getInputManager().addMapping("Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        scene.getInputManager().addMapping("Popup", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        scene.getInputManager().addListener(actionListener, "Popup");
        scene.getInputManager().addListener(actionListener, "Select");
        scene.getInputManager().addListener(actionListener, "CloseTool");
        
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                if (doubleClickTime > 0) {
                    doubleClickTime --;    
                }
                
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000/100);
        
    }
    private ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
        }

        public void onAction(String name, boolean isPressed, float tpf) {

            if (name.equals("CloseTool")) {
                scene.chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
                AppManager.sharedManager.getMainFrame().loadingPanel.setString("Camera View");

            }


            if (name.equals("Popup") || name.equals("Select")) {

                if (isPressed) {
                    CollisionResults results = new CollisionResults();
                    // Convert screen click to 3d position
                    Scene scene = AppManager.sharedManager.getScene();
                    Vector2f click2d = scene.getInputManager().getCursorPosition();
                    Vector3f click3d = scene.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = scene.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    // Aim the ray from the clicked spot forwards.
                    Ray ray = new Ray(click3d, dir);
                    // Collect intersections between ray and all nodes in results list.
                    scene.getRootNode().collideWith(ray, results);

                    if (results.size() > 0) {
                        // The closest result is the target that the player picked:
                        Geometry target = results.getClosestCollision().getGeometry();

                        for (int i = 0; i < scene.environmentNode.molecules.size(); i++) {
                            MoleculeNode mol = (MoleculeNode) scene.environmentNode.molecules.get(i);

                            if (mol.hasChild(target)) {

                                if (name.equals("Select")){
                                    doubleClickTime += 50;
                                    
                                    if (doubleClickTime >= 70){
                                        AppManager.sharedManager.getMainFrame().getExplorerPanel().getControl().selectedMolecule(i);

                                    }
                                }
                                
                                
                                else if (name.equals("Popup")){
                                    Point canvasPoint = AppManager.sharedManager.getMainFrame().ctx.getCanvas().getLocation();
                                    AppManager.sharedManager.getMainFrame().popMenu.displayPopMenu((int) click2d.x, (int) click2d.y, mol);
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    public void initToolForType(ToolType toolType, String axis, MoleculeNode selectedMol) {
        if (mouseTool != null) {
           mouseTool.setNewTool(axis,selectedMol,toolType);
        }
        else{
            mouseTool = new Tool(axis, selectedMol, toolType);
        }
        
        scene.chaseCam.setToggleRotationTrigger();
    }

}
