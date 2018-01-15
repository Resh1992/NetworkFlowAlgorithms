package model;

import java.util.*;
import graph.*;

/**
* Represents a flow graph.
* @author Resham Ahluwalia
* @version 1.0
* @since 2017-12-01
*/
public class FlowGraph {
	private Hashtable<String, FlowVertex> vertices;
	private Hashtable<String, FlowEdge> edges;
	
	/**
	* Creates a flow graph from the simple graph.
	* @param graph Simple graph from which to construct flow graph
	* @throws Exception If graph generation fails.
	*/
	public FlowGraph(SimpleGraph graph) throws Exception {
		this.vertices = new Hashtable<String, FlowVertex>();
		this.edges = new Hashtable<String, FlowEdge>();
		
		Iterator vertexIterator = graph.vertices();
		while (vertexIterator.hasNext()) {
			Vertex vertex = (Vertex)vertexIterator.next();
			FlowVertex flowVertex = new FlowVertex((String)vertex.getName());
			this.addVertex(flowVertex);
		}
		
		Iterator edgeIterator = graph.edges();
		while (edgeIterator.hasNext()) {
			Edge edge = (Edge)edgeIterator.next();
			
			FlowVertex origin = this.vertices.get(edge.getFirstEndpoint().getName());
			FlowVertex dest = this.vertices.get(edge.getSecondEndpoint().getName());
			double capacity = (double)edge.getData();
			
			this.addEdge(origin, dest, capacity);
		}
	}
	
	/**
	* Get vertices in the graph.
	* @return Collection of vertices in the graph.
	*/
	public Collection<FlowVertex> getVertices() {
		return this.vertices.values();
	}
	
	/**
	* Add vertex in the graph.
	* @param vertex Vertex to add in the graph.
	*/
	public void addVertex(FlowVertex vertex) {
		this.vertices.put(vertex.getName(), vertex);
	}
	
	/**
	* Add edge in the graph.
	* @param origin Origin vertex of edge to add.
	* @param dest Destination vertex of edge to add.
	* @param capacity Capacity of the edge to add.
	* @throws Exception If addition of edge fails
	*/
	public void addEdge(FlowVertex origin, FlowVertex dest, double capacity) throws Exception {
		FlowEdge edge = new FlowEdge(origin, dest, capacity);
		origin.addEdge(edge);
		this.edges.put(edge.getName(), edge);
	}
	
	/**
	* Get source vertex in the graph.
	* @return Source vertex.
	*/
	public FlowVertex getSource() {
		return this.getVertex("s");
	}
	
	/**
	* Get sink vertex in the graph.
	* @return Sink vertex.
	*/
	public FlowVertex getSink() {
		return this.getVertex("t");
	}
	
	/**
	* Get vertex of given name in the graph.
	* @param name Name of the vertex to return.
	* @return Vertex with given name.
	*/
	public FlowVertex getVertex(String name) {
		return this.vertices.get(name);
	}
	
	/**
	* Reset visited flag on all the vertices in the graph.
	*/
	public void resetVisited() {
		for (FlowVertex vertex : this.vertices.values()) {
			vertex.resetVisited();
		}
	}
	
	/**
	* Get the number of vertices in the graph.
	* @return Number of vertices in the graph.
	*/
	public int numberOfVertices() {
		return this.vertices.size();
	}
	
	
	/**
	* Get the number of edges in the graph.
	* @return Number of edges in the graph.
	*/
	public int numberOfEdges() {
		return this.edges.size();
	}
}
