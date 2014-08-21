package com.bit;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * http://www.spoj.com/problems/CTRICK/
 * 
 * @author doom
 *
 */

public class CardTrick {

	public FastScanner mFastScanner;
	public PrintWriter mOut;
	public int N;
	public int T;
//	public ArrayList<Integer> mSeq;
	public int [] mFinal;
	public static int MAXN;
	public int bit[];

	public CardTrick() {
		mFastScanner = new FastScanner();
		mOut = new PrintWriter(System.out);
//		mSeq = new ArrayList<>();
		mFinal = new int[20009];
		bit = new int[20009];
	}
	
	public void initBit() {
		for (int i = 0; i <= MAXN; i++) {
			bit[i] = 0;
		}
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

	/*
	public void solveTestCase() {
		
		int size;
		int index;
		int count;
		int next;
		
		index = 1;
		mSeq.remove(index);
		count = 2;
		next = 2;
		
		while (mSeq.size() > 0) {
			size = mSeq.size();			
			index = (index + count) % size;
			
//			mFinal.set(mSeq.get(index), next);
			mFinal[mSeq.get(index)] = next;
			mSeq.remove(index);
			count++;
			next++;
			
		}
	}
	*/
	
	public void solveUsingBit() {
		int i;
		int start;
		int end;
		int mid;
		int L;
		int M;
		int query;
		
		initBit();
		
		for (i = 1; i <= N; i++) 
			update(i, 1);
		
		L = 1;
		M = N;
		
		for (i = 1; i <= N; i++) {
			
			L = (L + i) % M;
			if (L == 0)
				L = M;
			M--;
			
			start = 1;
			end = N;
			
			while (start <= end) {
				mid = (start + end) >> 1;
				query = query(mid);
				if (query >= L)
					end = mid - 1;
				else
					start = mid + 1;
			}
			
			mFinal[start] = i;
			
			update(start, -1);
			
		}
	}

	public void solve() {
		T = mFastScanner.nextInt();

		while (T-- > 0) {
			N = mFastScanner.nextInt();
//			mSeq.clear();
			
			if (N == 1) {
				mOut.println(1);
				mOut.flush();
				continue;
			}
			
			MAXN = N + 2;
			
			solveUsingBit();
			
//			mFinal.ensureCapacity(N);
//			mFinal.set(1, 1);
//			mFinal[1] = 1;
//			for (int i = 0; i < N; i++) {
//				mSeq.add(i);
//			}
//			
//			solveTestCase();

			for (int i = 1; i <= N; i++) {
				mOut.print(mFinal[i]);
				if (i != N)
					mOut.print(" ");
			}
			mOut.println();
			mOut.flush();
		}
		close();
	}

	public void close() {
		mOut.flush();
		mOut.close();
	}

	public static void main(String[] args) {
		CardTrick mSol = new CardTrick();
		mSol.solve();
	}

}
