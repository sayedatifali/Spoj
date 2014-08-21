package com.segment;

import java.io.PrintWriter;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/TEMPLEQ/
 * 
 * @author sultan.of.swing
 * 
 */

public class TempleQueue {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public SegmentTree mSegmentTree;
	public int[] A;

	public TempleQueue() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void solve() {
		int i;
		int N, Q;
		int op;
		int x;
		int ans;

		N = mFScanner.nextInt();
		Q = mFScanner.nextInt();

		A = new int[N];
		mSegmentTree = new SegmentTree(N);

		for (i = 0; i < N; i++) {
			A[i] = mFScanner.nextInt();
		}

		mSegmentTree.buildSegmentTree(1, 0, N - 1, A);

		for (i = 0; i < Q; i++) {
			op = mFScanner.nextInt();
			x = mFScanner.nextInt();

			if (op == 1) {
				// One pilgrim enters queue x
				mSegmentTree.update(1, 0, N - 1, x - 1, 1);
			} else if (op == 2) {
				// Find number of queues containing atleast x pilgrims currently
				ans = mSegmentTree.queryQueues(1, 0, N - 1, x);
				mOut.println(ans);
			} else if (op == 3) {
				// Open doors of all queues having atleast x pilgrims
				mSegmentTree.rangeUpdate(1, 0, N - 1, x);
			}

//			mOut.flush();

		}

		close();

	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	class SegmentTree {
		public Node mTree[];
		public int MAXN;

		public SegmentTree(int N) {
			MAXN = N;
			MAXN |= (MAXN >> 1);
			MAXN |= (MAXN >> 2);
			MAXN |= (MAXN >> 4);
			MAXN |= (MAXN >> 8);
			MAXN |= (MAXN >> 16);
			MAXN = MAXN + 1;
			MAXN = (MAXN << 1) + 1;

			mTree = new Node[MAXN];
		}

		public void buildSegmentTree(int node, int begin, int end, int[] A) {

			int mid;

			if (begin == end) {
				mTree[node] = new Node(A[begin]);
				return;
			}

			mid = begin + ((end - begin) >> 1);

			buildSegmentTree(2 * node, begin, mid, A);
			buildSegmentTree(2 * node + 1, mid + 1, end, A);

			mTree[node] = new Node();
			mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

		}

		public void update(int node, int begin, int end, int index, int pilgrim) {

			int mid;

			if (index < begin || index > end)
				return;

			if (begin == end) {
				mTree[node].updateLeaf(pilgrim);
				return;
			}

			mid = begin + ((end - begin) >> 1);

			update(2 * node, begin, mid, index, pilgrim);
			update(2 * node + 1, mid + 1, end, index, pilgrim);

			mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

		}

		public void rangeUpdate(int node, int begin, int end, int pilgrims) {
			int mid;

			if (begin != end) {
				mTree[node].split(mTree[2 * node], mTree[2 * node + 1]);
			}
			mTree[node].propagate();

			if (pilgrims <= mTree[node].minQueueSize) {
				if (begin != end)
					mTree[node].split(mTree[2 * node], mTree[2 * node + 1], -1);
				mTree[node].update(-1);
				return;
			}
			if (pilgrims > mTree[node].maxQueueSize)
				return;

			mid = begin + ((end - begin) >> 1);

			rangeUpdate(2 * node, begin, mid, pilgrims);
			rangeUpdate(2 * node + 1, mid + 1, end, pilgrims);

			mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

		}

		public int queryQueues(int node, int begin, int end, int pilgrims) {

			int mid;
			int left, right;

			if (begin != end) {
				mTree[node].split(mTree[2 * node], mTree[2 * node + 1]);
			}
			mTree[node].propagate();

			if (pilgrims <= mTree[node].minQueueSize)
				return end - begin + 1;
			if (pilgrims > mTree[node].maxQueueSize)
				return 0;

			mid = begin + ((end - begin) >> 1);

			left = queryQueues(2 * node, begin, mid, pilgrims);
			right = queryQueues(2 * node + 1, mid + 1, end, pilgrims);

			return left + right;
		}
	}

	class Node {
		public int queueSize;
		public int minQueueSize;
		public int maxQueueSize;
		public int lazy;

		public Node() {
			this.queueSize = this.minQueueSize = this.maxQueueSize = this.lazy = 0;
		}

		public Node(int pilgrims) {
			this.queueSize = pilgrims;
			this.lazy = 0;
			minQueueSize = maxQueueSize = pilgrims;
		}

		public void updateLeaf(int pilgrim) {
			this.queueSize += pilgrim;
			this.minQueueSize = this.maxQueueSize = this.queueSize;
		}

		public void update(int pilgrim) {
			this.queueSize += pilgrim;
			this.minQueueSize += pilgrim;
			this.maxQueueSize += pilgrim;
		}

		public void split(Node left, Node right) {
			left.lazy += this.lazy;
			right.lazy += this.lazy;
		}

		public void split(Node left, Node right, int lazy) {
			left.lazy += lazy;
			right.lazy += lazy;
		}

		public void propagate() {
			this.queueSize += this.lazy;
			this.minQueueSize += this.lazy;
			this.maxQueueSize += this.lazy;
			this.lazy = 0;
		}

		public void merge(Node left, Node right) {
			this.minQueueSize = Math.min(left.minQueueSize, right.minQueueSize);
			this.maxQueueSize = Math.max(left.maxQueueSize, right.maxQueueSize);
		}
	}

	public static void main(String[] args) {
		TempleQueue mSol = new TempleQueue();
		mSol.solve();
	}

}
