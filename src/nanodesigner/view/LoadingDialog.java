/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import nanodesigner.manager.AppManager;

/**
 *
 * @author Oliver
 */
final public class LoadingDialog extends JDialog {

    /**
     * LoadingDialog's JProgressBar uses a mix of both asynchronous callbacks
     * based on tasks Aswell as a frame-based progression. This is useful as the
     * frame-based progression can display the potential lag of the Scene thread
     * as a whole. The ratio of Async Callbacks : Frame Progression is defined
     * by frameCallbackRatio.
     *
     */
    JProgressBar progressBar = null;
    JTextArea textArea = null;
    boolean doneInitialLoading = false;
    boolean disposed = false;
    int count = 0;
    Dimension dimension = new Dimension(200, 300);
    Thread thread = null;

    public void setDisposed() {
        if (!disposed) {
            disposed = true;
            count = 0;
            removeAll();
            doneInitialLoading = false;
            dispose();
        }
    }

    public LoadingDialog( int max) {
        super();

        System.out.println("Loadingbar max = " + max);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
        //this.setAlwaysOnTop(true);

        if (max == 0) {
            setDisposed();
            return;
        }

        setModal(false);
        setContentPane(initContent(0, max));
        pack();
        setLocationRelativeTo(AppManager.getSharedManager().getMainFrame().getMainPanel());
        setResizable(false);
        setVisible(true);
    }

    synchronized void updateLoadingPanelFromSceneThread(float tpf) {
    }

    public void incrementProgressBar() {
        System.out.println("Increment: count = :" + count);
        progressBar.setValue(count++);

        if (!doneInitialLoading) {
            if (count >= progressBar.getMaximum()) {
                this.doneInitialLoading = true;
                setDisposed();

            }
        }
    }

    synchronized public void appendTextField(final String newLineString) {
        String currentText = this.textArea.getText();
        String newText = currentText + "\n" + newLineString;
        this.textArea.setText(newText);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    private JPanel initContent(int min, int max) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(dimension);
        panel.setMaximumSize(dimension);
        panel.setMinimumSize(dimension);

        JLabel text = new JLabel("Please wait");
        panel.add(text, BorderLayout.NORTH);

        progressBar = new JProgressBar();
        progressBar.setMinimum(min);
        progressBar.setMaximum(max);
        panel.add(progressBar, BorderLayout.CENTER);

        Dimension textAreaDimension = new Dimension(dimension.width, dimension.height - 50);

        textArea = new JTextArea("....");
        textArea.setPreferredSize(textAreaDimension);
        textArea.setMaximumSize(textAreaDimension);
        textArea.setMinimumSize(textAreaDimension);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(textArea, BorderLayout.SOUTH);

        return panel;
    }
}
