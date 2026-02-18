package spiderman;

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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        
        String inputDimension = args[0];
        String inputSpiderVerse = args[1];
        String inputSpot = args[2];
        String outputTrackSpot = args[3];
        StdIn.setFile(inputSpot);
        int startValue = StdIn.readInt();
        int endValue = StdIn.readInt();

        VertexValue[] vertexValue = Collider.colliderCreator(inputDimension, inputSpiderVerse, outputTrackSpot);
        Collider.incrementPeopleValue(vertexValue, inputDimension, inputSpiderVerse, inputSpot);

        StdOut.setFile(args[3]);
        boolean resultValue = false;
        int startIndex = findIndexVertexValue(vertexValue, startValue);
        VertexValue starter = vertexValue[startIndex];
        StdOut.print(starter.getDimension().getDimensionNumber() + " ");
        dfs(vertexValue, startValue, endValue, resultValue);
        


        
        
    }

    public static int findIndexVertexValue(VertexValue[] vertexValue, int dimension) {
        for(int i = 0; i < vertexValue.length; i++) {
            if(vertexValue[i].getDimension().getDimensionNumber() == dimension) {
                return i;
            }
        }
        return -1;
    }

    public static void dfs(VertexValue[] base, int start, int end, boolean found) {
        int intialIndex = findIndexVertexValue(base, start);
        VertexValue starterValue = base[intialIndex];
        starterValue.setMarked(true);
        ArrayList<DimensionValue> ajacencyList = starterValue.getAjacencyList();
        for(int i = 0; i < ajacencyList.size(); i++) {
            int currentIndex = findIndexVertexValue(base, ajacencyList.get(i).getDimensionNumber());
            VertexValue currentVertex = base[currentIndex];
            if(currentVertex.isMarked() == false) {
                int currentDimensionNumber = currentVertex.getDimension().getDimensionNumber();
                if(!found) StdOut.print(currentDimensionNumber + " ");
                if(currentDimensionNumber == end) {
                    found = true;
                }
                dfs(base, currentDimensionNumber, end, found);
            }
        }      
    }
}
