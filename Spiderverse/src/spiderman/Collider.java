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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        VertexValue[] vertexValue = colliderCreator(args[0], args[1], args[2]);
        incrementPeopleValue(vertexValue, args[0], args[1], args[2]);
        for(int i = 0; i < vertexValue.length; i++) {
            StdOut.print(vertexValue[i].dimensionValue.getDimensionNumber() + " ");
            for(int j = 0; j < vertexValue[i].ajacencyList.size(); j++) {
                StdOut.print(vertexValue[i].ajacencyList.get(j).getDimensionNumber() + " ");
            }
            StdOut.println();
        }




    } 

    public static VertexValue[] colliderCreator(String dimensionInputFile, String spiderverseInputFile, String colliderOutputFile) {
        

        StdOut.setFile(colliderOutputFile);

        
        StdIn.setFile(dimensionInputFile);
        int dimension = StdIn.readInt();
        int sizeValue = StdIn.readInt();
        double capacityValue = StdIn.readDouble();
        HashTable adjacencyValue = new HashTable(sizeValue);
        for(int i = 0; i < dimension; i++) {
            int dimensionNumber = StdIn.readInt();
            int eventsCanon  = StdIn.readInt();
            int weightValue = StdIn.readInt();
            DimensionValue dimensionValue = new DimensionValue(dimensionNumber, eventsCanon, weightValue, null);
            adjacencyValue.add(dimensionValue);
            if(( (1.0 * i + 1)/ adjacencyValue.getNumClusters() )>= capacityValue){
                adjacencyValue.rehashFunction();
            }
            StdIn.readLine();
        }
        

      
        adjacencyValue.connectDimensions();

  
        StdIn.setFile(dimensionInputFile);
        StdIn.readInt();
        StdIn.readInt();
        StdIn.readInt();

        VertexValue[] graph = new VertexValue[dimension];
        for(int i = 0; i < dimension; i++) {
            int dimensionNumber = StdIn.readInt();
            int eventsCanon = StdIn.readInt();
            int weightValue = StdIn.readInt();
            ArrayList<PersonValue> peopleValue = new ArrayList<PersonValue>();
            DimensionValue dimensionValue = new DimensionValue(dimensionNumber, eventsCanon, weightValue, peopleValue, null);
            ArrayList<DimensionValue> ajacencyList = new ArrayList<DimensionValue>();
            VertexValue v = new VertexValue(dimensionValue, ajacencyList);
            graph[i] = v;
        }

        for(int i = 0 ; i < adjacencyValue.getSize(); i++) {
            DimensionValue dimensionValue = adjacencyValue.get(i);
            int indexValue = indexFinder(graph, dimensionValue.getDimensionNumber());
            

            for(int j = 1 ; j < adjacencyValue.getIndexSize(i); j++) {
                DimensionValue pointer = adjacencyValue.get(i).getNext(j);
                int indexValueTwo = indexFinder(graph, pointer.getDimensionNumber());
                graph[indexValue].addToAList(graph[indexValueTwo].dimensionValue);
                graph[indexValueTwo].addToAList(graph[indexValue].dimensionValue);
                
            }
        }

        return graph;
        
    }

    public static int indexFinder(VertexValue[] vertexValue, int dimensionNumber) {
        for(int i = 0; i < vertexValue.length; i++) {
            if(vertexValue[i].dimensionValue.getDimensionNumber() == dimensionNumber) {
                return i;
            }
        }
        return -1;
    }

    public static VertexValue[] incrementPeopleValue(VertexValue[] vertexValue, String dimensionInputFile, String spiderverseInputFile, String colliderOutputFile){
        
        StdIn.setFile(spiderverseInputFile);
        int peopleNumber = StdIn.readInt();
        for(int i = 0; i < peopleNumber; i++) {
            int dimensionNumber = StdIn.readInt();
            String nameValue = StdIn.readString();
            int dimensionSignature = StdIn.readInt();
            PersonValue personValue = new PersonValue(dimensionSignature, nameValue, dimensionNumber);
            int index = indexFinder(vertexValue, dimensionNumber);
            vertexValue[index].dimensionValue.addPerson(personValue);
        }



        return vertexValue;

    }

}