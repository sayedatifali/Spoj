package com.bit;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.io.FasterScanner;

/**
 * http://www.spoj.com/problems/ORDERSET/
 * 
 * @author sultan.of.swing
 *
 */

public class OrderSet {
	
	public FasterScanner mFScanner;
	public PrintWriter mOut;
	public char [] mOp;
	public int [] mNum;
	public int [] mCoord;
	public int [] mReverseMap;
	public HashMap<Integer, Integer> mMapping;
	public BIT mBIT;
	
	public OrderSet() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}
	
	public void solve() {
		int i;
		int mappedNumber;
		int Q;
		int res;
		StringTokenizer st;
		
		Q = mFScanner.nextInt();
		mOp = new char[Q];
		mNum = new int[Q];
		mCoord = new int[Q];
		mReverseMap = new int[Q + 10];
		mMapping = new HashMap<Integer, Integer>();
		mBIT = new BIT(Q + 1);
		
		for (i = 0; i < Q; i++) {
			st = new StringTokenizer(mFScanner.nextLine());
			mOp[i] = st.nextToken().charAt(0);
			mNum[i] = Integer.parseInt(st.nextToken());
			mCoord[i] = mNum[i];
		}
		
		
		// Coordinate compression
		
		Arrays.sort(mCoord);
		
		mappedNumber = 1;
		
		mMapping.put(mCoord[0], 1);
		mReverseMap[mappedNumber] = mCoord[0];
		
		for (i = 1; i < Q; i++) {
			if (mCoord[i] != mCoord[i - 1]) {
				mappedNumber++;
				mMapping.put(mCoord[i], mappedNumber);
				mReverseMap[mappedNumber] = mCoord[i];
			}
		}
		
		// Process queries
		
		for (i = 0; i < Q; i++) {
			if (mOp[i] == 'I') {
				mBIT.update(mMapping.get(mNum[i]), 1);				
			} else if (mOp[i] == 'D') {
				mBIT.update(mMapping.get(mNum[i]), -1);
			} else if (mOp[i] == 'C') {
				res = mBIT.smaller(mMapping.get(mNum[i]));
				mOut.println(res);
			} else if (mOp[i] == 'K') {
				res = mBIT.rank(mNum[i]);
				if (res == -1)
					mOut.println("invalid");
				else
					mOut.println(mReverseMap[res]);
			}
		}
		
		close();
		
	}
	
	public void close() {
		mOut.flush();
		mOut.close();
	}
	
	public static void main(String [] args) {
		OrderSet mSol = new OrderSet();
		mSol.solve();
	}
	
	class BIT {
		public int [] mTree;
		public boolean [] mStatus;
		public int N;
		
		public BIT(int N) {
			this.N = N + 10;
			mTree = new int[this.N];
			mStatus = new boolean[this.N];
		}
		
		public void update(int idx, int value) {
			if (mStatus[idx] && value == 1)
				return;
			if (!mStatus[idx] && value == -1)
				return;
			if (mStatus[idx] && value == -1)
				mStatus[idx] = false;
			else if (!mStatus[idx] && value == 1)
				mStatus[idx] = true;
			while (idx < N) {
				mTree[idx] += value;
				idx += (idx & -idx);
			}
		}
		
		public int query(int idx) {
			int sum;
			sum = 0;
			while (idx > 0) {
				sum += mTree[idx];
				idx -= (idx & -idx);
			}
			return sum;
		}
		
		public int rank(int num) {
			return getRank(0, N - 1, num);
		}
		
		private int getRank(int start, int end, int val) {
			
			int mid;
			int res;
			
			mid = start + ((end - start) >> 1);
			
			res = query(mid);
			
			if (res == val && start == end)
				return start;
			if (start >= end)
				return -1;
			
			if (res < val) {
				res = getRank(mid + 1, end, val);
			} else {
				res = getRank(0, mid, val);
			}
			
			return res;
		}
		
		public int smaller(int num) {
			int ans;
			ans = query(num - 1);
			return ans;
		}
		
	}

}
