/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.tool;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.ui.Picture;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;
import nanodesigner.model.MoleculeNode;

/**
 *
 * @author Oliver
 */
public class Tool {

    MoleculeNode targetMoleculeNode = null;
    String axis = null;
    Point initialPoint = null;
    MouseMotionListener motionListener = null;
    MouseListener mouseListener = null;
    Scene scene = null;
    Timer timer = null;
    TimerTask timerTask = null;
    Picture pic = null;
    float deltaX = 0;
    float movement = 0;
    boolean enabled = true;
    ToolType type = null;

    void setNewTool(String axis, MoleculeNode selectedMol, ToolType toolType) {

        this.targetMoleculeNode = selectedMol;
        this.type = toolType;
        this.axis = axis;
        AppManager.getSharedManager().getMainFrame().loadingPanel.setString("Tool Mode: " + type.toString() + ": " + axis + "\t" + " Press (Esc) to exit");

    }

    public enum ToolType {

        RotateToolType,
        TranslateToolType
    }

    public Tool(String aAxis, MoleculeNode object, final ToolType type) {
        super();
        this.type = type;
        this.scene = AppManager.getSharedManager().getScene();
        this.axis = aAxis;
        this.targetMoleculeNode = object;
        this.initialPoint = MouseInfo.getPointerInfo().getLocation();

        AppManager.getSharedManager().getMainFrame().loadingPanel.setString("Tool Mode: " + type.toString() + ": " + axis + "\t" + " Press (Esc) to exit");

        mouseListener = new MouseListener() {
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == 1) {
                    setEnabled(true);
                    initialPoint = MouseInfo.getPointerInfo().getLocation();

                    if (pic == null) {
                          initGUIImage();
                    } else {
                          setGUIImagePosition(initialPoint.x, initialPoint.y);
                    }
                }

            }

            public void mouseReleased(MouseEvent me) {
                setEnabled(false);
            }

            public void mouseEntered(MouseEvent me) {
            }

            public void mouseExited(MouseEvent me) {
            }

            public void mouseClicked(MouseEvent me) {
            }
        };



        motionListener = new MouseMotionListener() {
            public void mouseDragged(MouseEvent me) {
                if (enabled) {

                    deltaX = (me.getLocationOnScreen().x - initialPoint.x);
                    int padding = 8;

                    if (type == ToolType.RotateToolType) {
                        float m = 0.0025f;
                        m *= deltaX;
                        movement = m;
                    } else if (type == ToolType.TranslateToolType) {
                        float m = 0.005f;
                        m *= deltaX;
                        movement = m;
                    }
                    if (deltaX <= padding && deltaX >= -padding) {
                        movement = 0;
                    }
                    float picWidth = deltaX;
                    setGUIImageWidth(picWidth);

                }
            }

            public void mouseMoved(MouseEvent me) {
            }
        };

        AppManager.getSharedManager().getMainFrame().ctx.getCanvas().addMouseMotionListener(motionListener);
        AppManager.getSharedManager().getMainFrame().ctx.getCanvas().addMouseListener(mouseListener);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (enabled) {
                    updateTool();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000 / 30);
    }

    private void updateTool() {

        if (enabled) {
            scene.enqueue(new Callable() {
                @Override
                public Void call() {

                    if (type == ToolType.RotateToolType) {
                        if (axis.equals("X")) {
                            targetMoleculeNode.rotate(new Quaternion().fromAngleAxis(movement, Vector3f.UNIT_X));
                        } else if (axis.equals("Y")) {
                            targetMoleculeNode.rotate(new Quaternion().fromAngleAxis(movement, Vector3f.UNIT_Y));
                        } else if (axis.equals("Z")) {
                            targetMoleculeNode.rotate(new Quaternion().fromAngleAxis(movement, Vector3f.UNIT_Z));
                        }
                    } else if (type == ToolType.TranslateToolType) {
                        if (axis.equals("X")) {
                            targetMoleculeNode.move(movement, 0, 0);
                        } else if (axis.equals("Y")) {
                            targetMoleculeNode.move(0, movement, 0);
                        } else if (axis.equals("Z")) {
                            targetMoleculeNode.move(0, 0, movement);
                        }
                    }
                    return null;
                }
            });
        }
    }

    private void initGUIImage() {
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                pic = new Picture("HUD Picture");
                pic.setImage(scene.getAssetManager(), "Interface/toolbar.png", true);
                Material mat_wall = new Material(scene.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                mat_wall.setColor("Color", ColorRGBA.White);
                pic.setMaterial(mat_wall);
                scene.getGuiNode().attachChild(pic);
                pic.setPosition(initialPoint.x, initialPoint.y);
                return null;
            }
        });
    }

    void setGUIImageWidth(final float width) {
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                float w = Math.abs(width);

                if (pic != null) {
                    pic.setWidth(w);
                    AssetManager assetManager = AppManager.getSharedManager().getScene().getAssetManager();
                    Material mat_wall = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    float r = w / 255;
                    mat_wall.setColor("Color", new ColorRGBA(r, 0, 0, 1 - r));
                    pic.setMaterial(mat_wall);
                    if (width < 0) {
                        pic.setPosition(initialPoint.x - w, initialPoint.y);

                    } else {
                        pic.setPosition(initialPoint.x, initialPoint.y);
                    }
                }
                
                return null;
            }
        });

    }

    void setGUIImagePosition(final float x, final float y) {
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                if (pic != null) {
                    pic.setPosition(x, y);
                }
                return null;
            }
        });

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean state) {

        enabled = state;

        if (!enabled) {
            movement = 0;
            deltaX = 0;
            setGUIImageWidth(0);
        }

        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                if (pic != null) {
                    if (!state) {
                        pic.removeFromParent();
                    } else {
                        scene.getGuiNode().attachChild(pic);
                    }
                }
                return null;
            }
        });
    }
}
