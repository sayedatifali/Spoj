package com.flow;

import java.io.PrintWriter;

public class Stub {
	
	public FasterScanner mFScanner;
	public PrintWriter mOut;
	
	public Stub() {
		mFScanner = new FasterScanner();
		mOut = new PrintWriter(System.out);
	}
	
	public void solve() {
		
	}
	
	public void flush() {
		mOut.flush();
	}
	
	public void close() {
		mOut.close();
	}
	
	public static void main(String [] args) {
		Stub mSol = new Stub();
		mSol.solve();
		mSol.flush();
		mSol.close();
	}

}
