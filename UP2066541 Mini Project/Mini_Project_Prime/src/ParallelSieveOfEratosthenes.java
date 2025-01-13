import java.util.Arrays;

public class ParallelSieveOfEratosthenes extends Thread {
    final static int N = 10;
    final static int P = 8; // Number of threads
    static boolean[] primeList = new boolean[N + 1];
    static int primeCount = 0; // Variable to count prime numbers

    public static void main(String[] args) throws Exception {
        Arrays.fill(primeList, true); // Initially assume all numbers are prime
        primeList[0] = false; // 0 is not prime
        primeList[1] = false; // 1 is not prime

        ParallelSieveOfEratosthenes[] threads = new ParallelSieveOfEratosthenes[P];
        long startTime = System.currentTimeMillis();

        // Start threads
        for (int me = 0; me < P; me++) {
            threads[me] = new ParallelSieveOfEratosthenes(me);
            threads[me].start();
        }

        // Wait for all threads to complete
        for (int me = 0; me < P; me++) {
            threads[me].join();
        }

        // Count the total number of primes
        for (int i = 2; i <= N; i++) {
            if (primeList[i]) {
                primeCount++;
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Number of prime numbers found: " + primeCount);
        System.out.println("Calculated in " + (endTime - startTime) + " milliseconds");
    }

    int me;

    ParallelSieveOfEratosthenes(int me) {
        this.me = me;
    }

    final static int B = N / P;

    public void run() {
        int start = me * B + 2; // Start of the range for this thread
        int end = Math.min(start + B, N + 1); // End of the range for this thread

        for (int p = 2; p * p <= N; p++) {
            if (primeList[p]) {
                for (int i = Math.max(p * p, (start + p - 1) / p * p); i < end; i += p) {
                    primeList[i] = false; // Mark multiples of p as non-prime
                }
            }
        }
    }
}
