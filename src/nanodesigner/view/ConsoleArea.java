/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import nanodesigner.manager.AppManager;

/**
 *
 * @author Oliver
 */
public class ConsoleArea extends JTextArea {

    public static void LogLn(String string) {
        AppManager.sharedManager.getMainFrame().getConsoleArea().logln(string);
    }

    public static void Log(String string) {
        AppManager.sharedManager.getMainFrame().getConsoleArea().log(string);

    }

    public ConsoleArea() {
        init();
    }

    private void init() {
    }
    
    
    public void logln(String string) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        this.setText(getText() + "\n" + reportDate + " > "+ string);
        updateCarrotPosition();

    }

    public void log(String string) {
        this.setText(getText() + string);
        updateCarrotPosition();
    }

    private void updateCarrotPosition() {
        try {
            setCaretPosition(getLineStartOffset(getLineCount() - 1));
        } catch (BadLocationException ex) {
        }

    }
}
