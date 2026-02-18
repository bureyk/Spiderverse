package spiderman;

import java.util.ArrayList;

public class PersonValue {
    int dimensionSignature;
    String nameValue;
    int currentDim;
    ArrayList<Integer> pathvalue = new ArrayList<Integer>();
    String spiderNameValue;
    boolean successValue;
    int canonNumbervalue;

    public int getCanonNum() {
        return canonNumbervalue;
    }

    public void setCanonNum(int canonNum) {
        this.canonNumbervalue = canonNum;
    }

    public boolean isSuccess() {
        return successValue;
    }

    public void setSuccess(boolean success) {
        this.successValue = success;
    }

    public String getSpiderName() {
        return spiderNameValue;
    }

    public void setSpiderName(String spiderName) {
        this.spiderNameValue = spiderName;
    }

    public int getDimSig() {
        return dimensionSignature;
    }
    public void setDimSig(int dimSig) {
        this.dimensionSignature = dimSig;
    }
    public String getName() {
        return nameValue;
    }
    public void setName(String name) {
        this.nameValue = name;
    }
    public int getCurrentDim() {
        return currentDim;
    }
    public void setCurrentDim(int currentDim) {
        this.currentDim = currentDim;
    }

    public PersonValue(int dimSig, String name, int currentDim) {
        this.dimensionSignature = dimSig;
        this.nameValue = name;
        this.currentDim = currentDim;
    }

    public PersonValue(String name, ArrayList<Integer> path) {
        this.nameValue = name;
        this.pathvalue = path;
    }

    public ArrayList<Integer> getPath() {
        return pathvalue;
    }

    public void setPath(ArrayList<Integer> path) {
        this.pathvalue = path;
    }

    public void addPath(int path) {
        this.pathvalue.add(path);
    }


    
}
