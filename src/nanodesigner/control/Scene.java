package nanodesigner.control;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;
import com.jme3.system.AppSettings;
import java.util.concurrent.Callable;
import nanodesigner.control.PhysicsEngineState.CollisionBroadPhase;

import nanodesigner.manager.AppManager;
import nanodesigner.model.AtomModel;

import nanodesigner.model.EnvironmentNode;
import nanodesigner.model.MoleculeNode;
import nanodesigner.tool.MousePointer;
import nanodesigner.view.ConsoleArea;

public final class Scene extends SimpleApplication {

    public EnvironmentNode environmentNode = null;
    public ChaseCamera chaseCam = null;
    public MoleculeNode targetMoleculeNode = null;
    public AtomModel targetAtomModel = null;
    public Geometry wireBoxGeometry = null;
    public Geometry wireGridGeometry = null;
    PointLight lamp_light = null;
    private BulletAppState bulletAppState;
    private boolean physicsEnabled;
    private PhysicsEngineState physicsEngineState;
    public MousePointer mousePointer = null;
    
    
    @Override
    public void simpleInitApp() {
        initEnvironment();
        initCamera();
        initCoordinateAxis();
        AppManager.sharedManager.didFinishLoadingScene(this);
        mousePointer = new MousePointer(this);
        ConsoleArea.Log("Renderer: LWJGL 2.8.4... ");
        ConsoleArea.Log("Scene Thread Booted");
    }
    
    public AppSettings getSettings(){
       
        return settings;
    }

    private void initEnvironment() {
        environmentNode = new EnvironmentNode(this);
        rootNode.attachChild(environmentNode);

        //  NDAngleReader reader = new NDAngleReader();
        //  reader.readDATFile("/Users/Oliver/CNTWater90_50kcm_trials/CNTWater90_50kcm_DC_4.dat");
        //   ArrayList<Float> dangles = reader.calculateDeltaAngles();
        //  ArrayList<Float> tangles = reader.calculateTotalAngles();
        //  NDWriter writer = new NDWriter();
        //  writer.writeFloatListToColumn("Delta Angle", dangles, "/Users/Oliver/angle.nda", true);
        //  writer.writeFloatListToColumn("Total Angle", tangles, "/Users/Oliver/CNTWater90_50kcm_trials/CNTWater90_50kcm_DC_4.nda", true);


    }

    public void resetScene() {
        System.out.println("Resetting Scene");
        environmentNode.trashMolecules.addAll(environmentNode.molecules);
        environmentNode.emptyTrash();
        chaseCam.setSpatial(rootNode);
        ConsoleArea.LogLn("Resetting Scene");
    }


    private void initCoordinateAxis() {
        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        arrow.setLineWidth(3); // make arrow thicker
        putShape(arrow, ColorRGBA.Red).setLocalTranslation(0, 0, 0);

        arrow = new Arrow(Vector3f.UNIT_Y);
        arrow.setLineWidth(3); // make arrow thicker
        putShape(arrow, ColorRGBA.Green).setLocalTranslation(0, 0, 0);

        arrow = new Arrow(Vector3f.UNIT_Z);
        arrow.setLineWidth(3); // make arrow thicker
        putShape(arrow, ColorRGBA.Blue).setLocalTranslation(0, 0, 0);
    }

    private Geometry putShape(Mesh shape, ColorRGBA color) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }

    public void initWireBox(BoundingBox box, ColorRGBA color) {
        wireBoxGeometry = new Geometry("wireframe cube", new WireBox(box.getXExtent(), box.getYExtent(), box.getZExtent()));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        wireBoxGeometry.setMaterial(mat);
        wireBoxGeometry.setLocalTranslation(box.getCenter());
        rootNode.attachChild(wireBoxGeometry);
    }

    private void initWireGrid(Vector3f pos, int size, ColorRGBA color) {
        wireGridGeometry = new Geometry("wireframe grid", new Grid(100, 100, 10));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        wireGridGeometry.setMaterial(mat);
        wireGridGeometry.center().move(pos);
        rootNode.attachChild(wireGridGeometry);
    }

    private void initCamera() {
        // viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

        flyCam.setDragToRotate(true);
        flyCam.setEnabled(false);

        chaseCam = new ChaseCamera(cam, rootNode, inputManager);
        chaseCam.setMaxDistance(2000);
        chaseCam.setMinDistance(10);
        chaseCam.setDefaultDistance(500);
        chaseCam.setSmoothMotion(true);
        chaseCam.setTrailingEnabled(false);
        chaseCam.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        rootNode.addLight(sun);


        DirectionalLight sun2 = new DirectionalLight();
        sun2.setColor(ColorRGBA.White);
        sun2.setDirection(new Vector3f(.5f, .5f, .5f).normalizeLocal());
        rootNode.addLight(sun2);

    }

    synchronized public void setChaseCamSpatial(final Spatial spatial) {
        enqueue(new Callable() {
            @Override
            public Void call() {
                chaseCam.setSpatial(spatial);
                if (spatial instanceof MoleculeNode) {
                    MoleculeNode mol = (MoleculeNode) spatial;
                }

                return null;
            }
        });

    }

    synchronized public void addRigidBodyToPhysicSpace(final RigidBodyControl rigidBodyControl) {
        enqueue(new Callable() {
            @Override
            public Void call() {
                bulletAppState.getPhysicsSpace().add(rigidBodyControl);
                return null;
            }
        });

    }

    /**
     * Scene Physics are defined*
     */
    private void initPhysics() {
        ConsoleArea.LogLn("Initialising Physics Engine... ");
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        PhysicsSpace.BroadphaseType broadphaseType = null;
        if (physicsEngineState.getCollisionPhase() == CollisionBroadPhase.DBVT) {
            broadphaseType = PhysicsSpace.BroadphaseType.DBVT;
        } else if (physicsEngineState.getCollisionPhase() == CollisionBroadPhase.SIMPLE) {
            broadphaseType = PhysicsSpace.BroadphaseType.SIMPLE;
        } else if (physicsEngineState.getCollisionPhase() == CollisionBroadPhase.AXIS) {
            broadphaseType = PhysicsSpace.BroadphaseType.AXIS_SWEEP_3;
        } else if (physicsEngineState.getCollisionPhase() == CollisionBroadPhase.AXIS_32) {
            broadphaseType = PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32;
        }
        bulletAppState.setBroadphaseType(broadphaseType);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0.0f, 0.0f, 0.0f));
        bulletAppState.getPhysicsSpace().setAccuracy(physicsEngineState.getAccuracy());
        bulletAppState.setSpeed(physicsEngineState.getSpeed());
        if (physicsEngineState.isDebugDraw()) {
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        }
        ConsoleArea.Log("Attaching Physics Bodies to Atoms... ");
        environmentNode.initialisePhysicBodies();  //enable the physic bodies down the heirachy
        ConsoleArea.Log("Done!");
    }

    private void destroyPhysics() {
        ConsoleArea.LogLn("Stopping Physics Engine... ");
        bulletAppState.setEnabled(false);
        bulletAppState.getPhysicsSpace().clearForces();
        this.environmentNode.destroyPhysicBodies();
        bulletAppState.stateDetached(stateManager);
        bulletAppState.getPhysicsSpace().destroy();
        ConsoleArea.Log("Done!");
    }

    public boolean isPhysicsEnabled() {
        return physicsEnabled;
    }

    public PhysicsEngineState getPhysicsEngineState() {
        return physicsEngineState;
    }

    public void setPhysicsEngineState(final PhysicsEngineState physicsEngineState) {
        this.physicsEngineState = physicsEngineState;

        if (physicsEnabled) {
            //If the physics are already enabled then when the physicsEngineState updates, we must update
            //The physics engine:

            enqueue(new Callable() {
                @Override
                public Void call() {
                    bulletAppState.setSpeed(physicsEngineState.getSpeed());
                    bulletAppState.getPhysicsSpace().setGravity(physicsEngineState.getGravity());
                    return null;
                }
            });
        }
    }

    synchronized public void setPhysicsEnabled(final boolean enable, final PhysicsEngineState physicsEngineState) {
        AppManager.getSharedManager().getMainFrame().addLoadingPanel(this.environmentNode.getMolecules().size());
        this.physicsEngineState = physicsEngineState;

        enqueue(new Callable() {
            @Override
            public Void call() {
                physicsEnabled = enable;
                if (physicsEnabled == true) {
                    initPhysics();
                    environmentNode.reinitialiseMarkers();

                } else {
                    destroyPhysics();
                }
                return null;
            }
        });
    }

    @Override
    public void simpleUpdate(float tpf) {
        //We continue updates down to the AppManager and feed them to GUIManager
        //This should be revised for modular updates.
        AppManager.sharedManager.updateAppManagerFromSceneThread(tpf);
        this.environmentNode.updateEnvironmentFromSceneThread();
        if (wireBoxGeometry != null) {
        }
    }
        

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public EnvironmentNode getEnvironmentNode() {
        return environmentNode;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }
}
