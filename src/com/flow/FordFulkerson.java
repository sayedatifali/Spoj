package com.flow;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FordFulkerson {

	private boolean marked[];
	private FlowEdge edgeTo[];
	private int value; // value of flow

	public FordFulkerson(List<FlowEdge>[] G, int s, int t) {

		int delta;

		value = 0;

		while (hasAugmentingPath(G, s, t)) {

			delta = Integer.MAX_VALUE;

			for (int v = t; v != s; v = edgeTo[v].other(v)) {
				delta = Math.min(delta, edgeTo[v].residualCapacityTo(v));
			}

			for (int v = t; v != s; v = edgeTo[v].other(v)) {
				edgeTo[v].addResidualFlowTo(v, delta);
			}

			value += delta;
		}

	}

	public boolean hasAugmentingPath(List<FlowEdge>[] G, int s, int t) {
		int v, w;
		int V;
		Queue<Integer> queue = new LinkedList<Integer>();
		V = G.length;

		marked = new boolean[V];
		edgeTo = new FlowEdge[V];

		queue.add(s);
		marked[s] = true;

		while (!queue.isEmpty()) {

			v = queue.poll();

			for (FlowEdge e : G[v]) {
				w = e.other(v);

				if (e.residualCapacityTo(w) > 0 && !marked[w]) {
					marked[w] = true;
					edgeTo[w] = e;
					queue.add(w);
				}
			}

		}

		return marked[t];
	}

	public int value() {
		return value;
	}

	static class FlowEdge {

		private int v;
		private int w;
		private int flow;
		private int capacity;

		public FlowEdge(int v, int w, int flow, int capacity) {
			this.v = v;
			this.w = w;
			this.flow = flow;
			this.capacity = capacity;
		}

		public int from() {
			return v;
		}

		public int to() {
			return w;
		}

		public int either() {
			return v;
		}

		public int other(int vertex) {
			if (v == vertex)
				return this.w;
			else
				return this.v;
		}

		public int weight() {
			return flow;
		}

		public int capacity() {
			return capacity;
		}

		public int residualCapacityTo(int vertex) {
			if (v == vertex)
				return flow;
			else
				return capacity - flow;
		}

		public void addResidualFlowTo(int vertex, int delta) {
			if (v == vertex)
				flow -= delta;
			else
				flow += delta;
		}

	}

	public static void main(String[] args) {

		int V;
		FordFulkerson fordFulkerson;
		FlowEdge edge;
		List<FlowEdge> Graph[];

		V = 6;

		Graph = new List[V];

		for (int i = 0; i < V; i++)
			Graph[i] = new LinkedList<>();

		edge = new FlowEdge(0, 1, 0, 10);

		Graph[0].add(edge);
		Graph[1].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(0, 2, 0, 5);
		Graph[0].add(edge);
		Graph[2].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(1, 3, 0, 9);
		Graph[1].add(edge);
		Graph[3].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(1, 4, 0, 4);
		Graph[1].add(edge);
		Graph[4].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(2, 1, 0, 4);
		Graph[2].add(edge);
		Graph[1].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(2, 4, 0, 8);
		Graph[2].add(edge);
		Graph[4].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(3, 4, 0, 15);
		Graph[3].add(edge);
		Graph[4].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(3, 5, 0, 10);
		Graph[3].add(edge);
		Graph[5].add(edge);
		// G.addEdge(edge);
		edge = new FlowEdge(4, 5, 0, 10);
		Graph[4].add(edge);
		Graph[5].add(edge);
		// G.addEdge(edge);

		fordFulkerson = new FordFulkerson(Graph, 0, 5);

		System.out.println("Maximum flow in the network: "
				+ fordFulkerson.value());

	}
}
