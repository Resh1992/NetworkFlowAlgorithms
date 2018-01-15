package model;

import java.util.*;

/**
* Represents a vertex in the flow graph.
* @author Resham Ahluwalia
* @version 1.0
* @since 2017-12-01
*/
public class FlowVertex {
	// name of vertex
	private String name;
	
	// table from edge name of edge object
	Hashtable<String, FlowEdge> edges;
	
	// excess flow, useful for preflow algorithm. For Ford Fulkerson type algorithm, excess = 0
	private double excess;
	
	// height, used in preflow algorithm
	private int height;
	
	private boolean visited;
	
	/**
	* Creates a flow vertex.
	* @param name Name of the vertex.
	*/
	public FlowVertex(String name) {
		this.name = name;
		this.edges = new Hashtable<String, FlowEdge>();
	}

	/**
	* Add given edge to the vertex. Origin of the given edge must be this vertex.
	* @param edge Edge to add to this vertex.
	* @throws Exception If origin of given edge is not this vertex.
	*/
	public void addEdge(FlowEdge edge) throws Exception {
		if (edge.getOrigin().getName() != this.name) {
			// Adding edge to this vertex but in the edge origin is specified as some other vertex
			throw new Exception("Adding edge " + edge.getName() + " with origin vertex " + edge.getOrigin().getName() + " on " + this.name);
		}
		
		this.edges.put(edge.getName(), edge);
	}
	
	/**
	* Remove given edge from this vertex.
	* @param edge Edge to remove.
	*/
	public void removeEdge(FlowEdge edge) {
		this.edges.remove(edge.getName());
	}

	/**
	* Get name of this vertex.
	* @return Name of this vertex.
	*/
	public String getName() {
		return this.name;
	}

	/**
	* Whether this vertex has been visited or not. Useful in case of graph traversals like DFS.
	* @return True if vertex has been marked as visited, false otherwise.
	*/
	public boolean isVisited() {
		return this.visited;
	}
	
	/**
	* Mark this vertex as visited.
	*/
	public void markVisited() {
		this.visited = true;
	}
	
	/**
	* Reset the visited flag on this vertex.
	*/
	public void resetVisited() {
		this.visited = false;
	}
	
	
	/**
	* Get excess of this vertex.
	* @return Excess of this vertex.
	*/
	public double getExcess() {
		return this.excess;
	}
	
	/**
	* Increase excess on this vertex by given amount.
	* @param increment Amount by which to increase excess.
	*/
	public void increaseExcess(double increment) {
		this.excess += increment;
	}
	
	
	/**
	* Get height label of this vertex.
	* @return Height label of this vertex.
	*/
	public int getHeight() {
		return this.height;
	}
	
	/**
	* Set height label of this vertex.
	* @param height Height of the vertex.
	*/
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	* Increment height label of this vertex by 1.
	*/
	public void incrementHeight() {
		this.height += 1;
	}
	
	/**
	* Get edge outgoing from this vertex such that other end point of that edge
	* has height less than this vertex.
	* @return Edge if their is neghboring vertex with height less than this vertex, otherwise null.
	*/
	public FlowEdge getLessHeightNeighborEdge() {		
		for (FlowEdge edge : this.edges.values()) {
			if (edge.getResidualCapacity() > 0 && edge.getDest().height < this.height) {
				return edge;
			}
		}
		
		return null;
	}
	
	/**
	* Get outgoing flow from this vertex.
	* @return Total outgoing flow from this vertex.
	*/
	public double getOutgoingFlow() {
		double flow = 0;
		for (FlowEdge edge : this.edges.values()) {
			if (!edge.isBackwardEdge()) {
				flow += edge.getFlow();
			}
		}
		
		return flow;
	}
	
	/**
	* Get total outgoing capacity from this vertex.
	* @return Total outgoing capacity from this vertex.
	*/
	public double getOutgoingCapacity() {
		double capacity = 0;
		for (FlowEdge edge : this.edges.values()) {
			if (!edge.isBackwardEdge()) {
				capacity += edge.getCapacity();
			}
		}
		
		return capacity;
	}
	
	/**
	* Get outgoing edges from this vertex. This also includes backward edges.
	* @return Array of outgoing edges from this vertex.
	*/
	public FlowEdge[] getEdges() {
		FlowEdge[] edgeArray = new FlowEdge[this.edges.size()];
		this.edges.values().toArray(edgeArray);
		return edgeArray;
	}
	
	/**
	* Whether the vertex is source or sink.
	* @return True if vertex is either source or sink, otherwise false.
	*/
	public boolean isSourceOrSink() {
		return this.name.equals("s") || this.name.equals("t");
	}
}
