package algorithms;
import java.util.LinkedList; // Using the LinkedList class

import graph.SimpleGraph; // necessary imports
import model.*; // importing the utils

/**
 * <h3>Ford Fulkerson Algorithm</h3>
 * This class is an implementation of the Ford Fulkerson Method 
 * to find a maximum flow in a given network.
 * @author karthik Kolathumani
 * @version 1.0
 * @since 2017-12-01
 */
public class FordFulkerson implements FlowAlgorithm {
	/**
	 * Calculates the Max Flow
	 * @return double A max Flow Value
	 */
	@Override
	public double findMaxFlow(SimpleGraph simpleGraph) throws Exception {
		FlowGraph graph = new FlowGraph(simpleGraph);
		FlowVertex source = graph.getSource();
		
		while (true) {
			LinkedList<FlowEdge> path = this.getPathToSink(graph, source);
			graph.resetVisited();
			if (path == null) {
				// No s-t path in residual graph
				break;
			}
			
			double bottleneck = this.getBottleneck(path);
			for (FlowEdge edge : path) {
				edge.increaseFlow(bottleneck);
			}
		}
		
		return source.getOutgoingFlow();
	}
	
	
	/**
	 * Used to find the maximum value by which the flow can be augmented
	 * @param path An augmenting path
	 * @return double A bottleneck value or the maximum residual capacity in the chosen s-t path
	 */
	private double getBottleneck(LinkedList<FlowEdge> path) {
		double bottleneck = Double.MAX_VALUE;
		for (FlowEdge edge : path) {
			if (edge.getResidualCapacity() < bottleneck) {
				bottleneck = edge.getResidualCapacity();
			}
		}
		
		return bottleneck;
	}
	
	
	/**
	 * Traversing the vertices 
	 * @param graph This is a residual graph
	 * @param origin This is source
	 * @return LinkedList Returns the s-t path
	 */
	private LinkedList<FlowEdge> getPathToSink(FlowGraph graph, FlowVertex origin) {
		// Mark origin as visited
		origin.markVisited();
		
		for (FlowEdge edge : origin.getEdges()) {
			if (edge.getResidualCapacity() > 0) {
				if (edge.getDest().getName().equals(graph.getSink().getName())) {
					// Reached sink using this edge, return path as just this edge
					LinkedList<FlowEdge> path = new LinkedList<FlowEdge>();
					path.add(edge);
					return path;
				} else if (!edge.getDest().isVisited()) {
					LinkedList<FlowEdge> path = this.getPathToSink(graph, edge.getDest());
					if (path != null) {
						// Dest of edge reached the sink, add this edge and return
						path.addFirst(edge);
						return path;
					}
				}
			}
		}
		
		// No path found
		return null;
	}
}