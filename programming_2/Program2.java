/**
 * Created by tkalbar on 3/2/19.
 */

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Program2 {
	
	class edge {
		int startnode;
		int endnode;
		int weight;
		public edge(int i, int j, int abs) {
			startnode = i;
			endnode = j;
			weight = abs;
		}
	}
	
	class comparator implements Comparator<edge> {
		
		@Override
		public int compare(edge e0, edge e1) {
			return e0.weight - e1.weight;
		}
		
	}

    public int constructIntensityGraph(int[][] image){
        // TODO
    	
        int intensityWeight = 0;
    	int row = image.length;
    	int col = image[0].length;        
		// Create adjacency list
        Map<Integer, ArrayList<edge>> adjacency = new HashMap<Integer, ArrayList<edge>>();
        
        // for row*col size image,the node number is like this:
        // 0, 1, 2, ......, col-1;
        // col, col+1, ..., 2*col-1;
        // ........................;
        // (row-1)*col, ..., row*col-1;
		for (int i = 0; i < row*col; ++i){
			ArrayList<edge> temp = new ArrayList<edge>();
			int indexrow = i / col;
        	int indexcol = i % col;
        	// Up to 4 edges could be assigned to one vertex
			if (indexrow-1>=0) {
				temp.add(new edge(i, col*(indexrow-1)+indexcol, Math.abs(image[indexrow][indexcol] - image[indexrow-1][indexcol])));
        	}
        		
        	if (indexrow+1 < row) {
        		temp.add(new edge(i, col*(indexrow+1)+indexcol, Math.abs(image[indexrow][indexcol] - image[indexrow+1][indexcol])));
        	}
        		
        	if (indexcol + 1 < col) {
        		temp.add(new edge(i, i+1, Math.abs(image[indexrow][indexcol] - image[indexrow][indexcol+1])));
        	}
        		
        	if (indexcol - 1 >= 0) {
        		temp.add(new edge(i, i-1, Math.abs(image[indexrow][indexcol] - image[indexrow][indexcol-1])));
        	}
			adjacency.put(i, temp);
		}

        for(int node = 0; node < row * col; ++node) {
			ArrayList<edge> adjacencyedge = adjacency.get(node);
			for(int i = 0; i < adjacencyedge.size(); ++i) {
                intensityWeight += adjacencyedge.get(i).weight;
			}
		}

    	return intensityWeight / 2;
    }
    
    
	public int constructPrunedGraph(int[][] image){
        // TODO
		
		// Using prim's algorithm
		int prunedweight = 0;
    	int row = image.length;
    	int col = image[0].length;
    	// Create notvisited arraylist to store vertices that are not in the minimum spanning tree,0 is the start node.
    	ArrayList<Integer> notvisited = new ArrayList<Integer>();
        for (int i = 1; i < row*col; ++i) {
        	notvisited.add(i);
        }
        
		// Create adjacency list
        Map<Integer, ArrayList<edge>> adjacency = new HashMap<Integer, ArrayList<edge>>();
        
        // for row*col size image, the node number is like this:
        // 0, 1, 2, ......, col-1;
        // col, col+1, ..., 2*col-1;
        // ........................;
        // (row-1)*col, ..., row*col-1;
		for (int i = 0; i < row*col; ++i){
			ArrayList<edge> temp = new ArrayList<edge>();
			int indexrow = i / col;
        	int indexcol = i % col;
        	// Up to 4 edges could be assigned to one vertex
			if (indexrow-1>=0) {
				temp.add(new edge(i, col*(indexrow-1)+indexcol, Math.abs(image[indexrow][indexcol] - image[indexrow-1][indexcol])));
        	}
        		
        	if (indexrow+1 < row) {
        		temp.add(new edge(i, col*(indexrow+1)+indexcol, Math.abs(image[indexrow][indexcol] - image[indexrow+1][indexcol])));
        	}
        		
        	if (indexcol + 1 < col) {
        		temp.add(new edge(i, i+1, Math.abs(image[indexrow][indexcol] - image[indexrow][indexcol+1])));
        	}
        		
        	if (indexcol - 1 >= 0) {
        		temp.add(new edge(i, i-1, Math.abs(image[indexrow][indexcol] - image[indexrow][indexcol-1])));
        	}
			adjacency.put(i, temp);
		}
		
		// Initialize adjacency list, add the 0 node edge to PriorityQueue.
        PriorityQueue<edge> edgequeue = new PriorityQueue<>(new comparator());
        for(int i = 0; i < adjacency.get(0).size(); ++i){
			edgequeue.add((adjacency.get(0).get(i)));
		}
        // Extract the smallest one from priority queue and add it to MST
        while(!notvisited.isEmpty()) {
        	edge minedge= edgequeue.poll();
        	if(!notvisited.contains(minedge.startnode) && notvisited.contains(minedge.endnode)) {
        		prunedweight += minedge.weight;
        		notvisited.remove(Integer.valueOf(minedge.endnode));
        		ArrayList<edge> adjacencyedge = adjacency.get(minedge.endnode);
                for(int i = 0; i < adjacencyedge.size(); ++i){
					if(notvisited.contains(adjacencyedge.get(i).endnode))
					    edgequeue.add((adjacencyedge.get(i)));
				}
        	}
        }
        return prunedweight;
    }

}
