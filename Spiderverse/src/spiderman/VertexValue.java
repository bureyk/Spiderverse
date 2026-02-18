package spiderman;

import java.util.ArrayList;

public class VertexValue {
    boolean valueMarked;
    VertexValue vertexEdgeTo;
    int distanceValue;
    DimensionValue dimensionValue;
    ArrayList<DimensionValue> ajacencyList;
    boolean markedDijkstra;
    

    public VertexValue(DimensionValue dimensionValue, ArrayList<DimensionValue> ajacencyList) {
        this.dimensionValue = dimensionValue;
        this.ajacencyList = ajacencyList;
        valueMarked = false;
        vertexEdgeTo = null;
        distanceValue = Integer.MAX_VALUE;
        this.markedDijkstra = false;
    }

    public boolean isDijkstraMarked() {
        return markedDijkstra;
    }

    public void setDijkstraMarked(boolean dijkstraMarked) {
        markedDijkstra = dijkstraMarked;
    }

    public boolean isMarked() {
        return valueMarked;
    }
    public void setMarked(boolean marked) {
        this.valueMarked = marked;
    }
    public VertexValue getEdgetoVertex() {
        return vertexEdgeTo;
    }
    public void setEdgetoVertex(VertexValue edgetoVertex) {
        this.vertexEdgeTo = edgetoVertex;
    }
    public int getDistance() {
        return distanceValue;
    }
    public void setDistance(int distance) {
        this.distanceValue = distance;
    }
    public DimensionValue getDimension() {
        return dimensionValue;
    }
    public void setDimension(DimensionValue dimensionValue) {
        this.dimensionValue = dimensionValue;
    }
    public ArrayList<DimensionValue> getAjacencyList() {
        return ajacencyList;
    }
    public void setAjacencyList(ArrayList<DimensionValue> ajacencyList) {
        this.ajacencyList = ajacencyList;
    }

    public void addToAList(DimensionValue d) {
        this.ajacencyList.add(d);
    }

    




}
