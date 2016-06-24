/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import com.sun.corba.se.spi.activation._ActivatorImplBase;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import nanodesigner.utilities.GUIUtilities;

/**
 *
 * @author Oliver
 */
public class LoadingBar extends JProgressBar {

    int count = 0;
    boolean doneInitialLoading = false;
    boolean active = false;
    public boolean looping = false; /** if looping = true, must call deactivate() after usage **/
    
    LoadingBar() {
        setString("Camera View");
        setStringPainted(true);
    }

    synchronized void updateLoadingPanelFromSceneThread(float tpf) {
    }

    public void activate(int maxValue) {
        this.setMaximum(maxValue);
        this.setValue(0);
        this.count = 0;
        this.doneInitialLoading = false;
        this.active = true;
    }

    public void deactivate() {
        this.setMaximum(0);
        this.count = 0;
        this.active = false;
        this.looping = false;
    }

    public void incrementProgressBar() {
        if (this.active) {
            count ++;
            this.setValue(count);

            if (!doneInitialLoading) {
                if (count >= this.getMaximum()) {
                    this.doneInitialLoading = true;
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {

                        @Override
                        public void run() {
                            if (!looping) {deactivate();}
                            else{
                                count = 0;
                            }
                        }
                    };
                    timer.schedule(task, 1000);
                }
            }
        }
    }
}
