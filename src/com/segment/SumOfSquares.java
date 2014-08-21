package com.segment;

import java.io.PrintWriter;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/SEGSQRSS/
 * 
 * AC in SPOJ! :D
 * 
 * @author sultan.of.swing
 * 
 */

public class SumOfSquares {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	private SegmentTree mSegTree;
	public int[] mNum;

	public SumOfSquares() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void solve() {
		int i, j;
		int T;
		int N, Q;
		int u, v;
		Node ans;
		int op;
		int x;

		T = mFScanner.nextInt();

		for (i = 0; i < T; i++) {

			N = mFScanner.nextInt();
			Q = mFScanner.nextInt();

			mNum = new int[N];
			mSegTree = new SegmentTree(N);

			for (j = 0; j < N; j++)
				mNum[j] = mFScanner.nextInt();

			mSegTree.buildSegmentTree(1, 0, N - 1, mNum);

			mOut.println("Case " + (i + 1) + ":");

			for (j = 0; j < Q; j++) {
				op = mFScanner.nextInt();
				u = mFScanner.nextInt() - 1;
				v = mFScanner.nextInt() - 1;

				if (op == 2) { // Query operation
					ans = mSegTree.query(1, 0, N - 1, u, v);
					mOut.println(ans.squareSum);
				} else if (op == 1) { // Update operation - add value
					x = mFScanner.nextInt();
					mSegTree.rangeUpdate(1, 0, N - 1, u, v, x, true);
				} else if (op == 0) { // Update operation - set value
					x = mFScanner.nextInt();
					mSegTree.rangeUpdate(1, 0, N - 1, u, v, x, false);
				}
			}

		}

		close();

	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		SumOfSquares mSolution = new SumOfSquares();
		mSolution.solve();
	}

	class SegmentTree {
		public int N;
		public int MAXN;
		public Node mTree[];

		public SegmentTree(int N) {
			this.N = N;
			getSize(N);
			mTree = new Node[MAXN];
		}

		public void getSize(int N) {
			MAXN = N;
			MAXN |= (MAXN >> 1);
			MAXN |= (MAXN >> 2);
			MAXN |= (MAXN >> 4);
			MAXN |= (MAXN >> 8);
			MAXN |= (MAXN >> 16);
			MAXN = (MAXN + 1) << 1;
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

		public void rangeUpdate(int node, int begin, int end, int upBegin,
				int upEnd, long value, boolean add) {

			int mid;

			if (begin != end) {
				mTree[node].split(mTree[node * 2], mTree[node * 2 + 1]);
			}
			mTree[node].propagate(end - begin + 1);

			if (upBegin > end || upEnd < begin) {
				return;
			}

			if (upBegin <= begin && upEnd >= end) {
				mTree[node].update(value, add, end - begin + 1);
				if (begin != end) {
					mTree[node].split(mTree[2 * node], mTree[2 * node + 1],
							add, value);
				}
				return;
			}

			mid = begin + ((end - begin) >> 1);

			rangeUpdate(2 * node, begin, mid, upBegin, upEnd, value, add);
			rangeUpdate(2 * node + 1, mid + 1, end, upBegin, upEnd, value, add);

			mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

		}

		public Node query(int node, int begin, int end, int qBegin, int qEnd) {

			int mid;

			Node left, right;
			Node res;

			left = right = null;

			if (begin != end)
				mTree[node].split(mTree[node * 2], mTree[node * 2 + 1]);

			mTree[node].propagate(end - begin + 1);

			if (qBegin > end || qEnd < begin)
				return null;

			if (qBegin <= begin && qEnd >= end) {
				return mTree[node];
			}

			mid = begin + ((end - begin) >> 1);

			if (qBegin <= mid)
				left = query(2 * node, begin, mid, qBegin, qEnd);

			if (qEnd > mid)
				right = query(2 * node + 1, mid + 1, end, qBegin, qEnd);

			if (left == null)
				return right;
			if (right == null)
				return left;

			res = new Node();
			res.merge(left, right);

			return res;
		}

	}

	class Node {
		long numSum;
		long lazySum;
		long lazySet;
		long squareSum;

		public Node() {
			lazySum = 0;
			lazySet = Long.MIN_VALUE;
			numSum = 0;
			squareSum = 0;
		}

		public Node(long num) {
			this.numSum = num;
			lazySum = 0;
			lazySet = Long.MIN_VALUE;
			squareSum = num * num;
		}

		public void split(Node left, Node right) {
			if (this.lazySet != Long.MIN_VALUE) {
				left.lazySet = this.lazySet;
				right.lazySet = this.lazySet;
				left.lazySum = right.lazySum = 0;
			}
			if (this.lazySum != 0) {
				left.lazySum += this.lazySum;
				right.lazySum += this.lazySum;
			}
		}

		public void split(Node left, Node right, boolean add, long value) {
			if (!add) { // Set a new value. Remove all old sum values
				left.lazySet = right.lazySet = value;
				left.lazySum = right.lazySum = 0;
			} else {
				left.lazySum += value;
				right.lazySum += value;
			}
		}

		public void propagate(int length) {
			if (this.lazySet != Long.MIN_VALUE) {
				this.numSum = this.lazySet * length;
				this.squareSum = length * this.lazySet * this.lazySet;
			}

			this.numSum += length * this.lazySum;

			// Updating by the logic that
			// (a + b) ^ 2 = a ^ 2 + 2 * a * b + b ^ 2
			// New square = old square sum + 2 * oldSum * length * newExtraSum +
			// length * (newExtraSum ^ 2)

			this.squareSum = this.squareSum + 2 * this.numSum
					* (length * this.lazySum) + length * this.lazySum
					* this.lazySum;

			this.lazySum = 0;
			this.lazySet = Long.MIN_VALUE;
		}

		public void update(long value, boolean add, int length) {
			if (add) {
				this.squareSum = this.squareSum + 2 * this.numSum * value
						+ length * value * value;
				this.numSum += length * value;
			} else {
				this.squareSum = length * value * value;
				this.numSum = length * value;
			}

		}

		public void merge(Node left, Node right) {
			this.numSum = left.numSum + right.numSum;
			this.squareSum = left.squareSum + right.squareSum;
		}
	}

}

// TestCase:
// 1
// 7 5
// 3 2 5 1 4 9 8
// 2 1 7
// 0 5 7 1
// 2 1 7
// 1 3 4 2
// 2 1 4
//
// 200
// 42
// 71
