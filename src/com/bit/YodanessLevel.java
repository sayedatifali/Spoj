package com.bit;

import java.io.PrintWriter;
import java.util.HashMap;

/**
 * http://www.spoj.com/problems/YODANESS/
 * 
 * AC :)
 * 
 * @author doom
 * 
 */

public class YodanessLevel {

	public FastScanner mFastScanner;
	public PrintWriter mOut;
	public int N;
	public int T;
	public static int MAXN;
	public long bit[];
	public int array[];
	public HashMap<String, Integer> mHashMap;

	public YodanessLevel() {
		mFastScanner = new FastScanner();
		mOut = new PrintWriter(System.out);
		bit = new long[30009];
		MAXN = 30000;
		mHashMap = new HashMap<String, Integer>();
	}

	public void clearBIT() {
		for (int i = 0; i <= MAXN; i++) {
			bit[i] = 0;
		}
	}

	public void update(int idx, long val) {
		while (idx <= MAXN) {
			bit[idx] += val;
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

	public void solve() {
		int i, j;
		long ans;
		String str;

		T = mFastScanner.nextInt();

		while (T-- > 0) {
			N = mFastScanner.nextInt();
			MAXN = N + 2;
			clearBIT();
			mHashMap.clear();

			array = new int[N + 1];

			for (i = 1; i <= N; i++) {
				str = mFastScanner.next();
				mHashMap.put(str, i);
			}

			for (i = 1; i <= N; i++) {
				str = mFastScanner.next();
				array[i] = mHashMap.get(str);
			}

			ans = 0;

			for (i = N; i > 0; i--) {
				ans += query(array[i] - 1);
				update(array[i], 1);
			}

			mOut.println(ans);

//			mOut.flush();
		}

		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		YodanessLevel mSol = new YodanessLevel();
		mSol.solve();
	}

}