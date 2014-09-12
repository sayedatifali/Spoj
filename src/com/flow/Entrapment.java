package com.flow;

import java.io.PrintWriter;
import java.util.Arrays;

/**
 * http://www.spoj.com/problems/EN/
 * 
 * @author sultan.of.swing
 *
 */
public class Entrapment {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public DinicsFlowFast mFlow;

	public Entrapment() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void solve() {
		int T;
		int V, E;
		int u, v;
		int dest;
		int src;
		int vertex;
		long flow;

		T = mFScanner.nextInt();

		mFlow = new DinicsFlowFast();

		while (T-- > 0) {
			V = mFScanner.nextInt();
			E = mFScanner.nextInt();

			mFlow.initialize();
			src = 0;
			dest = V;

			for (int i = 0; i < E; i++) {
				u = mFScanner.nextInt();
				v = mFScanner.nextInt();
				if (u > V || v > V)
					continue;
				mFlow.addEdge(u, v, 1);
			}

			mFlow.addEdge(0, 1, Integer.MAX_VALUE);

			mFlow.computeMaxFlow(src, dest);
			flow = mFlow.maxFlow();

//			mOut.println(flow);

			if (flow == 1)
				vertex = mFlow.bfs(1);
			else
				vertex = V;

			mOut.println(vertex);
		}
	}

	public void flush() {
		mOut.flush();
	}

	public void close() {
		mOut.close();
	}

	class DinicsFlowFast {

		public int V;
		public int E;
		public static final int MAXV = 35011;
		public static final int MAXE = 150011 * 2;
		public int mVertex[];
		public long mCapacity[];
		public int mNext[];
		public int mLast[];
		public int mDist[];
		public int mQueue[];
		public int mNow[];
		public int mEdgeCount[];
		public int edgeCount;
		public int S;
		public int T;
		public long mFlow;

		public DinicsFlowFast() {
			mVertex = new int[MAXE];
			mCapacity = new long[MAXE];
			mNext = new int[MAXE];
			mLast = new int[MAXV];
			mDist = new int[MAXV];
			mQueue = new int[MAXV];
			mNow = new int[MAXV];
			mEdgeCount = new int[MAXV];
		}

		public void initialize() {
			edgeCount = 0;
			Arrays.fill(mLast, -1);
			Arrays.fill(mNext, -1);
			mFlow = 0;
		}

		public void addEdge(int u, int v, long capacity) {
			mVertex[edgeCount] = v;
			mCapacity[edgeCount] = capacity;
			mNext[edgeCount] = mLast[u];
			mEdgeCount[u]++;
			mLast[u] = edgeCount++;

//			mVertex[edgeCount] = u;
//			mCapacity[edgeCount] = reverseCapacity;
//			mNext[edgeCount] = mLast[v];
//			mEdgeCount[v]++;
//			mLast[v] = edgeCount++;
		}
		
		public int bfs(int source) {
			int a, b;
			int u, v, edge;

			Arrays.fill(mDist, -1);
			a = b = 0;

			mQueue[0] = source;
			mDist[source] = 0;

			while (a <= b) {
				u = mQueue[a++];
				for (edge = mLast[u]; edge >= 0; edge = mNext[edge]) {
					v = mVertex[edge];
					if (mDist[v] == -1) {
						mQueue[++b] = v;
						mDist[v] = mDist[u] + 1;
					}
					if (mEdgeCount[v] == 1 && mCapacity[edge] == 0)
						return v;
				}
			}
			return 0;
		}

		public boolean dinicBFS() {

			int a, b;
			int u, edge;

			Arrays.fill(mDist, -1);
			a = b = 0;

			mQueue[0] = S;
			mDist[S] = 0;

			while (a <= b) {
				u = mQueue[a++];
				for (edge = mLast[u]; edge >= 0; edge = mNext[edge]) {
					if (mCapacity[edge] > 0 && mDist[mVertex[edge]] == -1) {
						mQueue[++b] = mVertex[edge];
						mDist[mVertex[edge]] = mDist[u] + 1;
					}
				}
			}

			return mDist[T] >= 0;
		}

		public long dinicDFS(int u, long flow) {

			long delta;

			if (u == T)
				return flow;

			for (int edge = mNow[u]; edge >= 0; edge = mNext[edge], mNow[u] = edge) {
				if (mCapacity[edge] > 0 && mDist[u] == mDist[mVertex[edge]] - 1) {
					delta = dinicDFS(mVertex[edge],
							Math.min(flow, mCapacity[edge]));
					if (delta > 0) {
						mCapacity[edge] -= delta;
//						mCapacity[edge ^ 1] += delta;
						return delta;
					}
				}
			}

			return 0;
		}

		public void computeMaxFlow(int S, int T) {
			long delta;

			this.S = S;
			this.T = T;

			while (dinicBFS()) {
				for (int i = 0; i < MAXV; i++)
					mNow[i] = mLast[i];
				while (true) {
					delta = dinicDFS(S, Long.MAX_VALUE);
					if (delta == 0)
						break;
					mFlow += delta;
				}
			}

		}

		public long maxFlow() {
			return mFlow;
		}
	}

	public static void main(String[] args) {
		Entrapment mSol = new Entrapment();
		mSol.solve();
		mSol.flush();
		mSol.close();
	}

}
