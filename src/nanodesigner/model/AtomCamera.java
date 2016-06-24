/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.model;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 *
 * @author Oliver
 */
public class AtomCamera extends ChaseCamera{
    
    public AtomCamera(Camera cam) {
        super(cam);
    }

    public AtomCamera(Camera cam, InputManager inputManager) {
        super(cam, inputManager);
    }

    public AtomCamera(Camera cam, Spatial target) {
        super(cam, target);
    }

    public AtomCamera(Camera cam, Spatial target, InputManager inputManager) {
        super(cam, target, inputManager);
    }

    @Override
    protected void updateCamera(float tpf) {
        super.updateCamera(tpf);
    } 
}
