/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.control;

import com.jme3.math.Vector3f;

/**
 *
 * @author Oliver
 */
public class PhysicsEngineState {

    private boolean debugDraw;
    private float accuracy;
    private float numOfSteps;
    private float speed;
    private float collisionBoxSize;
    private CollisionDetail collisionDetail;
    private CollisionBroadPhase collisionPhase;
    private Vector3f gravity;
 
    public enum CollisionDetail {
        FAST,
        NORMAL,
        SLOW;

        public int getOrdinal() {
            if (this == FAST) {
                return 0;
            } else if (this == NORMAL) {
                return 1;
            } else if (this == SLOW) {
                return 2;
            }
            return -1;
        }
    }

    public enum CollisionBroadPhase {
        DBVT,
        SIMPLE,
        AXIS_32,
        AXIS
    }
    
    
   PhysicsEngineState(boolean debugDraw, float accuracy, int numOfSteps, float speed, Vector3f gravityVector3f, float collisionBoxSize, CollisionDetail collisionDetail, CollisionBroadPhase collisionBroadPhase) {
        super();
        this.debugDraw = debugDraw;
        this.accuracy = accuracy;
        this.numOfSteps = numOfSteps;
        this.speed = speed;
        this.collisionDetail = collisionDetail;
        this.collisionPhase = collisionBroadPhase;
        this.collisionBoxSize = collisionBoxSize;
        this.gravity = gravityVector3f;
    }

    public float getCollisionBoxSize() {
        return collisionBoxSize;
    }

    public void setCollisionBoxSize(float collisionBoxSize) {
        this.collisionBoxSize = collisionBoxSize;
    }

    synchronized public void updateFromPhysicsEngineThread() {
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    public boolean isDebugDraw() {
        return debugDraw;
    }

    public void setDebugDraw(boolean debugDraw) {
        this.debugDraw = debugDraw;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getNumOfSteps() {
        return numOfSteps;
    }

    public void setNumOfSteps(float numOfSteps) {
        this.numOfSteps = numOfSteps;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public CollisionDetail getCollisionDetail() {
        return collisionDetail;
    }

    public void setCollisionDetail(CollisionDetail collisionDetail) {
        this.collisionDetail = collisionDetail;
    }

    public CollisionBroadPhase getCollisionPhase() {
        return collisionPhase;
    }

    public void setCollisionPhase(CollisionBroadPhase collisionPhase) {
        this.collisionPhase = collisionPhase;
    }
}
