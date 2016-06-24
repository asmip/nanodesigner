/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oliver
 */
public class HudControl implements ScreenController{

    private Scene scene;
    private Nifty nifty;

    
    public HudControl(Scene scene) {
        //Initialise HUD
        this.scene = scene;
        initNiftyDisplay();
       
    
    
    }
    private void initNiftyDisplay(){
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(scene.getAssetManager(),
                                                           scene.getInputManager(),
                                                           scene.getAudioRenderer(),
                                                           scene.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/nanohud.xml", "start", this);
        
        // attach the nifty display to the gui view port as a processor
        scene.getGuiViewPort().addProcessor(niftyDisplay);
        scene.getInputManager().setCursorVisible(true);
    
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);      
    }
    
    public void onMouse(){
        System.out.println("On Mouse Button");
    }
    
    public void onRotateN(){
        System.out.println("On Rotate Negative Button");
    }
    
    public void onRotateP(){
        System.out.println("On Rotate Positive Button");
    }
    
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
    }

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void quit(){
        System.out.println("Quit!!");
        nifty.gotoScreen("end");
    }
}
