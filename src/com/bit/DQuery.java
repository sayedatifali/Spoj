package com.bit;

import java.io.PrintWriter;
import java.util.Arrays;

/**
 * http://www.spoj.com/problems/DQUERY/
 * 
 * TLE in Java
 * 
 * @author doom
 * 
 */

public class DQuery {

	public FastScanner mFastScanner;
	public PrintWriter mOut;
	public int N;
	public int Q;
	public int NQ;
	public int T;
	public static int MAXN;
	public int bit[];
	public Events array[];
	public int numbers[];
	public int ans[];
	public int past[];

	public DQuery() {
		mFastScanner = new FastScanner();
		mOut = new PrintWriter(System.out);
		bit = new int[30009];
	}

	public void update(int idx, int val) {
		while (idx <= MAXN) {
			bit[idx] += val;
			idx += (idx & -idx);
		}
	}

	public int query(int idx) {
		int sum = 0;
		while (idx > 0) {
			sum += bit[idx];
			idx -= (idx & -idx);
		}
		return sum;
	}

	public void solve() {
		int i;
		int j;
		int res;

		N = mFastScanner.nextInt();

		numbers = new int[N];

		for (i = 0; i < N; i++)
			numbers[i] = mFastScanner.nextInt();

		Q = mFastScanner.nextInt();

		ans = new int[Q];
		array = new Events[N + Q];

		for (i = 0; i < N; i++) {
			// Storing numbers
			array[i] = new Events();
			array[i].endIndex = i + 1;
			array[i].val = numbers[i];
		}

		for (i = 0; i < Q; i++) {
			array[N + i] = new Events();
			array[N + i].startIndex = mFastScanner.nextInt();
			array[N + i].endIndex = mFastScanner.nextInt();
			array[N + i].query = i + 1;
		}

		Arrays.sort(array);

		MAXN = N + 2;
		NQ = N + Q;

		past = new int[1000001];

		j = 1;

		for (i = 0; i < NQ; i++) {
			// Number Event
			if (array[i].query == 0) {
				if (past[array[i].val] != 0) {
					update(past[array[i].val], -1);
				}
				update(array[i].endIndex, 1);
				past[array[i].val] = j;
				j++;
			}

			// Query Event
			else {
				res = query(array[i].endIndex);
				res -= query(array[i].startIndex - 1);
				ans[array[i].query - 1] = res;
			}
		}

		for (i = 0; i < Q; i++)
			mOut.println(ans[i]);

		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	class Events implements Comparable<Events> {
		public int val;
		public int startIndex;
		public int endIndex;
		public int query; // 1 for query

		public Events() {
			val = startIndex = endIndex = query = 0;
		}

		@Override
		public int compareTo(Events arg0) {
			// TODO Auto-generated method stub
			if (this.endIndex < arg0.endIndex)
				return -1;
			else if (this.endIndex > arg0.endIndex)
				return 1;
			else if (this.query == 0 && arg0.query != 0)
				return -1;
			else if (this.query != 0 && arg0.query == 0)
				return 1;

			return 0;
		}
	}

	public static void main(String[] args) {
		DQuery mSol = new DQuery();
		mSol.solve();
	}

}