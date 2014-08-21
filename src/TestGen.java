
public class TestGen {
	
	public static void main(String [] args) {
		int i, j;
		int N, Q;
		int A[];
		
		N = 15;
		Q = 20;
		
		A = new int[N];
		
		System.out.println(N + " " + Q);
		
		for (i = 0; i < N; i++) {
			A[i] = (int) (5 * Math.random());
			System.out.print(A[i] + " ");
		}
		
		System.out.println();
		
		
	
	}

}
