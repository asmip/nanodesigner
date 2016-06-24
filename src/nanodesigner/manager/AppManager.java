/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.manager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import nanodesigner.control.Scene;
import nanodesigner.view.MainFrame;

/**
 *
 * @author Oliver
 */
public class AppManager {
    public static AppManager sharedManager = null;
    private static final Logger logger = Logger.getLogger(Scene.class.getName());
    private Scene scene;
    private MainFrame mainFrame;
    
    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.WARNING);
        AppManager.sharedManager = new AppManager();
    }

    public AppManager() {    
        super();
        getJVMInformation();
        scene = new Scene();
        mainFrame = new MainFrame(scene);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void getJVMInformation(){
      long maxMem = Runtime.getRuntime().maxMemory();
      System.out.println("AppManager: JVM: Max memory: " + maxMem);
    }
    
    public static AppManager getSharedManager() {
       return sharedManager;
    }

       
    public void didFinishLoadingScene(Scene scene){
        mainFrame.didFinishLoadingScene();
    }

        
    public void updateAppManagerFromSceneThread(float tpf) {
      /*** Update is called from Scene Thread, useful for synchronizing the GUI
       * with the Scene
       ***/
        mainFrame.updateMainFrameFromSceneThread(tpf);
    }
    
    public Scene getScene() {
        return scene;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }    
}
