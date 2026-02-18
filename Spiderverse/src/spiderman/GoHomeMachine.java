package spiderman;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }
        String inputDimension = args[0];
        String inputSpiderVerse = args[1];
        String inputHub = args[2];
        String anomaliesValue = args[3];
        String outputValue = args[4];
        StdOut.setFile(outputValue);

        VertexValue[] vertexValue = Collider.colliderCreator(inputDimension, inputSpiderVerse, outputValue);
        Collider.incrementPeopleValue(vertexValue, inputDimension, inputSpiderVerse, inputHub);

        StdIn.setFile(inputHub);
        int hub = StdIn.readInt();
        Dijkstra(vertexValue, hub);

        ArrayList<PersonValue> pathValues = getHome(vertexValue, hub, anomaliesValue);
        for(int i = 0; i < pathValues.size(); i++){
            StdOut.print(pathValues.get(i).getCanonNum() + " ");
            StdOut.print(pathValues.get(i).getName() + " ");
            if(pathValues.get(i).isSuccess()){
                StdOut.print("SUCCESS ");
            } else {
                StdOut.print("FAILED ");
            }
            ArrayList<Integer> valPath = pathValues.get(i).getPath();
            for(int j = 0; j < valPath.size(); j++){
                StdOut.print(valPath.get(j) + " ");
            }
            StdOut.println();
        }

        



        
        
    }

    public static void Dijkstra(VertexValue[] vertexValue, int hubValue){
        ArrayList<VertexValue> fringeValue = new ArrayList<VertexValue>();
        int index = TrackSpot.findIndexVertexValue(vertexValue, hubValue);
        vertexValue[index].setDistance(0);
        fringeValue.add(vertexValue[index]);
        while(!fringeValue.isEmpty()){
            VertexValue m = fringeValue.get(0);
            for(int i = 0; i< fringeValue.size(); i++){
                if (fringeValue.get(i).getDistance() < m.getDistance()) m = fringeValue.get(i);
            }
            int mIndex = TrackSpot.findIndexVertexValue(vertexValue, m.getDimension().getDimensionNumber());
            vertexValue[mIndex].setDijkstraMarked(true);

            ArrayList<DimensionValue> list = vertexValue[mIndex].getAjacencyList();

            for(int i = 0; i < list.size(); i++){
                int currentIndexValue = TrackSpot.findIndexVertexValue(vertexValue, list.get(i).getDimensionNumber());
                if(vertexValue[currentIndexValue].getDistance() == Integer.MAX_VALUE){
                    vertexValue[currentIndexValue].setDistance(m.getDistance()+m.getDimension().getWeight()+vertexValue[currentIndexValue].getDimension().getWeight());
                    fringeValue.add(vertexValue[currentIndexValue]);
                    vertexValue[currentIndexValue].setEdgetoVertex(vertexValue[mIndex]);
                } else if (vertexValue[currentIndexValue].getDistance() >m.getDistance()+m.getDimension().getWeight()+vertexValue[currentIndexValue].getDimension().getWeight()){
                    vertexValue[currentIndexValue].setDistance(m.getDistance()+m.getDimension().getWeight()+vertexValue[currentIndexValue].getDimension().getWeight());
                    vertexValue[currentIndexValue].setEdgetoVertex(vertexValue[mIndex]);
                }
            }

            for (int j =0; j< fringeValue.size(); j++){
                if (fringeValue.get(j) == m) fringeValue.remove(j);
            }
            
        }
    }

    public static int getCanon(VertexValue[] vertexValue, int dim){
        
        for (int i = 0; i<vertexValue.length;i++){
            if(vertexValue[i].getDimension().getDimensionNumber() == dim) return vertexValue[i].getDimension().getCanonEvents();
        }
        return -1;
    }

    public static int getDimSig (VertexValue[] vertexValue, String name){
        for (int i = 0; i<vertexValue.length;i++){
            ArrayList<PersonValue> personValues = vertexValue[i].getDimension().getPeople();
            for (int j = 0; j<personValues.size(); j++){
                if(personValues.get(j).getName().equalsIgnoreCase(name)) return personValues.get(j).getDimSig();
            }
        }
        return -1;
    }

    public static ArrayList<Integer> PathHome (VertexValue[] vertexValue, int hub, int dimensionSignature ){
        ArrayList<Integer> path = new ArrayList<>();
        int goalIndex = TrackSpot.findIndexVertexValue(vertexValue, dimensionSignature);
        int hubIndex = TrackSpot.findIndexVertexValue(vertexValue, hub);
        VertexValue hubVertex = vertexValue[hubIndex];
        VertexValue pointer = vertexValue[goalIndex];
        path.add(dimensionSignature);

        while(pointer != hubVertex){
            int dim = pointer.getDimension().getDimensionNumber();
            path.add(dim);
            VertexValue ptr2 = pointer.getEdgetoVertex();
            int ptr2Index = TrackSpot.findIndexVertexValue(vertexValue, ptr2.getDimension().getDimensionNumber());
            pointer = vertexValue[ptr2Index];
        }
        path.add(hub);

        ArrayList<Integer> path2 = new ArrayList<>();
        for(int i = path.size()-1; i >= 0; i--){
            path2.add(path.get(i));
        }
        return path2;

    }

    public static ArrayList<PersonValue> getHome(VertexValue[] vertexValue, int hub, String anomaliesValue){
        ArrayList<PersonValue> paths = new ArrayList<>();
        StdIn.setFile(anomaliesValue);
        int numberAnomaliesValue = StdIn.readInt();
        for (int i = 0; i<numberAnomaliesValue; i++){
            String nameValue = StdIn.readString();
            int time = StdIn.readInt();
            int dimensionSignature = getDimSig(vertexValue, nameValue);
            int dimensionCanonValue = getCanon(vertexValue, dimensionSignature);
            ArrayList<Integer> anomalyPathValue = PathHome(vertexValue, hub, dimensionSignature);

            int timeUsed = vertexValue[TrackSpot.findIndexVertexValue(vertexValue, dimensionSignature)].getDistance();
            anomalyPathValue.remove(anomalyPathValue.size()-1);
            PersonValue anomalyValue = new PersonValue(nameValue, anomalyPathValue);
            paths.add(anomalyValue);
            if(timeUsed > time){
                anomalyValue.setSuccess(false);
                anomalyValue.setCanonNum(dimensionCanonValue-1);
            } else {
                anomalyValue.setSuccess(true);
                anomalyValue.setCanonNum(dimensionCanonValue);
            }

        }

        return paths;
    }

}
