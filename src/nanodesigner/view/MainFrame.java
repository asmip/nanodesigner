/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.view;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import nanodesigner.control.DensityMeasureControl;
import nanodesigner.control.DistanceConflictControl;
import nanodesigner.control.ExplorerControl;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;

/**
 *
 * @author Oliver
 */
public class MainFrame extends JFrame {

    static MainFrame sharedFrame = null;
    Scene scene = null;
    Dimension defaultSize = new Dimension(640, 480);
    private JPanel mainPanel;
    private ExplorerPanel explorerPanel = null;
    private PropertyPanel propertyPanel = null;
    private ConsoleArea consoleArea = null;
    JCheckBoxMenuItem atomicCheckBox;
    JCheckBoxMenuItem explorerCheckBox;
    public LoadingBar loadingPanel = null;    
    PhysicsEngineDialog physicsEngineDialog = null;
    private MarkerManageDialog markerManageDialog;
    public PopMenu popMenu = null;
    public JmeCanvasContext ctx = null;

    public MainFrame(Scene scene) {
        this.scene = scene;
        init();

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    private void init() {
        sharedFrame = this;

        setTitle("NanoDesigner Session View");
        setResizable(false);
        
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        AppSettings settings = new AppSettings(true);
        settings.setWidth(1024);
        settings.setHeight(768);

        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem resetMenuItem = new JMenuItem("New", null);
        resetMenuItem.setMnemonic(KeyEvent.VK_C);
        resetMenuItem.setToolTipText("Create a new Scene");
        resetMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane pane = new JOptionPane(
                        "You are sure you want to\nCreate a new Scene?");
                Object[] options = new String[]{"Cancle", "Yes"};
                pane.setOptions(options);
                JDialog dialog = pane.createDialog(new JFrame(), "New Scene");
                dialog.setVisible(true);
                Object obj = pane.getValue();
                int result = -1;
                for (int k = 0; k < options.length; k++) {
                    if (options[k].equals(obj)) {
                        result = k;
                    }
                }
                if (result == 1) {
                    AppManager.sharedManager.getScene().resetScene();
                }
            }
        });


        JMenuItem eMenuItem = new JMenuItem("Exit", null);
        eMenuItem.setMnemonic(KeyEvent.VK_C);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });


        JMenuItem exportItem = new JMenuItem("Export", null);
        exportItem.setMnemonic(KeyEvent.VK_C);
        exportItem.setToolTipText("Export to XYZ or PDB");
        exportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ExportFileChooser dialog = new ExportFileChooser((MainFrame.sharedFrame));

            }
        });
        file.add(resetMenuItem);
        file.add(exportItem);
        file.add(eMenuItem);

        menubar.add(file);

        JMenu envMenu = new JMenu("Environment");
        JMenuItem solvationItem = new JMenuItem("Solvate Box", null);
        solvationItem.setMnemonic(KeyEvent.VK_0);
        solvationItem.setToolTipText("Generate box of solvate");
        solvationItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SolvationDialog dialog = new SolvationDialog(MainFrame.sharedFrame);

            }
        });

        JMenuItem spatialItem = new JMenuItem("Spatial Sort", null);
        spatialItem.setToolTipText("Removes clashing expendable molecules");
        spatialItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                AppManager.sharedManager.getScene().environmentNode.executeSpatialSort();

            }
        });

        JMenuItem purgeItem = new JMenuItem("Purge Expendables", null);
        purgeItem.setToolTipText("Purge the environment of expendable molecules");
        purgeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane pane = new JOptionPane(
                        "You are about to delete all\nExpendable Molcules\nfrom the Environment");
                Object[] options = new String[]{"Cancle", "Purge"};
                pane.setOptions(options);
                JDialog dialog = pane.createDialog(new JFrame(), "Purge Expendables");
                dialog.setVisible(true);
                Object obj = pane.getValue();
                int result = -1;
                for (int k = 0; k < options.length; k++) {
                    if (options[k].equals(obj)) {
                        result = k;
                    }
                }
                if (result == 1) {
                    AppManager.sharedManager.getScene().environmentNode.purgeExpendables();
                }
            }
        });

        envMenu.add(solvationItem);
        envMenu.add(spatialItem);
        envMenu.add(purgeItem);
        menubar.add(envMenu);

        JMenuItem markerAddItem = new JMenuItem("Add Marker", null);
        markerAddItem.setToolTipText("Add a Marker to the Scene.");
        markerAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            }
        });

        JMenu dynMenu = new JMenu("Dynamics");
        JMenuItem physicItem = new JMenuItem("Physics Engine", null);
        physicItem.setMnemonic(KeyEvent.VK_0);
        physicItem.setToolTipText("Configure & Enable a integrator physics engine.");
        physicItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (physicsEngineDialog == null) {
                    physicsEngineDialog = new PhysicsEngineDialog();
                } else {
                    physicsEngineDialog.setVisible(true);
                }
            }
        });

        JMenuItem manageMarkerItem = new JMenuItem("Manage Markers", null);
        manageMarkerItem.setToolTipText("Manage Markers in the Scene.");
        manageMarkerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                if (markerManageDialog != null) {
                    markerManageDialog.dispose();
                }
                markerManageDialog = new MarkerManageDialog();

            }
        });

        dynMenu.add(physicItem);
        dynMenu.add(manageMarkerItem);
        menubar.add(dynMenu);

        JMenu measureMenu = new JMenu("Measure");
        JMenuItem densityItem = new JMenuItem("Density", null);
        densityItem.setToolTipText("Measure various molecular densities.");
        densityItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                DensityMeasureControl densityMeasureControl = new DensityMeasureControl();
            }
        });
        measureMenu.add(densityItem);

        JMenuItem distanceItem = new JMenuItem("Distance Conflicts", null);
        distanceItem.setToolTipText("Measure distance between atoms for conflicts.");
        distanceItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                DistanceConflictControl distanceConflict = new DistanceConflictControl(scene.environmentNode.molecules);
                distanceConflict.checkForConflicts();
            }
        });
        measureMenu.add(distanceItem);
        menubar.add(measureMenu);

        JMenu moleculeMenu = new JMenu("Molecule");
        JMenuItem importItem = new JMenuItem("Import", null);
        importItem.setMnemonic(KeyEvent.VK_C);
        importItem.setToolTipText("Import XYZ or PDB");
        importItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ImportFileChooser importChooser = new ImportFileChooser(MainFrame.sharedFrame);
            }
        });


        moleculeMenu.add(importItem);
        menubar.add(moleculeMenu);

        JMenu windowMenu = new JMenu("Window");
        JCheckBoxMenuItem sessionCheckBox = new JCheckBoxMenuItem("Session Window");
        sessionCheckBox.setEnabled(false);
        sessionCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            }
        });
        explorerCheckBox = new JCheckBoxMenuItem("Explorer Window");
        explorerCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) event.getSource();
                addExplorerPanel(item.isSelected());
            }
        });
        atomicCheckBox = new JCheckBoxMenuItem("Atomic Editor");
        atomicCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) event.getSource();
                addAtomicEditorPanel(item.isSelected());
            }
        });

        windowMenu.add(sessionCheckBox);
        windowMenu.add(atomicCheckBox);
        windowMenu.add(explorerCheckBox);
        menubar.add(windowMenu);
        setJMenuBar(menubar);

        //***//CANVAS
        scene.setSettings(settings);
        scene.createCanvas(); // create canvas!
        scene.setPauseOnLostFocus(false);
        ctx = (JmeCanvasContext) scene.getContext();
        ctx.getCanvas().setPreferredSize(defaultSize);
        mainPanel.add(ctx.getCanvas(), BorderLayout.WEST);
        ctx.getCanvas().validate();
        scene.startCanvas();
        //***//CANVAS

        popMenu = new PopMenu(this, new Point(0, 0));
        mainPanel.add(popMenu);

        final JPanel bottomPanel = new JPanel(new BorderLayout(2, 2));
        consoleArea = new ConsoleArea();
        
        JScrollPane scrollPane = new JScrollPane(consoleArea); 
        scrollPane.setPreferredSize(new Dimension(0,60));
        bottomPanel.add(scrollPane);

        Thread thread = new Thread(new Runnable() {
            public void run() {
                loadingPanel = new LoadingBar();
                bottomPanel.add(loadingPanel, BorderLayout.SOUTH);
            }
        });
        thread.start();

        

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    final public void addLoadingPanel(final int max) {

        loadingPanel.activate(max);


    }

    final void addExplorerPanel(boolean isSelected) {
        if (isSelected) {
            setSize(getSize().width + 200, getSize().height);
            mainPanel.add(explorerPanel = new ExplorerPanel(new ExplorerControl(), new Dimension(200, getSize().height)), BorderLayout.CENTER);
        } else {
            setSize(getSize().width - 200, getSize().height);
            explorerPanel.removeAll();
        }

        explorerCheckBox.setSelected(isSelected);
    }

    final void addAtomicEditorPanel(boolean isSelected) {

        if (isSelected) {
            setSize(getSize().width + 200, getSize().height);
            mainPanel.add(propertyPanel = new PropertyPanel(new Dimension(200, getSize().height)), BorderLayout.EAST);

        } else {
            setSize(getSize().width - 200, getSize().height);
            propertyPanel.removeAll();
        }

        atomicCheckBox.setSelected(isSelected);
    }

    public void updateMainFrameFromSceneThread(float tpf) {

        if (loadingPanel != null) {
            loadingPanel.updateLoadingPanelFromSceneThread(tpf);
        }
        if (propertyPanel != null) {
            propertyPanel.updatePropertyPanelFromSceneThread(tpf);
        }
    }

    public void didFinishLoadingScene() {
        addAtomicEditorPanel(true);
        addExplorerPanel(true);

        pack();
        setVisible(true);
    }

    public ExplorerPanel getExplorerPanel() {
        return explorerPanel;
    }

    public void setExplorerPanel(ExplorerPanel explorerPanel) {
        this.explorerPanel = explorerPanel;
    }

    public PropertyPanel getPropertyPanel() {
        return propertyPanel;
    }

    public void setPropertyPanel(PropertyPanel propertyPanel) {
        this.propertyPanel = propertyPanel;
    }

    public ConsoleArea getConsoleArea() {
        return consoleArea;
    }

    public void setConsoleArea(ConsoleArea consoleArea) {
        this.consoleArea = consoleArea;
    }
    
};
