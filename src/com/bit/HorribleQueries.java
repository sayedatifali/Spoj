package com.bit;

import java.io.PrintWriter;

/**
 * Range update, range query
 * 
 * Explanation:
 * 
 * @link 
 *       http://apps.topcoder.com/forums/?module=Thread&threadID=715842&start=0&mc
 *       =8
 * 
 * @author doom
 * 
 */

public class HorribleQueries {

	private long tree1[];
	private long tree2[];
	private int MAXN;

	public HorribleQueries(int N) {
		MAXN = N + 1;
		tree1 = new long[MAXN];
		tree2 = new long[MAXN];
	}

	private void update(long[] tree, int idx, long val) {

		while (idx < MAXN) {
			tree[idx] += val;
			idx += (idx & -idx);
		}
	}

	private long read(long[] tree, int idx) {
		long sum = 0;

		while (idx > 0) {
			sum += tree[idx];
			idx -= (idx & -idx);
		}

		return sum;
	}

	public long getRangeSum(int a, int b) {
		return read(b) - read(a - 1);
	}

	private long read(int p) {
		return read(tree1, p) * p - read(tree2, p);
	}

	public void update(int a, int b, long val) {
		update(tree1, a, val);
		update(tree1, b + 1, -val);
		update(tree2, a, val * (a - 1));
		update(tree2, b + 1, -val * b);
	}

	public static void main(String[] args) {

		int T, N, C;
		int p, q;
		long value;
		int type;
		HorribleQueries mBit;
		FastScanner fScanner = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);

		T = fScanner.nextInt();

		while (T-- > 0) {
			N = fScanner.nextInt();
			C = fScanner.nextInt();

			mBit = new HorribleQueries(N);

			while (C-- > 0) {
				type = fScanner.nextInt();

				// Update
				if (type == 0) {
					p = fScanner.nextInt();
					q = fScanner.nextInt();
					value = fScanner.nextLong();

					mBit.update(p, q, value);

				}
				// Query
				else {
					p = fScanner.nextInt();
					q = fScanner.nextInt();
					value = mBit.getRangeSum(p, q);
					out.println(value);
				}
			}

			out.flush();

		}

		out.close();

		// System.out.println(mBit.getRangeSum(3, 4));

	}

}
