package com.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FastScanner {
	BufferedReader br;
	StringTokenizer st;

	public FastScanner() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	public String next() {
		while (st == null || !st.hasMoreElements()) {
			try {
				st = new StringTokenizer(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return st.nextToken();
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}

	public long nextLong() {
		return Long.parseLong(next());
	}

	public double nextDouble() {
		return Double.parseDouble(next());
	}

	public String nextLine() {
		String str = "";
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public char[] nextCharArray(int N) {
		int i;
		char[] array;
		String str;

		array = new char[N];

		i = 0;

		str = nextLine();

		for (i = 0; i < N && i < str.length(); i++) {
			array[i] = str.charAt(i);
		}

		return array;
	}

	public char[][] nextChar2DArray(int M, int N) {
		int i;
		char[][] array;

		array = new char[M][N];

		i = 0;

		for (i = 0; i < M; i++) {
			array[i] = nextCharArray(N);
		}

		return array;
	}

	public int[] nextIntArray(int N) {
		int i;
		int[] array;

		array = new int[N];

		i = 0;

		for (i = 0; i < N; i++) {
			array[i] = nextInt();
		}

		return array;
	}

	public int[][] nextInt2DArray(int M, int N) {
		int i;
		int[][] array;

		array = new int[M][N];

		i = 0;

		for (i = 0; i < M; i++) {
			array[i] = nextIntArray(N);
		}

		return array;
	}

	public long[] nextLongArray(int N) {
		int i;
		long[] array;

		array = new long[N];

		i = 0;

		for (i = 0; i < N; i++) {
			array[i] = nextLong();
		}

		return array;
	}

	public long[][] nextLong2DArray(int M, int N) {
		int i;
		long[][] array;

		array = new long[M][N];

		i = 0;

		for (i = 0; i < M; i++) {
			array[i] = nextLongArray(N);
		}

		return array;
	}
}