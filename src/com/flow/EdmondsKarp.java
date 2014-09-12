package com.flow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Time Complexity O(V*E^2)
 * 
 * @author sultan.of.swing
 *
 */
public class EdmondsKarp {

	public int V;
	public List<FlowEdge>[] mGraph;
	public boolean marked[];
	public FlowEdge edgeTo[];
	public int flow;

	public EdmondsKarp(int V) {
		mGraph = new List[V];
		for (int i = 0; i < V; i++)
			mGraph[i] = new LinkedList<FlowEdge>();
		marked = new boolean[V];
		edgeTo = new FlowEdge[V];
		flow = 0;
	}

	public void addEdge(int u, int v, int flow, int capacity) {
		FlowEdge e;
		e = new FlowEdge(u, v, flow, capacity);
		mGraph[u].add(e);
		mGraph[v].add(e);
	}

	public void maxFlow(int s, int t) {
		int delta;
		
		while (hasAugmentingPath(s, t)) {
			delta = Integer.MAX_VALUE;
			
			for (int u = t; u != s; u = edgeTo[u].other(u)) {
				delta = Math.min(delta, edgeTo[u].residualCapacityTo(u));
			}
			
			for (int u = t; u != s; u = edgeTo[u].other(u)) {
				edgeTo[u].addResidualFlowTo(u, delta);
			}
			
			this.flow += delta;
		}

	}
	
	public int flow() {
		return this.flow;
	}

	public boolean hasAugmentingPath(int s, int t) {
		int u;
		int v;
		Arrays.fill(marked, false);
		Queue<Integer> queue;

		queue = new LinkedList<Integer>();
		marked[s] = true;
		queue.add(s);

		while (!queue.isEmpty()) {
			u = queue.poll();

			for (FlowEdge e : mGraph[u]) {
				v = e.other(u);
				if (!marked[v] && e.residualCapacityTo(v) > 0) {
					marked[v] = true;
					edgeTo[v] = e;
					queue.add(v);
				}
			}
		}

		return marked[t];
	}

	class FlowEdge {
		public int u;
		public int v;
		public int flow;
		public int capacity;

		public FlowEdge(int u, int v, int flow, int capacity) {
			this.u = u;
			this.v = v;
			this.flow = flow;
			this.capacity = capacity;
		}

		public int residualCapacityTo(int vertex) {
			if (this.u == vertex) // Back edge
				return this.flow;
			// Forward edge
			return this.capacity - this.flow;
		}

		public void addResidualFlowTo(int vertex, int delta) {
			if (this.u == vertex) { // Back edge
				this.flow -= delta;
			} else { // Forward edge
				this.flow += delta;
			}
		}

		public int from() {
			return u;
		}

		public int to() {
			return v;
		}

		public int other(int w) {
			if (w == this.u)
				return v;
			return u;
		}

	}
	
	public static void main(String [] args) {
		EdmondsKarp mFlow = new EdmondsKarp(6);
		
		mFlow.addEdge(0, 1, 0, 10);
		mFlow.addEdge(0, 2, 0, 5);
		mFlow.addEdge(1, 3, 0, 9);
		mFlow.addEdge(1, 4, 0, 4);
		mFlow.addEdge(2, 1, 0, 4);
		mFlow.addEdge(2, 4, 0, 8);
		mFlow.addEdge(3, 4, 0, 15);
		mFlow.addEdge(3, 5, 0, 10);
		mFlow.addEdge(4, 5, 0, 10);
		
		mFlow.maxFlow(0, 5);
		
		System.out.println("Maxflow = " + mFlow.flow());
	}

}
