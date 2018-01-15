package algorithms;

import graph.SimpleGraph;

/**
* <h3>Flow Algorithm Interface</h3>
* Interface for implementing the flow algorithm.
* @author Resham Ahluwalia
* @version 1.0
* @since 2017-12-01
*/
public interface FlowAlgorithm {
	/**
	* Method to calculate the maximum flow in the given simple graph.
	* @param graph Graph in which to calculate max flow.
	* @return Value of max flow in the given graph.
	* @throws Exception If finding max flow fails
	*/
	public double findMaxFlow(SimpleGraph graph) throws Exception;
}
