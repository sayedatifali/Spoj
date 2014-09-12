package com.flow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DinicsFlow {

	public int V;
	public int flow;
	public List<FlowEdge>[] mGraph;
	public int mQueue[];

	public DinicsFlow(int V) {
		this.V = V;
		mGraph = new List[V];
		for (int i = 0; i < V; i++)
			mGraph[i] = new LinkedList<FlowEdge>();
		mQueue = new int[V];
	}

	public void addEdge(int u, int v, int flow, int capacity) {
		FlowEdge e;
		e = new FlowEdge(u, v, flow, capacity);
		mGraph[u].add(e);
		mGraph[v].add(e);
	}

	public boolean dinicBFS(int src, int dst, int[] dist) {
		int u;
		int v;
		int sizeQ;
		Arrays.fill(dist, -1);
		dist[src] = 0;
		sizeQ = 0;

		mQueue[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			u = mQueue[i];
			for (FlowEdge e : mGraph[u]) {
				v = e.other(u);
				if (dist[v] < 0 && e.residualCapacityTo(v) > 0) {
					dist[v] = dist[u] + 1;
					mQueue[sizeQ++] = v;
				}
			}
		}

		return dist[dst] >= 0;
	}

	public int dinicDFS(int[] ptr, int[] dist, int dst, int u, int f) {

		int v;
		int df;
		FlowEdge e;

		if (u == dst)
			return f;

		for (; ptr[u] < mGraph[u].size(); ptr[u]++) {
			e = mGraph[u].get(ptr[u]);
			v = e.other(u);
			if (dist[v] == dist[u] + 1 && e.residualCapacityTo(v) > 0) {
				df = dinicDFS(ptr, dist, dst, v,
						Math.min(f, e.residualCapacityTo(v)));
				if (df > 0) {
					e.addResidualFlowTo(v, df);
					return df;
				}
			}
		}

		return 0;
	}

	public void maxFlow(int s, int t) {
		int df;
		int[] dist;
		int[] ptr;
		this.flow = 0;
		dist = new int[V];
		ptr = new int[V];

		while (dinicBFS(s, t, dist)) {
			Arrays.fill(ptr, 0);
			while (true) {
				df = dinicDFS(ptr, dist, t, s, Integer.MAX_VALUE);
				if (df == 0)
					break;
				this.flow += df;
			}
		}
	}

	public int flow() {
		return flow;
	}

	class FlowEdge {
		int u; // from
		int v; // to
		int flow;
		int capacity;

		public FlowEdge(int u, int v, int flow, int capacity) {
			this.u = u;
			this.v = v;
			this.flow = flow;
			this.capacity = capacity;
		}

		public int from() {
			return u;
		}

		public int to() {
			return v;
		}

		public int other(int vertex) {
			if (this.u == vertex)
				return this.v;
			return this.u;
		}

		public int residualCapacityTo(int vertex) {
			if (this.u == vertex) {
				return flow;
			}
			return capacity - flow;
		}

		public void addResidualFlowTo(int vertex, int delta) {
			if (this.u == vertex) {
				this.flow -= delta;
			} else {
				this.flow += delta;
			}
		}

	}

	public static void main(String[] args) {

		DinicsFlow mFlow = new DinicsFlow(6);

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
