package spiderman;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        String inputDimension = args[0];
        String inputSpiderVerse = args[1];
        String inputHub = args[2];
        String outputCollected = args[3];
        StdIn.setFile(inputHub);
        int hubStartingValue = StdIn.readInt();
        StdOut.setFile(outputCollected);
        
        VertexValue[] vertexValue = Collider.colliderCreator(inputDimension, inputSpiderVerse, outputCollected);
        Collider.incrementPeopleValue(vertexValue, inputDimension, inputSpiderVerse, outputCollected);

        
        bfs(vertexValue, hubStartingValue);

        ArrayList<PersonValue> anomaliesValue = checkAnomalies(vertexValue, hubStartingValue);
        for(int i =0; i< anomaliesValue.size(); i++){
            StdOut.print(anomaliesValue.get(i).getName() + " ");
            if(anomaliesValue.get(i).getSpiderName() != null){
                StdOut.print(anomaliesValue.get(i).getSpiderName() + " ");
            }
            for(int j = 0; j < anomaliesValue.get(i).getPath().size(); j++){
                StdOut.print(anomaliesValue.get(i).getPath().get(j) + " ");
            }
            StdOut.println();
        }


    }

    public static int getVertexIndex(VertexValue[] vertexValue, int dimension) {
        for(int i = 0; i < vertexValue.length; i++) {
            if(vertexValue[i].getDimension().getDimensionNumber() == dimension) {
                return i;
            }
        }
        return -1;
    }

    public static void bfs(VertexValue[] vertexValue, int startValue) {
        int initialIndex = getVertexIndex(vertexValue, startValue);
        Queue<VertexValue> queueValue = new LinkedList<VertexValue>();
        VertexValue initial = vertexValue[initialIndex];
        queueValue.add(initial);
        initial.setMarked(true);
        initial.setDistance(0);
        while(!queueValue.isEmpty()) {
            VertexValue current = queueValue.remove();
            ArrayList<DimensionValue> list = current.getAjacencyList();
            for(int i = 0; i < list.size(); i++) {
                int currentIndex = getVertexIndex(vertexValue, list.get(i).getDimensionNumber());
                VertexValue nextValue = vertexValue[currentIndex];
                if(!nextValue.isMarked()) {
                    nextValue.setMarked(true);
                    nextValue.setDistance(current.getDistance() + 1);
                    nextValue.setEdgetoVertex(current);
                    queueValue.add(nextValue);
                }
            }
        }
    }
    public static ArrayList<Integer> getPathWithSpider (VertexValue[] base, int hubValue, int anomaly) {
        ArrayList<Integer> pathValue = new ArrayList<Integer>();
        int hubIndex = getVertexIndex(base, hubValue);
        int anomalyIndex = getVertexIndex(base, anomaly);
        VertexValue pointer = base[anomalyIndex];
        while(pointer.getDimension().getDimensionNumber() != base[hubIndex].getDimension().getDimensionNumber()) {
            pathValue.add(pointer.getDimension().getDimensionNumber());
            pointer = pointer.getEdgetoVertex();
        }
        pathValue.add(hubValue);
        return pathValue;
    }

    public static ArrayList<Integer> getPathWithoutSpider (VertexValue[] base, int hubValue, int anomaly) {
        ArrayList<Integer> pathSpiderValue = getPathWithSpider(base, hubValue, anomaly);
        ArrayList<Integer> pathValue = new ArrayList<Integer>();
        for(int i = pathSpiderValue.size() - 1; i >= 0; i--) {
            pathValue.add(pathSpiderValue.get(i));
        }
        for(int i = 1; i < pathSpiderValue.size(); i++) {
            pathValue.add(pathSpiderValue.get(i));
        }
        return pathValue;
    }

    public static ArrayList<PersonValue> checkAnomalies(VertexValue[] base, int hub) {
        ArrayList<PersonValue> anomaliesValue = new ArrayList<PersonValue>();
        
        for(int i = 0; i < base.length; i++) {
            ArrayList<PersonValue> personValues = base[i].getDimension().getPeople();
            ArrayList<Integer> indexAnormally = new ArrayList<Integer>();
            ArrayList<Integer> indexSpiderValue = new ArrayList<Integer>();
            for(int j = 0; j < personValues.size(); j++) {
                if(personValues.get(j).getCurrentDim() == personValues.get(j).getDimSig()){
                    indexSpiderValue.add(j);
                }
                if (personValues.get(j).getCurrentDim() != hub && personValues.get(j).getCurrentDim() != personValues.get(j).getDimSig()) {
                    indexAnormally.add(j);
                };
            }
            for(int s = 0; s < indexAnormally.size(); s++) {
                if(indexSpiderValue.size() > 0) {
                    ArrayList<Integer> path = getPathWithSpider(base, hub, personValues.get(indexAnormally.get(s)).getCurrentDim());
                    PersonValue anomaly = new PersonValue(personValues.get(indexAnormally.get(s)).getName(), path);
                    anomaly.setSpiderName(personValues.get(indexSpiderValue.get(0)).getName());
                    anomaliesValue.add(anomaly);
                    base[i].getDimension().getPeople().get(indexAnormally.get(s)).setCurrentDim(hub);
                    base[i].getDimension().getPeople().get(indexSpiderValue.get(0)).setCurrentDim(hub);
                    indexSpiderValue.remove(0);
                } else {
                    ArrayList<Integer> path = getPathWithoutSpider(base, hub, personValues.get(indexAnormally.get(s)).getCurrentDim());
                    PersonValue anomaly = new PersonValue(personValues.get(indexAnormally.get(s)).getName(), path);
                    anomaliesValue.add(anomaly);
                    base[i].getDimension().getPeople().get(indexAnormally.get(s)).setCurrentDim(hub);
                }
            }
        }
        return anomaliesValue;
    }



}
