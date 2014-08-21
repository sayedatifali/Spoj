package com.util;

public class Utilities {

	public boolean nextPerm(int[] a) {

		int i;
		int j;
		int N;
		int temp;

		N = a.length;
		i = N - 2;

		for (; i >= 0; i--) {
			if (a[i] < a[i + 1])
				break;
		}
		if (i < 0)
			return false;

		for (j = N - 1; j >= i; j--) {
			if (a[j] > a[i]) {
				temp = a[j];
				a[j] = a[i];
				a[i] = temp;
				break;
			}
		}

		for (j = i + 1; j < (N + i + 1) / 2; j++) {
			temp = a[N - j + i];
			a[N - j + i] = a[j];
			a[j] = temp;
		}

		return true;
	}

	public long nCr(long n, long r) {

		long ans = 1;

		if (r > n)
			return 0;

		for (long i = 1; i <= r; i++) {
			ans = ans * (n - i + 1) / i;
		}
		return ans;

	}

	public long nPr(long n, long r) {
		long ans = 1;

		if (r > n)
			return 0;

		for (long i = 1; i <= r; i++) {
			ans = ans * (n - i + 1);
		}
		return ans;
	}

	/* This function calculates (a^b)%c */
	long modulo(long a, long b, long c) {
		long x = 1, y = a; // long long is taken to avoid overflow of
							// intermediate results
		while (b > 0) {
			if (b % 2 == 1) {
				x = (x * y) % c;
			}
			y = (y * y) % c; // squaring the base
			b /= 2;
		}
		return x % c;
	}

	/*
	 * this function calculates (a*b)%c taking into account that a*b might
	 * overflow
	 */
	long mulmod(long a, long b, long c) {
		long x = 0, y = a % c;
		while (b > 0) {
			if (b % 2 == 1) {
				x = (x + y) % c;
			}
			y = (y * 2) % c;
			b /= 2;
		}
		return x % c;
	}

	/*
	 * Fermat's test for checking primality, the more iterations the more is
	 * accuracy
	 */
	boolean Fermat(long p, int iterations) {
		if (p == 1) { // 1 isn't prime
			return false;
		}
		for (int i = 0; i < iterations; i++) {
			// choose a random integer between 1 and p-1 ( inclusive )
			long a = (long) (Math.random() * p);
			// modulo is the function we developed above for modular
			// exponentiation.
			if (modulo(a, p - 1, p) != 1) {
				return false; /* p is definitely composite */
			}
		}
		return true; /* p is probably prime */
	}

	/* Miller-Rabin primality test, iteration signifies the accuracy of the test */
	boolean Miller(long p, int iteration) {
		if (p < 2) {
			return false;
		}
		if (p != 2 && p % 2 == 0) {
			return false;
		}
		long s = p - 1;
		while (s % 2 == 0) {
			s /= 2;
		}
		for (int i = 0; i < iteration; i++) {
			long a = (long) Math.random() * p;
			long temp = s;
			long mod = modulo(a, temp, p);
			while (temp != p - 1 && mod != 1 && mod != p - 1) {
				mod = mulmod(mod, mod, p);
				temp *= 2;
			}
			if (mod != p - 1 && temp % 2 == 0) {
				return false;
			}
		}
		return true;
	}

	// Use 64 bits integers to avoid overflow errors during multiplication.
	long modPow(long a, long x, long p) {
		// calculates a^x mod p in logarithmic time.
		long res = 1;
		while (x > 0) {
			if (x % 2 != 0) {
				res = (res * a) % p;
			}
			a = (a * a) % p;
			x /= 2;
		}
		return res;
	}

	long modInverse(long a, long p) {
		// calculates the modular multiplicative of a mod m.
		// (assuming p is prime).
		return modPow(a, p - 2, p);
	}

	public void sieveOfEratosThenes(boolean[] prime, int N) {

		int i;
		int j;

//		prime = new boolean[N + 1];

		for (i = 2; i < N + 1; i++) {
			prime[i] = true;
		}

		for (i = 2; i < N + 1; i++) {

			j = 2 * i;

			if (!prime[i])
				continue;

			while (j < N + 1) {

				prime[j] = false;

				j += i;

			}

		}

		// for (i = 0; i < N; i++)
		// System.out.println(i + ": " + prime[i]);

	}

	public int reverseInteger(int A) {

		int i;
		int num = 0;

		while (A > 0) {

			i = A % 10;

			num = num * 10 + i;

			A /= 10;

		}

		return num;
	}

	public long gcd(long a, long b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}

	public long lcm(long a, long b) {
		long gcd;
		gcd = gcd(a, b);
		return (a * b) / gcd;
	}

	public void subsetsOfSubset(int N) {
		int i, j;

		for (i = 0; i < (1 << N); i++) {
			for (j = i; j > 0; j = (j - 1) & i) {
				System.out.println(Integer.toBinaryString(j));
			}
		}
	}

	public void iterateOverMBits(int n, int m) {
		// iterate over all the subsets with no more than m elements
		for (int i = 0; i < (1 << n); i = Integer.bitCount(i) < m ? i + 1
				: (i | (i - 1)) + 1) {
		}
	}

	public static void main(String[] args) {
		int i;
		int N;
		int count;
		Utilities util;

		util = new Utilities();

		N = 180;

		System.out.println("Enumerating subsets of: "
				+ Integer.toBinaryString(N));
		System.out.println("Number of set bits: " + Integer.bitCount(N));
		count = 0;
		for (i = N; i > 0; i = (i - 1) & N) {
			System.out.println(Integer.toBinaryString(i));
			count++;
		}

		System.out.println("Total number of subsets: " + count);
		
		boolean prime[];
		prime = new boolean[1000000];
		count = 0;
		
		util.sieveOfEratosThenes(prime, 100000);
		
		for (i = 0; i < 31700; i++) {
			if (prime[i]) {
				System.out.print(i + ",");
				count++;
			}
		}

		System.out.println("\nCount = " + count);
	}
}
