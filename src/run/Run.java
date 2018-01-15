package run;

import java.awt.Color;
import java.io.*;
import java.util.*;
import algorithms.*;
import graph.*;
import graph.generation.*;

public class Run {

	public static void main(String[] args) throws Exception {
		// rows columns max-capacity filename
		LinkedList<String[]> generatorArgs = new LinkedList<String[]>();
		generatorArgs.add(new String[] { "100", "200", "150", "Row-100.txt", "-cc" });
		generatorArgs.add(new String[] { "120", "200", "150", "Row-120.txt", "-cc" });
		generatorArgs.add(new String[] { "140", "200", "150", "Row-140.txt", "-cc" });
		generatorArgs.add(new String[] { "160", "200", "150", "Row-160.txt" , "-cc"});
		generatorArgs.add(new String[] { "180", "200", "150", "Row-180.txt" , "-cc"});
		generatorArgs.add(new String[] { "200", "200", "150", "Row-200.txt" , "-cc"});
		generatorArgs.add(new String[] { "220", "200", "150", "Row-220.txt" , "-cc"});
		generatorArgs.add(new String[] { "240", "200", "150", "Row-240.txt" , "-cc"});
		generatorArgs.add(new String[] { "260", "200", "150", "Row-260.txt" , "-cc"});
		generatorArgs.add(new String[] { "280", "200", "150", "Row-280.txt" , "-cc"});

		int numRuns = 5;
		boolean useExisting = false;
		boolean verbose = true;
		
		String separator = "\t";
		String header = "Rows" + separator
							+ "Columns" + separator
							+ "Max-Capacity" + separator
							+ "Vertices" + separator
							+ "Edges" + separator
							+ "Max flow" + separator
							+ "Preflow Time" + separator
							+ "Scaling Ford Fulkerson Time" + separator
							+ "Ford Fulkerson Time";
		System.out.println(header);
		LinkedList<String> lines = new LinkedList<String>();

		for (String[] generatorArg : generatorArgs) {
			String fileName = generatorArg[3];
			File graphFile = new File(fileName);
			if (!graphFile.exists() || !useExisting) {
				System.out.println("Creating graph " + fileName);
				new MeshGenerator(generatorArg).generate();
				SimpleGraph graph = new SimpleGraph();
				GraphInput.LoadSimpleGraph(graph, fileName);
				System.out.println(new ScallingFordFulkerson().findMaxFlow(graph));
				continue;
			}
			
			SimpleGraph graph = new SimpleGraph();

			PrintStream original = System.out;
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(int arg0) {
					// Do nothing
				}
			}));
			GraphInput.LoadSimpleGraph(graph, fileName);
			System.setOut(original);
			
			String rows = generatorArg[0];
			String columns = generatorArg[1];
			String maxCapacity = generatorArg[2];
			int vertices = graph.numVertices();
			int edges = graph.numEdges();
			
			double maxFlow = Double.NaN;
			
			FlowAlgorithm[] algorithms = new FlowAlgorithm[] { new PreFlowPush(), new ScallingFordFulkerson(), new FordFulkerson() };
			LinkedList<Long> durations = new LinkedList<Long>();
			
			for (FlowAlgorithm algorithm : algorithms) {
				
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < numRuns; i++) {
					if (verbose) {
						System.out.println(algorithm.getClass().getSimpleName() + ": run " + (i+1));
					}
					
					double result = algorithm.findMaxFlow(graph);
					
					if (Double.isNaN(maxFlow)) {
						maxFlow = result;
					} else if (maxFlow != result) {
						throw new Exception("Diff result " + result + " from " + maxFlow + " in " + algorithm.getClass().getSimpleName());
					}
				}
				
				long endTime = System.currentTimeMillis();
				long duration = (endTime - startTime) / numRuns;
				if (verbose) {
					System.out.println(algorithm.getClass().getSimpleName() + ": duration " + duration);
				}
				durations.addLast(duration);
			}
			
			String line = rows + separator
							+ columns + separator
							+ maxCapacity + separator
							+ vertices + separator
							+ edges + separator
							+ maxFlow + separator
							+ durations.get(0) + separator
							+ durations.get(1) + separator
							+ durations.get(2);
			System.out.println(line);
			lines.addLast(line);
		}
		
		System.out.println(header);
		for (String line : lines) {
			System.out.println(line);
		}
	}
}
