package spiderman;
import java.util.ArrayList;

public class HashTable {
    DimensionValue[] dimensionValueClusters;
    int clustersNumber; // number of clusters

    public HashTable( HashTable clusters, int numClusters) {
        this.dimensionValueClusters = clusters.getClusters();
        this.clustersNumber = numClusters;
    }

    public HashTable(int size) {
        dimensionValueClusters = new DimensionValue[size];
        clustersNumber = 0;
    }

    public HashTable(int size, int numClusters) {
        dimensionValueClusters = new DimensionValue[size];
        this.clustersNumber = numClusters;
    }

    public HashTable(String fileName) {
        StdIn.setFile(fileName);
        int size = StdIn.readInt();
        dimensionValueClusters = new DimensionValue[size];
        clustersNumber = 0;
    }

    public int getNumClusters() {
        return clustersNumber;
    }

    public int getSize() {
        return dimensionValueClusters.length;
    }

    public DimensionValue[] getClusters() {
        return dimensionValueClusters;
    }

    public DimensionValue get(int index) {
        return dimensionValueClusters[index];
    }

    public void set(int index, DimensionValue value) {
        dimensionValueClusters[index] = value;
    }

    public void add(DimensionValue value) {
        if(dimensionValueClusters[value.dimensionNumber % dimensionValueClusters.length] == null){
            clustersNumber++;
        }
        value.setNext(dimensionValueClusters[value.dimensionNumber % dimensionValueClusters.length]);
        dimensionValueClusters[value.dimensionNumber % dimensionValueClusters.length] = value;
    }

    public void addAtIndex(int index, DimensionValue value) {
        if(dimensionValueClusters[index] == null){
            clustersNumber++;
        }
        value.setNext(dimensionValueClusters[index]);
        dimensionValueClusters[index] = value;
    }

    public int getIndexSize(int index) {
        int count = 0;
        DimensionValue pointer = dimensionValueClusters[index];
        while(pointer != null){
            count++;
            pointer = pointer.getNext();
        }
        return count;
    }

    public void rehashFunction(){
        clustersNumber = 0;
        DimensionValue[] temp  = new DimensionValue[2 * dimensionValueClusters.length];
        DimensionValue ptr = null;
        for(int i = 0; i < dimensionValueClusters.length; i++){
            ptr = dimensionValueClusters[i];
            while(ptr != null){
                if(temp[ptr.dimensionNumber % temp.length] == null){
                    clustersNumber++;
                }
                DimensionValue next = ptr.getNext();
                DimensionValue tempPtr =  new DimensionValue(ptr.dimensionNumber, ptr.eventsCanon, ptr.weightValue,  temp[ptr.dimensionNumber % temp.length]);
                temp[ptr.dimensionNumber % temp.length] = tempPtr;
                ptr = next;
            }
        }
        dimensionValueClusters = temp;
    }

    public void print(String fileName) {
        StdOut.setFile(fileName);
        for(int i = 0; i < dimensionValueClusters.length; i++){
            DimensionValue ptr = dimensionValueClusters[i];
            while(ptr != null){
                StdOut.print(ptr.dimensionNumber + " ");
                ptr = ptr.getNext();
            }
            StdOut.println();
        }
    }

    public void connectDimensions(){
        for(int i = 0; i < dimensionValueClusters.length; i++){
            DimensionValue t1 = dimensionValueClusters[((i-1) % dimensionValueClusters.length + dimensionValueClusters.length ) % dimensionValueClusters.length];
            DimensionValue temp1 = new DimensionValue(t1.dimensionNumber, t1.eventsCanon, t1.weightValue, null);
            DimensionValue t2 = dimensionValueClusters[((i-2) % dimensionValueClusters.length + dimensionValueClusters.length ) % dimensionValueClusters.length];
            DimensionValue temp2 = new DimensionValue(t2.dimensionNumber, t2.eventsCanon, t2.weightValue, null);
            DimensionValue ptr = dimensionValueClusters[i];
            while(ptr.nextValue != null){
                ptr = ptr.getNext();
            }
            ptr.setNext(temp1);
            ptr.nextValue.setNext(temp2);


        }
    }

    public void concatenate(DimensionValue[] bottom){
        HashTable temp = new HashTable(dimensionValueClusters.length + bottom.length, clustersNumber);
        for(int i = 0; i < dimensionValueClusters.length; i++){
            temp.set(i, dimensionValueClusters[i]);
        }
        for(int i = 0; i < bottom.length; i++){
            temp.set(i + dimensionValueClusters.length, bottom[i]);
        }
        dimensionValueClusters = temp.dimensionValueClusters;
    }

    public void concatenate(HashTable bottom){
        HashTable temp = new HashTable(dimensionValueClusters.length + bottom.getSize(), clustersNumber);
        for(int i = 0; i < dimensionValueClusters.length; i++){
            temp.set(i, dimensionValueClusters[i]);
        }
        for(int i = 0; i < bottom.getSize(); i++){
            temp.set(i + dimensionValueClusters.length, bottom.get(i));
        }
        dimensionValueClusters = temp.dimensionValueClusters;
    }

    public void concatenate(ArrayList<DimensionValue> bottom){
        HashTable temp = new HashTable(dimensionValueClusters.length + bottom.size(), clustersNumber);
        for(int i = 0; i < dimensionValueClusters.length; i++){
            temp.set(i, dimensionValueClusters[i]);
        }
        for(int i = 0; i < bottom.size(); i++){
            temp.set(i + dimensionValueClusters.length, bottom.get(i));
        }
        dimensionValueClusters = temp.dimensionValueClusters;
    }

    public void addPerson(PersonValue p){
        dimensionValueClusters[p.getDimSig() % dimensionValueClusters.length].addPerson(p);
    }

    public void printCommandLine(){
        for(int i = 0; i < dimensionValueClusters.length; i++){
            DimensionValue ptr = dimensionValueClusters[i];
            while(ptr != null){
                StdOut.print(ptr.dimensionNumber + " ");
                ptr = ptr.getNext();
            }
        }
    }

    public int getIndex(int dimNum){
        return dimNum % dimensionValueClusters.length;
    }

    public void addPrevDimensions(){
        for(int i = 0; i < dimensionValueClusters.length; i++){
            DimensionValue pointer = dimensionValueClusters[i];
            DimensionValue nextList = pointer.getNext();
            DimensionValue textTwo = dimensionValueClusters[((i+2) % dimensionValueClusters.length + dimensionValueClusters.length ) % dimensionValueClusters.length];
            DimensionValue tempTwo = new DimensionValue(textTwo.dimensionNumber, textTwo.eventsCanon, textTwo.weightValue, null);
            DimensionValue textOne = dimensionValueClusters[((i+1) % dimensionValueClusters.length + dimensionValueClusters.length ) % dimensionValueClusters.length];
            DimensionValue tempOne = new DimensionValue(textOne.dimensionNumber, textOne.eventsCanon, textOne.weightValue, null);
            tempTwo.setNext(nextList);
            tempOne.setNext(tempTwo);
            pointer.setNext(tempOne);
            


        }

    }




}


