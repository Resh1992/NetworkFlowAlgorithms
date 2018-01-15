/*
 * Scaling -- Ford Fulkerson
 */
package algorithms;

import java.util.*;

import graph.SimpleGraph;
import model.*;

/**
* The ScallingFordFulkerson program implements the algorithm to improvise the runtime of Ford Fulkerson algorithm by introducing a scaling factor called "delta". This program finds the Maximum flow for a given s-t path of a Network flow Graph. 
*
* @author  Sonal Goswami
* @version 1.0
* @since   2017-12-1 
*/
public class ScallingFordFulkerson implements FlowAlgorithm {
	
	/**
	* Calculates max flow in given graph using Scaling Ford Fulkerson algorithm.
	* @param simpleGraph Graph in which we find max flow.
	* @return Max flow
	*/
	@Override
	public double findMaxFlow(SimpleGraph simpleGraph) throws Exception {
		FlowGraph graph = new FlowGraph(simpleGraph);
		FlowVertex source = graph.getSource();
		
		//Outgoing Capacity from Source
		double sourceOutgoingCapacity = source.getOutgoingCapacity();		
		// Get starting min residual capacity
		double minResidualCapacity = 1;
		while (minResidualCapacity * 2 < sourceOutgoingCapacity) {
			minResidualCapacity *= 2;
		}
		
		while (minResidualCapacity >= 1) {
			this.findMaxFlowWithMinResidualCapacity(graph, minResidualCapacity);
			minResidualCapacity = minResidualCapacity / 2;
		}
		
		return source.getOutgoingFlow();
	}
	
	/**
	* Calculates max flow with minimum residual capacity from the residual graph and the Maximum flow of an s-t flow.
	* @param simpleGraph Graph in which we find max flow.
	* @param bottleneck value of the maximum bottleneck of the Graph G 
	*/
	private void findMaxFlowWithMinResidualCapacity(FlowGraph graph, double minResidualCapacity) throws Exception {
		
		while (true) {
			LinkedList<FlowEdge> path = this.getPathToSinkWithMinResidualCapacity(graph, graph.getSource(), minResidualCapacity);
			graph.resetVisited();
			if (path == null) {
				// No s-t path in residual graph
				break;
			}
			
			double bottleneck = this.getBottleneck(path);
			//Here we find the largest bottleneck value of the given graph
			for (FlowEdge edge : path) {
				edge.increaseFlow(bottleneck);
			}
		}
	}
	
	/**
	* Calculates computes the largest bottleneck value of the given graph.
	* @param path Path in whcih to find bottle neck
	* @return bottlemneck value
	*/
	private double getBottleneck(LinkedList<FlowEdge> path) {
		//This method 
		double bottleneck = Double.MAX_VALUE;
		for (FlowEdge edge : path) {
			if (edge.getResidualCapacity() < bottleneck) {
				bottleneck = edge.getResidualCapacity();
			}
		}
		
		return bottleneck;
	}
	
	/**
	* This Linked List is used to find the s-t path to the sink with the Minimum Residual capacity
	* @param FlowGraph graph graph in which we find the max flow
	* @param FlowVertex origin starting nodes of the s-t flow 
	* @param minResidualCapacity the minimum residual capacity from the residual graph 
	* @return FlowEdge: s-t path with the max Flow, minimum residual capacity.
	*/
	
			//We take the graph, source node and the minimum residual capacity value as the input for this method. 
	private LinkedList<FlowEdge> getPathToSinkWithMinResidualCapacity(
														FlowGraph graph,
														FlowVertex origin,
														double minResidualCapacity) {
		
		// Mark origin as visited
		origin.markVisited();
		
		for (FlowEdge edge : origin.getEdges()) {
			if (edge.getResidualCapacity() >= minResidualCapacity) {
				if (edge.getDest().getName().equals(graph.getSink().getName())) {
					// Reached sink using this edge, return path as just this edge
					LinkedList<FlowEdge> path = new LinkedList<FlowEdge>();
					path.add(edge);
					return path;
				} else if (!edge.getDest().isVisited()) {
					LinkedList<FlowEdge> path = this.getPathToSinkWithMinResidualCapacity(graph, edge.getDest(), minResidualCapacity);
					if (path != null) {
						// Destination of edge reached the sink, add this edge and return
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