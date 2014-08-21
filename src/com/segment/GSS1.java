package com.segment;

import java.io.PrintWriter;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/GSS1/
 * 
 * TLE in SPOJ
 * 
 * @author doom
 * 
 */

public class GSS1 {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public int N;
	public int M;
	public int MAXN;
	public Node mTree[];
	public long A[];

	public GSS1() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void initSegmentTree() {
		A = new long[N];
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

	public void buildSegmentTree(int node, int begin, int end) {
		int mid;

		if (begin == end) {
			mTree[node] = new Node(A[begin]);
			return;
		}

		mid = begin + (end - begin) / 2;

		buildSegmentTree(2 * node, begin, mid);
		buildSegmentTree(2 * node + 1, mid + 1, end);

		mTree[node] = new Node();

		mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

	}

	public Node query(int node, int begin, int end, int qStartIndex,
			int qEndIndex) {

		int mid;
		Node left, right;
		Node n;

		if (qEndIndex < begin || qStartIndex > end)
			return null;

		if (qStartIndex <= begin && qEndIndex >= end)
			return mTree[node];

		mid = begin + (end - begin) / 2;

		left = right = null;

		if (qStartIndex <= mid)
			left = query(2 * node, begin, mid, qStartIndex, qEndIndex);

		if (qEndIndex > mid)
			right = query(2 * node + 1, mid + 1, end, qStartIndex, qEndIndex);

		if (left == null)
			return right;
		if (right == null)
			return left;

		n = new Node();
		n.merge(left, right);

		return n;
	}

	public void solve() {
		int i, a, b;
		Node ans;
		StringBuilder strB;
		N = mFScanner.nextInt();
		initSegmentTree();
		for (i = 0; i < N; i++) {
			A[i] = mFScanner.nextLong();
		}
		buildSegmentTree(1, 0, N - 1);
		M = mFScanner.nextInt();
		strB = new StringBuilder(4 * M);
		for (i = 0; i < M; i++) {
			a = mFScanner.nextInt() - 1;
			b = mFScanner.nextInt() - 1;
			ans = query(1, 0, N - 1, a, b);
			strB.append(ans.bestSum);
			strB.append("\n");
		}
		mOut.print(strB);
		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	class Node {
		public long value;
		public long bestSum;
		public long segmentSum;
		public long prefixSum;
		public long suffixSum;

		public Node() {
			this.bestSum = this.segmentSum = this.prefixSum = this.suffixSum = Integer.MIN_VALUE;
			this.value = -1; // Not a leaf node
		}

		// Constructor for leaf node
		public Node(long val) {
			this.value = this.bestSum = this.segmentSum = this.prefixSum = this.suffixSum = val;
		}

		public void merge(Node left, Node right) {
			this.segmentSum = left.segmentSum + right.segmentSum;
			this.prefixSum = Math.max(left.prefixSum, left.segmentSum
					+ right.prefixSum);
			this.suffixSum = Math.max(right.suffixSum, left.suffixSum
					+ right.segmentSum);
			this.bestSum = Math.max(Math.max(left.bestSum, right.bestSum),
					left.suffixSum + right.prefixSum);
		}
	}

	public static void main(String[] args) {
		GSS1 mSol = new GSS1();
		mSol.solve();
	}

}
