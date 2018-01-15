import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

import algorithms.*;
import graph.*;

/**
* <h3>Maximum Network Flow</h3>
* Application to find maximum flow in the graph using {@link algorithms.FordFulkerson Ford Fulkerson}, {@link algorithms.ScallingFordFulkerson Scaling Ford Fulkerson} and {@link algorithms.PreFlowPush Pre Flow Push} Algorithm.
* @author Resham Ahluwalia
* @version 1.0
* @since 2017-12-01
*/
public class tcss543 {

	/**
	 * <pre>
	* Main method for running application.
	* Arguments - Expect one argument which is the name of txt file containing input graph.
	* Output:
	* 	Number of vertices and edges in graph.
	* 	Max flow output from all three algorithms along with time taken by each algorithm.
	* </pre>
	* @param args Program arguments
	* @throws Exception If encounters some error during max flow calculation
	*/
	public static void main(String[] args) throws Exception {
		SimpleGraph graph = new SimpleGraph();
		GraphInput.LoadSimpleGraph(graph, args[0]);

		System.out.println("Vertices: " + graph.numVertices());
		System.out.println("Edges: " + graph.numEdges());

		FlowAlgorithm[] algorithms = new FlowAlgorithm[] { new ScallingFordFulkerson(), new FordFulkerson(), new PreFlowPush() };

		for (FlowAlgorithm algorithm : algorithms) {
			long startTime = System.currentTimeMillis();
			double maxFlow = algorithm.findMaxFlow(graph);
			long endTime = System.currentTimeMillis();
			
			long duration = endTime - startTime;

			System.out.println(algorithm.getClass().getSimpleName() +  " Max flow: " + maxFlow + " in " + duration + " ms");
		}
	}
}
