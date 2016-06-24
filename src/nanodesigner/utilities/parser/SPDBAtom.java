/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nanodesigner.utilities.parser;

/**
 *
 * @author Oliver
 */
public class SPDBAtom {

    private String serial, name, altLoc, resName, chainId, reqSeq, iCode, occupancy, tempFactor, charge;
    private float x,y,z;

    public SPDBAtom(String serial, String name, String altLoc, String resName, String chainId, String reqSeq, String iCode, String occupancy, String tempFactor, String charge, float x, float y, float z) {
        this.serial = serial;
        this.name = name;
        this.altLoc = altLoc;
        this.resName = resName;
        this.chainId = chainId;
        this.reqSeq = reqSeq;
        this.iCode = iCode;
        this.occupancy = occupancy;
        this.tempFactor = tempFactor;
        this.charge = charge;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAltLoc() {
        return altLoc;
    }

    public void setAltLoc(String altLoc) {
        this.altLoc = altLoc;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getReqSeq() {
        return reqSeq;
    }

    public void setReqSeq(String reqSeq) {
        this.reqSeq = reqSeq;
    }

    public String getiCode() {
        return iCode;
    }

    public void setiCode(String iCode) {
        this.iCode = iCode;
    }

    public String getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(String occupancy) {
        this.occupancy = occupancy;
    }

    public String getTempFactor() {
        return tempFactor;
    }

    public void setTempFactor(String tempFactor) {
        this.tempFactor = tempFactor;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
    
    
    
}
