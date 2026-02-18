package spiderman;

import java.util.ArrayList;

public class DimensionValue {
    int dimensionNumber;
    int eventsCanon;
    int weightValue;
    ArrayList<PersonValue> peopleList;
    boolean markedValue;
    DimensionValue nextValue;

    public DimensionValue(int dimensionNumber, int eventsCanon, int weightValue, DimensionValue nextValue) {
        this.dimensionNumber = dimensionNumber;
        this.eventsCanon = eventsCanon;
        this.weightValue = weightValue;
        this.markedValue = false;
        peopleList = new ArrayList<PersonValue>();
        this.nextValue = nextValue;
    }
    public DimensionValue(int dimNum, int canonEvents, int weight, ArrayList<PersonValue> people, DimensionValue next) {
        this.dimensionNumber = dimNum;
        this.eventsCanon = canonEvents;
        this.weightValue = weight;
        this.peopleList = people;
        this.nextValue = next;
    }

    public DimensionValue getNext(int j){
        DimensionValue pointer = this;
        for(int i = 0; i < j; i++){
            pointer = pointer.nextValue;
        }
        return pointer;
    }

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public void setDimensionNumber(int dimNum) {
        this.dimensionNumber = dimNum;
    }

    public int getCanonEvents() {
        return eventsCanon;
    }

    public void setCanonEvents(int canonEvents) {
        this.eventsCanon = canonEvents;
    }

    public int getWeight() {
        return weightValue;
    }

    public void setWeight(int weight) {
        this.weightValue = weight;
    }

    public DimensionValue getNext() {
        return nextValue;
    }

    public void setNext(DimensionValue next) {
        this.nextValue = next;
    }

    public ArrayList<PersonValue> getPeople() {
        return peopleList;
    }

    public void setPeople(ArrayList<PersonValue> people) {
        this.peopleList = people;
    }

    public void toString(String colliderOutputFile) {
        StdOut.println("Dimension: " + dimensionNumber + " CanonEvents: " + eventsCanon + " Weight: " + weightValue);
    }

    public void addPerson(PersonValue p) {
        peopleList.add(p);
    }



}
