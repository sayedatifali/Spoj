package com.bit;

import java.io.PrintWriter;

/**
 * http://www.spoj.com/problems/MATSUM/
 * 
 * TLE in JAVA
 * 
 * @author doom
 * 
 */

public class MatrixSum {

	public FastScanner mFastScanner;
	public PrintWriter mOut;
	public int N;
	public int T;
	public static int MAXN;
	public long bit[][];

	public MatrixSum() {
		mFastScanner = new FastScanner();
		mOut = new PrintWriter(System.out);
		bit = new long[1030][1030];
		MAXN = 1024;
	}

	public void clearBIT() {
		for (int i = 0; i <= MAXN; i++) {
			for (int j = 0; j <= MAXN; j++) {
				bit[i][j] = 0;
			}
		}
	}

	public void update(int idx, int idy, long val) {
		while (idx <= MAXN) {
			updatey(idx, idy, val);
			idx += (idx & -idx);
		}
	}

	private void updatey(int idx, int idy, long val) {
		while (idy <= MAXN) {
			bit[idx][idy] += val;
			idy += (idy & -idy);
		}
	}

	public long query(int idx, int idy) {
		long sum = 0;
		while (idx > 0) {
			sum += queryy(idx, idy);
			idx -= (idx & -idx);
		}
		return sum;
	}

	private long queryy(int idx, int idy) {
		long sum = 0;
		while (idy > 0) {
			sum += bit[idx][idy];
			idy -= (idy & -idy);
		}
		return sum;
	}

	public void solve() {
		int x, y;
		int x1, x2, y1, y2;
		long ans;
		long num;
		String st;

		T = mFastScanner.nextInt();

		while (T-- > 0) {
			N = mFastScanner.nextInt();
			MAXN = N + 2;
			clearBIT();
			while (true) {
				st = mFastScanner.next();
				if (st.equalsIgnoreCase("SET")) {
					x = mFastScanner.nextInt() + 1;
					y = mFastScanner.nextInt() + 1;
					num = mFastScanner.nextLong();

					update(x, y, num);

				} else if (st.equalsIgnoreCase("SUM")) {
					x1 = mFastScanner.nextInt();
					y1 = mFastScanner.nextInt();
					x2 = mFastScanner.nextInt() + 1;
					y2 = mFastScanner.nextInt() + 1;

					ans = query(x2, y2) - query(x2, y1) - query(x1, y2)
							+ query(x1, y1);

					mOut.println(ans);

				} else {
					break;
				}
			}
			mOut.flush();
		}

		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		MatrixSum mSol = new MatrixSum();
		mSol.solve();
	}

}