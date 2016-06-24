/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.model;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import nanodesigner.control.Scene;
import nanodesigner.manager.AppManager;
import nanodesigner.view.ConsoleArea;

/**
 *
 * @author Oliver
 */
public final class EnvironmentNode extends BatchNode {
    
    private Scene scene = null;
    public final ArrayList<MoleculeNode> molecules = new ArrayList<MoleculeNode>();
    public ArrayList<MoleculeNode> trashMolecules = new ArrayList<MoleculeNode>();
    public final ArrayList<MarkerModel> markers = new ArrayList<MarkerModel>();
    
    public EnvironmentNode(Scene scene) {
        super("Environment Node");
        this.scene = scene;
              
    }
    
    private void createPlane(Vector3f normalVector3f){
       PlaneCollisionShape plane    = new PlaneCollisionShape(new Plane(Vector3f.UNIT_X, 0));
       RigidBodyControl rigid       = new RigidBodyControl(plane, 1.0f);
       this.addControl(rigid);
       AppManager.sharedManager.getScene().addRigidBodyToPhysicSpace(rigid);
    }

    private void createPhysicBox(Vector3f vector){
        BoxCollisionShape box = new BoxCollisionShape(vector);
        RigidBodyControl rigid = new RigidBodyControl(box, 0.0f);
        this.addControl(rigid);
        AppManager.sharedManager.getScene().addRigidBodyToPhysicSpace(rigid);
    }
    
    public void initialisePhysicBodies(){
        for (int i = 0; i < this.molecules.size(); i++){
            MoleculeNode moleculeNode = (MoleculeNode)this.molecules.get(i);
            moleculeNode.initialisePhysicBodies();  
            
             if (AppManager.getSharedManager().getMainFrame().loadingPanel != null){
                    AppManager.getSharedManager().getMainFrame().loadingPanel.incrementProgressBar();
              }
        }
    }
    
    public void reinitialiseMarkers(){
        for (MarkerModel marker : this.markers){
            marker.reinitialise();
        }
    }
    
    public void destroyPhysicBodies() {    
        for (int i = 0; i < this.molecules.size(); i++){
            MoleculeNode moleculeNode = (MoleculeNode)this.molecules.get(i);
            moleculeNode.destroyPhysicBodies();  
            
            if (AppManager.getSharedManager().getMainFrame().loadingPanel != null){
                AppManager.getSharedManager().getMainFrame().loadingPanel.incrementProgressBar();
            }
        }
   }

    public void emptyTrash(){
        for (int i = 0; i < trashMolecules.size(); i++){
            MoleculeNode trashMol = (MoleculeNode)trashMolecules.get(i);
            removeMoleculeNode(trashMol);
        }
        for (int i = 0; i < markers.size(); i++){
            MarkerModel marker = markers.get(i);
            marker.molecules.removeAll(trashMolecules);
            
        }
        molecules.removeAll(trashMolecules);
        trashMolecules.clear();
              
        if (scene.wireBoxGeometry != null){
            scene.wireBoxGeometry.removeFromParent();
        }
        AppManager.sharedManager.getMainFrame().getExplorerPanel().getControl().populateMoleculeList();
    }
    
    synchronized public void addMarkerModel(final MarkerModel markerModel) {
        ConsoleArea.LogLn("Marker: " + markerModel.identifier + ": Adding at {" + markerModel.getWorldTranslation().x + " " + markerModel.getWorldTranslation().y + " " + markerModel.getWorldTranslation().z + "}");
        markers.add(markerModel);
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                attachChild(markerModel);
                return null;
            }
        });
    }

    synchronized public void removeMarkerModel(final MarkerModel markerModel) {
        markers.remove(markerModel);
        ConsoleArea.LogLn("Marker: " + markerModel.identifier + ": Removing");
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                markerModel.removeFromParent();
                return null;
            }
        });
    }
    
    synchronized public void addLineBetweenVectors(Vector3f[] lineVerticies){
        Mesh m = new Mesh();
        m.setMode(Mesh.Mode.Lines);


        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

        short[] indexes=new short[2*lineVerticies.length]; //Indexes are in pairs, from a vertex and to a vertex
        m.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });
        m.updateBound();
        m.updateCounts();

        final Geometry geo=new Geometry("line",m);
        
        Material mat = new Material(AppManager.getSharedManager().getScene().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Cyan);
        geo.setMaterial(mat);

        scene.enqueue(new Callable() {
            @Override
            public Void call() {
            AppManager.getSharedManager().getScene().getRootNode().attachChild(geo);
            return null;
            }
        });
    }
    
    synchronized public void addMoleculeNode(final MoleculeNode moleculeNode) {
        /*Optimizing a batchNode at this level can cause problems with the physics engine*/
        molecules.add(moleculeNode);
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                attachChild(moleculeNode);
                
                if (AppManager.getSharedManager().getMainFrame().loadingPanel != null){
                    AppManager.getSharedManager().getMainFrame().loadingPanel.incrementProgressBar();
                }
                return null;
            }
        });
        batch();
        //Update the ExplorerView
        AppManager.sharedManager.getMainFrame().getExplorerPanel().getControl().populateMoleculeList();
    }
    
    synchronized private void removeMoleculeNode(final MoleculeNode moleculeNode){                 
        //Where is teh molecules.remove(moleculeNode)???
        
        scene.enqueue(new Callable() {
            @Override
            public Void call() {
                moleculeNode.removeFromParent();
                return null;
            }
        });     
    }
    
    public void updateEnvironmentFromSceneThread() {
        for (int i = 0; i < markers.size(); i++){
            MarkerModel marker = (MarkerModel)markers.get(i);
            marker.updateFromSceneThread();     
        }
    }

    public void executeSpatialSort() {
        ConsoleArea.LogLn("Spatial Sort: Executing... ");
        for (int m = 0; m < molecules.size(); m++){
            for (int e = 0; e < molecules.size(); e++){
                if (e != m){
                    MoleculeNode mMol = molecules.get(m);
                    MoleculeNode eMol = molecules.get(e);
                    
                    if (!mMol.isExpendable() && eMol.isExpendable()){

                        BoundingBox mBoundingBox = (BoundingBox) mMol.getWorldBound();
                        BoundingBox eBoundingBox = (BoundingBox) eMol.getWorldBound();

                        if (mBoundingBox.intersectsBoundingBox(eBoundingBox)){
                           trashMolecules.add(eMol);  
                        }   
                    }
                }
            } 
        }
        ConsoleArea.Log("Removing " + trashMolecules.size() + " conflicting 'expendable' Molecules... Done!");
        emptyTrash();
    }
    
    public ArrayList<String> getSegmentStrings(){
        ArrayList<String> segments = new ArrayList<String>();  
        for (MoleculeNode mol : this.molecules){  
            if (!segments.contains(mol.getChainID())){
                segments.add(mol.getChainID());
            }
        }
        return segments;        
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    
    public final ArrayList<MoleculeNode> getMolecules() {
        return molecules;
    }

    public void purgeExpendables() {
        ConsoleArea.LogLn("Removing all 'expendable' molecules... Done!");
        for (int i = 0; i < molecules.size(); i++){
            MoleculeNode mol = molecules.get(i);
            if (mol.isExpendable()){
                trashMolecules.add(mol);
            }
        }
        emptyTrash();
    } 
}