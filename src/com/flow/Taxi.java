package com.flow;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * http://www.spoj.com/problems/TAXI/
 * 
 * @author sultan.of.swing
 *
 */
public class Taxi {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public int K;
	public int P;
	public int T;
	public int mSpeed; // metre/second
	public int mTime; // seconds
	public FlowNetwork mFlowNetwork;
	public FordFulkerson mMaxFlow;
	public int V;
	public int[] mPersonsX;
	public int[] mPersonsY;
	public int[] mTaxisX;
	public int[] mTaxisY;

	public Taxi() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void solve() {
		int i, j;
		int node;
		int source;
		int dest;
		int maxPersons;
		boolean reachable;
		FlowEdge e;

		K = mFScanner.nextInt();
		mPersonsX = new int[401];
		mPersonsY = new int[401];
		mTaxisX = new int[201];
		mTaxisY = new int[201];

		while (K-- > 0) {
			P = mFScanner.nextInt();
			T = mFScanner.nextInt();
			mSpeed = mFScanner.nextInt();
			mTime = mFScanner.nextInt();

			source = 0;
			V = P + T + 2;
			dest = V - 1;

			mFlowNetwork = new FlowNetwork(V);

			node = 1;
			for (i = 0; i < P; i++) {
				mPersonsX[i] = mFScanner.nextInt();
				mPersonsY[i] = mFScanner.nextInt();
				e = new FlowEdge(source, node, 0, 1);
				mFlowNetwork.addEdge(e);
				node++;
			}

			for (i = 0; i < T; i++) {
				mTaxisX[i] = mFScanner.nextInt();
				mTaxisY[i] = mFScanner.nextInt();
				e = new FlowEdge(node, dest, 0, 1);
				mFlowNetwork.addEdge(e);
				node++;
			}

			for (i = 0; i < P; i++) {
				source = i + 1;
				dest = P + 1;
				for (j = 0; j < T; j++) {
					reachable = isReachable(mPersonsX[i], mPersonsY[i],
							mTaxisX[j], mTaxisY[j]);
					if (reachable) {
						e = new FlowEdge(source, dest, 0, 1);
						mFlowNetwork.addEdge(e);
					}
					dest++;
				}
			}
			
			source = 0;
			dest = V - 1;
			mMaxFlow = new FordFulkerson(mFlowNetwork, source, dest);
			
			maxPersons = mMaxFlow.value();
			mOut.println(maxPersons);

		}

	}

	public boolean isReachable(int sx, int sy, int dx, int dy) {

		double dist;
		double t;
		int time;
		double x;
		double y;

		x = Math.abs(sx - dx);
//		x *= x;
		y = Math.abs(sy - dy);
//		y *= y;

//		dist = Math.sqrt(x + y);
		dist = x + y;
		
		t = dist / mSpeed;
		time = mTime;

		if (t > time)
			return false;

		return true;

	}

	public void flush() {
		mOut.flush();
	}

	public void close() {
		mOut.close();
	}

	class FordFulkerson {

		private boolean marked[];
		private FlowEdge edgeTo[];
		private int value; // value of flow

		public FordFulkerson(FlowNetwork G, int s, int t) {

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

		public boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
			int v, w;
			int V;
			Queue<Integer> queue = new LinkedList<Integer>();
			V = G.V();

			marked = new boolean[V];
			edgeTo = new FlowEdge[V];

			queue.add(s);
			marked[s] = true;

			while (!queue.isEmpty()) {

				v = queue.poll();

				for (FlowEdge e : G.adj(v)) {
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

		public boolean inCut(int v) {
			return marked[v];
		}
	}

	class FlowNetwork {

		private int V;
		private int E;
		private Bag<FlowEdge> adj[];

		public FlowNetwork(int V) {
			int i;
			adj = (Bag<FlowEdge>[]) new Bag[V];

			this.V = V;
			E = 0;

			for (i = 0; i < V; i++) {
				adj[i] = new Bag<FlowEdge>();
			}
		}

		public void addEdge(FlowEdge e) {
			int v;
			int w;
			v = e.from();
			w = e.to();

			adj[v].add(e);
			adj[w].add(e);
			E++;
		}

		public int V() {
			return V;
		}

		public int E() {
			return E;
		}

		public Iterable<FlowEdge> adj(int v) {
			return adj[v];
		}

		// Each edge is returned twice
		public Iterable<FlowEdge> edges() {
			int v;
			Bag<FlowEdge> edges = new Bag<FlowEdge>();

			for (v = 0; v < V; v++) {
				for (FlowEdge e : adj(v))
					edges.add(e);
			}

			return edges;
		}

	}

	class FlowEdge implements Comparable<FlowEdge> {

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

		@Override
		// Do not need a compareto function
		public int compareTo(FlowEdge arg0) {
			// TODO Auto-generated method stub
			if (this.flow < arg0.flow)
				return -1;
			else if (this.flow > arg0.flow)
				return 1;
			return 0;
		}

	}

	class Bag<Item> implements Iterable<Item> {

		private Node first;
		private int N;

		private class Node {
			private Item item;
			private Node next;
		}

		public Bag() {
			first = null;
			N = 0;
		}

		public void add(Item w) {
			Node oldfirst = first;
			first = new Node();
			first.item = w;
			first.next = oldfirst;
			N++;
		}

		public boolean isEmpty() {
			return first == null;
		}

		@Override
		public Iterator<Item> iterator() {
			// TODO Auto-generated method stub
			return new ListIterator();
		}

		private class ListIterator implements Iterator<Item> {
			private Node current = first;

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return current != null;
			}

			@Override
			public Item next() {
				// TODO Auto-generated method stub
				if (!hasNext())
					throw new NoSuchElementException();
				Item item = current.item;
				current = current.next;
				return item;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				throw new UnsupportedOperationException();
			}
		}
	}

	public static void main(String[] args) {
		Taxi mSol = new Taxi();
		mSol.solve();
		mSol.flush();
		mSol.close();
	}

}
