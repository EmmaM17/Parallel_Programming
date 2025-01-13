import java.util.ArrayList;
import java.util.List;

public class SieveOfEratosthenes {
    public static void main(String[] args) {
        int N = 30000000;

        long startTime = System.currentTimeMillis();

        boolean primeList[] = new boolean[N + 1];
        for (int i = 0; i <= N; i++) {
            primeList[i] = true;  // Initially, assume all numbers are prime
        }

        // Implement the Sieve of Eratosthenes algorithm
        for (int p = 2; p * p <= N; p++) {
            // If prime[p] is still true, it means p is a prime
            if (primeList[p] == true) {
                // Mark all multiples of p as non-prime
                for (int i = p * p; i <= N; i += p) {
                    primeList[i] = false;
                }
            }
        }

        // Print all prime numbers and count them
        int primeCount = 0;  // Variable to count prime numbers
        System.out.print("Prime numbers up to " + N + ": ");
        for (int i = 2; i <= N; i++) {
            if (primeList[i] == true) {
                primeCount++;  // Increment the count for each prime number found
            }
        }

        long endTime = System.currentTimeMillis();
        long timeMs = endTime - startTime;
        System.out.println("Number of prime numbers found: "+ primeCount);
        System.out.println("Calculated in " + timeMs + " milliseconds");
        System.out.println("Performance = " +  ((2L * N * N * N) / (1E6 * timeMs)) + " GFLOPS") ;
    }
}
