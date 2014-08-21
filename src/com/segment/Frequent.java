package com.segment;

import java.io.PrintWriter;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/FREQUENT/
 * 
 * AC in SPOJ!! :D
 * 
 * @author doom
 * 
 */

public class Frequent {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public SegmentTree mSegTree;
	public int N;
	public int[] A;

	public Frequent() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}

	public void solve() {
		int N, Q;
		int u, v;
		Node res;
		
		mSegTree = new SegmentTree(100005);
		A = new int[100005];

		while (true) {

			N = mFScanner.nextInt();
			
			if (N == 0)
				break;
			
			Q = mFScanner.nextInt();

			mSegTree.setN(N);

			for (int i = 0; i < N; i++) {
				A[i] = mFScanner.nextInt();
			}

			mSegTree.buildSegmentTree(1, 0, N - 1, A);

			for (int i = 0; i < Q; i++) {
				u = mFScanner.nextInt() - 1;
				v = mFScanner.nextInt() - 1;
				res = mSegTree.query(1, 0, N - 1, u, v);
				mOut.println(res.bestCount);
			}

		}

		close();

	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		Frequent mSolution = new Frequent();
		mSolution.solve();
	}

	class SegmentTree {
		public int N;
		public int MAXN;
		public Node[] mTree;

		public SegmentTree(int N) {
			setN(N);
			setMAXN(N);
			mTree = new Node[MAXN];
			for (int i = 0; i < MAXN; i++) {
				mTree[i] = new Node();
			}
		}
		
		public void setN(int N) {
			this.N = N;
		}
		
		public void setMAXN(int N) {
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
				mTree[node].setNum(A[begin]);
				return;
			}

			mid = begin + ((end - begin) >> 1);

			buildSegmentTree(node * 2, begin, mid, A);
			buildSegmentTree(node * 2 + 1, mid + 1, end, A);

			mTree[node].mergeNode(mTree[node * 2], mTree[2 * node + 1]);

		}

		public Node query(int node, int begin, int end, int qBegin, int qEnd) {

			int mid;
			Node left, right;
			Node res;

			left = right = null;

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

			res.mergeNode(left, right);

			return res;
		}
	}

	class Node {

		int prefixNum;
		int prefixCount;
		int suffixNum;
		int suffixCount;
		int bestCount;

		public Node() {
			prefixCount = prefixNum = 0;
			suffixNum = suffixCount = 0;
			bestCount = 0;
		}
		
		public void setNum(int num) {
			prefixNum = suffixNum = num;
			prefixCount = suffixCount = 1;
			bestCount = 1;
		}
		
		public void mergeNode(Node left, Node right) {

			this.prefixNum = left.prefixNum;
			this.prefixCount = left.prefixCount;

			if (left.prefixNum == right.prefixNum) {
				this.prefixCount = left.prefixCount + right.prefixCount;
			}

			this.suffixNum = right.suffixNum;
			this.suffixCount = right.suffixCount;

			if (left.suffixNum == right.suffixNum) {
				this.suffixCount = left.suffixCount + right.suffixCount;
			}

			this.bestCount = 0;
			
			if (left.suffixNum == right.prefixNum) {
				this.bestCount = left.suffixCount + right.prefixCount;
			}

			this.bestCount = Math.max(this.bestCount,
					Math.max(left.bestCount, right.bestCount));
			this.bestCount = Math.max(this.bestCount,
					Math.max(this.prefixCount, this.suffixCount));

		}
	}

//	class Node {
//
//		int prefixNum;
//		int prefixCount;
//		int suffixNum;
//		int suffixCount;
//		int segmentNum;
//		int segmentCount;
//		int bestCount;
//
//		public Node() {
//			prefixCount = prefixNum = 0;
//			suffixNum = suffixCount = 0;
//			bestCount = 0;
//			segmentNum = segmentCount = 0;
//		}
//
//		public Node(int num) {
//			prefixNum = suffixNum = segmentNum = num;
//			prefixCount = suffixCount = segmentCount = 1;
//			bestCount = 1;
//		}
//
//		public void merge(Node left, Node right) {
//
//			if (left.suffixNum == right.prefixNum) {
//				this.segmentNum = left.suffixNum;
//				this.segmentCount = left.suffixCount + right.prefixCount;
//			} else {
//				if (left.segmentCount > right.segmentCount) {
//					this.segmentCount = left.segmentCount;
//					this.segmentNum = left.segmentNum;
//				} else {
//					this.segmentCount = right.segmentCount;
//					this.segmentNum = right.segmentNum;
//				}
//			}
//
//			this.prefixNum = left.prefixNum;
//			this.prefixCount = left.prefixCount;
//
//			if (left.prefixNum == right.prefixNum) {
//				this.prefixCount = left.prefixCount + right.prefixCount;
//			}
//
//			this.suffixNum = right.suffixNum;
//			this.suffixCount = right.suffixCount;
//
//			if (left.suffixNum == right.suffixNum) {
//				this.suffixCount = left.suffixCount + right.suffixCount;
//			}
//
//			this.bestCount = Math.max(this.segmentCount,
//					Math.max(this.prefixCount, this.suffixCount));
//
//		}
//	}

}
