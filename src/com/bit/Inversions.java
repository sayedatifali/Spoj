package com.bit;

import java.io.PrintWriter;
import java.util.Arrays;

/**
 * http://www.spoj.com/problems/INVCNT/
 * 
 * @author doom
 * 
 */

public class Inversions {

	public FastScanner mFastScanner;
	public PrintWriter mOut;
	// public BufferedReader mBReadLine;
	public Array[] array;
	public int N;
	public static final int MAXN = 200005;
	public static long[] bit = new long[MAXN + 10];

	public Inversions() {
		mFastScanner = new FastScanner();
		mOut = new PrintWriter(System.out);

		// mBReadLine = new BufferedReader(new InputStreamReader(System.in));
		 bit = new long[MAXN + 10];

	}

	public void initBit() {
		for (int i = 0; i <= MAXN; i++)
			bit[i] = 0;
	}

	public void update(int idx, long value) {

		while (idx > 0 && idx <= MAXN) {
			bit[idx] += value;
			idx += (idx & -idx);
		}

	}

	public long query(int idx) {
		long sum = 0;

		while (idx > 0) {
			sum += bit[idx];
			idx -= (idx & -idx);
		}

		return sum;
	}

	public long solveTestCase() {
		int i;
		long ans = 0;

		for (i = 0; i < N; i++) {
			ans += query(array[i].index);
			update(array[i].index, 1);
		}

		return ans;
	}

	public void solve() {
		int T;
		long ans;

		T = mFastScanner.nextInt();

		while (T-- > 0) {
			N = mFastScanner.nextInt();
			// N = (int) (200000 * Math.random()) + 1;
			initBit();
			array = new Array[N];
			for (int i = 0; i < N; i++) {
				array[i] = new Array(N - i, mFastScanner.nextInt());
			}
			
			Arrays.sort(array);
			// array = new int[N];
			// for (int i = 0; i < N; i++) {
			// array[i] = (int) (MAXN * Math.random()) + 1;
			// }
			// mFastScanner.nextLine();
			ans = solveTestCase();
			// mOut.println("N = " + N + "; ans = " + ans);
			// ans = 0;
			mOut.println(ans);
			mOut.flush();
		}

		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		Inversions mSolution = new Inversions();
		mSolution.solve();
	}
	
	class Array implements Comparable<Array>{
		int index;
		int val;
		
		public Array(int index, int val) {
			this.index = index;
			this.val = val;
		}

		@Override
		public int compareTo(Array arg0) {
			// TODO Auto-generated method stub
			if (this.val < arg0.val)
				return -1;
			if (this.val > arg0.val)
				return 1;
			return 0;
		}
		
		
	}

}