package com.segment;

import java.io.PrintWriter;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/BRCKTS/
 * 
 * AC :)
 * 
 * @author doom
 * 
 */

public class Brackets {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public int N;
	public int M;
	public int MAXN;
	public Node mTree[];
	public int A[];

	public Brackets() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void initSegmentTree() {
		MAXN = N;
		MAXN = MAXN | (MAXN >> 1);
		MAXN = MAXN | (MAXN >> 2);
		MAXN = MAXN | (MAXN >> 4);
		MAXN = MAXN | (MAXN >> 8);
		MAXN = MAXN | (MAXN >> 16);
		MAXN = (MAXN + 1) << 1;
		mTree = new Node[MAXN];
	}

	public void buildSegmentTree(int node, int begin, int end) {
		int mid;

		if (begin == end) {
			int open, close;
			open = close = 0;
			if (A[begin] == -1)
				open = 1;
			else
				close = 1;
			mTree[node] = new Node(open, close);
			return;
		}

		mid = begin + ((end - begin) >> 1);

		buildSegmentTree(2 * node, begin, mid);
		buildSegmentTree(2 * node + 1, mid + 1, end);

		mTree[node] = new Node();
		mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

	}

	public Node query(int node, int begin, int end, int qBegin, int qEnd) {
		int mid;
		Node left, right, n;

		if (qEnd < begin || qBegin > end)
			return null;

		if (qBegin <= begin && qEnd >= end)
			return mTree[node];

		mid = begin + ((end - begin) >> 1);

		left = right = null;

		left = query(2 * node, begin, mid, qBegin, qEnd);
		right = query(2 * node + 1, mid + 1, end, qBegin, qEnd);

		if (left == null)
			return right;

		if (right == null)
			return left;

		n = new Node();

		n.merge(left, right);

		return n;
	}

	public void updateValue(int node, int begin, int end, int index) {
		int mid;

		if (begin == end) {
			mTree[node].reverse();
			return;
		}

		mid = begin + ((end - begin) >> 1);

		if (index >= begin && index <= mid)
			updateValue(2 * node, begin, mid, index);
		if (index > mid && index <= end)
			updateValue(2 * node + 1, mid + 1, end, index);

		mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);
	}

	public String check(Node node) {

		// mOut.println(node.open + " " + node.close);
		if (node.open == node.close && node.open == 0 && node.close == 0)
			return "YES";

		return "NO";
	}

	public void solve() {
		int i;
		int input;
		int index;
		int test;
		Node node;
		String str;
		StringBuilder strB;

		for (test = 1; test <= 10; test++) {

			N = mFScanner.nextInt();
			A = new int[N];
			initSegmentTree();

			str = mFScanner.nextLine();

			for (i = 0; i < N; i++) {
				if (str.charAt(i) == '(')
					A[i] = -1;
				else
					A[i] = 1;
			}

			buildSegmentTree(1, 0, N - 1);

			M = mFScanner.nextInt();
			strB = new StringBuilder(M);

			for (i = 0; i < M; i++) {
				input = mFScanner.nextInt();

				if (input == 0) {
					node = query(1, 0, N - 1, 0, N - 1);
					strB.append(check(node));
					strB.append("\n");
				} else {
					index = input - 1;
					updateValue(1, 0, N - 1, index);
				}
			}
			mOut.println("Test " + test + ":");
			mOut.print(strB);
			mOut.flush();
		}

		close();

	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	class Node {
		int open;
		int close;

		public Node() {
			open = close = 0;
		}

		public Node(int open, int close) {
			this.open = open;
			this.close = close;
		}

		public void reverse() {
			open = 1 - open;
			close = 1 - close;
		}

		public void merge(Node left, Node right) {
			int min;
			min = Math.min(left.open, right.close);
			this.open = left.open + right.open - min;
			this.close = left.close + right.close - min;
		}
	}

	public static void main(String[] args) {
		Brackets mSol = new Brackets();
		mSol.solve();
	}
}
