package com.bit;

public class Primes {

	public int N;
	public boolean mPrime[];

	public Primes(int N) {
		this.N = N;
		mPrime = new boolean[N + 1];
		sieveOfEratosThenes(mPrime, N);
	}

	public int numOfPrimes() {
		int i;
		int primes;

		primes = 0;

		for (i = 0; i <= N; i++) {
			if (mPrime[i])
				primes++;
		}

//		for (i = 0; i <= N; i++) {
//			System.out.println(i + ": " + mPrime[i]);
//		}

		return primes;
	}

	public void sieveOfEratosThenes(boolean[] prime, int N) {

		int i;
		int j;

		// prime = new boolean[N + 1];

		prime[0] = prime[1] = false;

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

	public static void main(String[] args) {
		Primes prime = new Primes(30000);

		System.out.println(prime.numOfPrimes());

	}

}
