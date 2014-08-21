package com.segment;

import java.io.PrintWriter;
import java.util.StringTokenizer;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/HELPR2D2/
 * 
 * AC in SPOJ! :D
 * 
 * Solution using segment tree. Should be done by using Interval Tree.
 * 
 * @author sultan.of.swing
 * 
 */

public class HelpR2D2 {

	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public static SegmentTree mSegTree;
	public int T;
	public int[] mK;
	public int mStarShips;

	public HelpR2D2() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
		mStarShips = 100004;
		mSegTree = new SegmentTree(mStarShips);
		mK = new int[mStarShips];
	}

	public void solve() {
		int K, N;
		StringTokenizer st;
		String str;
		int val;
		int rep;
		int maxShips;

		T = mFScanner.nextInt();

		while (T-- > 0) {
			K = mFScanner.nextInt();
			N = mFScanner.nextInt();

			for (int i = 0; i < mStarShips; i++) {
				mK[i] = K;
			}

			maxShips = Math.min(N, mStarShips);

			mSegTree.buildSegmentTree(1, 0, maxShips - 1, mK);

			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(mFScanner.nextLine());
				str = st.nextToken();
				rep = 1;
				if (str.charAt(0) == 'b') {
					rep = Integer.parseInt(st.nextToken());
					val = Integer.parseInt(st.nextToken());
				} else {
					val = Integer.parseInt(str);
				}

				for (int j = 0; j < rep; j++) {
					if (val == 0)
						break;
					mSegTree.update(1, 0, maxShips - 1, val);
				}

				i += rep - 1;
			}
			mOut.print(mSegTree.getNode(1).wastedSpaceship);
			mOut.print(" ");
			mOut.println(mSegTree.getNode(1).wastedSpace);

		}

		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		HelpR2D2 mSol = new HelpR2D2();
		mSol.solve();
	}

	class SegmentTree {
		private int MAXN;
		private Node mTree[];

		public SegmentTree(int N) {
			setMAXN(N);
			init();
		}

		public void init() {
			mTree = new Node[MAXN];

			for (int i = 0; i < MAXN; i++)
				mTree[i] = new Node();
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

		public Node getNode(int node) {
			return mTree[node];
		}

		public void buildSegmentTree(int node, int begin, int end, int[] A) {

			int mid;

			if (begin == end) {
				mTree[node].setTotalWeight(A[begin]);
				return;
			}

			mid = begin + ((end - begin) >> 1);

			buildSegmentTree(2 * node, begin, mid, A);
			buildSegmentTree(2 * node + 1, mid + 1, end, A);

			mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);

		}

		public void update(int node, int begin, int end, int weight) {

			int mid;

			if (begin == end) {
				mTree[node].updateLeaf(weight);
				return;
			}

			mid = begin + ((end - begin) >> 1);

			// Check if there is space left in a starship in the left subtree of
			// current node. If so then go left, else go right
			if (weight <= mTree[node].maxLeftWeight) {
				update(2 * node, begin, mid, weight);
			} else {
				update(2 * node + 1, mid + 1, end, weight);
			}

			mTree[node].merge(mTree[2 * node], mTree[2 * node + 1]);
		}
	}

	class Node {
		int maxLeftWeight, maxRightWeight;
		int remainingWeight;
		int wastedSpace;
		int wastedSpaceship;

		public Node() {
			maxLeftWeight = maxRightWeight = remainingWeight = wastedSpace = wastedSpaceship = 0;
		}

		public void setTotalWeight(int totalWeight) {
			maxLeftWeight = maxRightWeight = totalWeight;
			remainingWeight = totalWeight;
			wastedSpace = 0;
			wastedSpaceship = 0;
		}

		public void updateLeaf(int containerWeight) {
			this.remainingWeight -= containerWeight;
			this.maxLeftWeight = this.maxRightWeight = this.remainingWeight;
			this.wastedSpace = this.remainingWeight;
			this.wastedSpaceship = 1;
		}

		public void merge(Node left, Node right) {
			// Store the maximum empty space in a starship in the left as well
			// as the right subtree of current node.
			this.maxLeftWeight = Math.max(left.maxLeftWeight,
					left.maxRightWeight);
			this.maxRightWeight = Math.max(right.maxLeftWeight,
					right.maxRightWeight);
			this.wastedSpace = left.wastedSpace + right.wastedSpace;
			this.wastedSpaceship = left.wastedSpaceship + right.wastedSpaceship;
		}
	}
}
