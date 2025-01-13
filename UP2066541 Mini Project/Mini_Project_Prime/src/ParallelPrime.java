import java.util.ArrayList;
import java.util.List;

public class ParallelPrime extends Thread {
    final static int N = 10; // Checking numbers up to 10,000,000 to see if prime
    final static int P = 8; // Number of threads
    static List<Integer> primeList = new ArrayList<>();

    // Thread-local variable to store the primes found by each thread
    private List<Integer> localPrimeList = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        ParallelPrime[] threads = new ParallelPrime[P];
        long startTime = System.currentTimeMillis();  // Start time for total computation

        // Create and start all threads
        for (int me = 0; me < P; me++) {
            threads[me] = new ParallelPrime(me);
            threads[me].start();
        }

        // Wait for all threads to finish
        for (int me = 0; me < P; me++) {
            threads[me].join();
            // After a thread finishes, add its local primes to the global primeList
            primeList.addAll(threads[me].getLocalPrimeList());
        }

        long endTime = System.currentTimeMillis();  // End time for total computation
        System.out.println("Number of prime numbers found: " + primeList.size());
        System.out.println("Total computation time: " + (endTime - startTime) + " milliseconds");
    }

    int me;

    ParallelPrime(int me) {
        this.me = me;
    }

    final static int B = N / P;  // block size

    public void run() {
        int begin = (me * B) + 2;  // Starting number for this thread
        int end = (me == P - 1) ? N : (begin + B); // Make sure last thread covers to N

        for (int n = begin; n <= end; n++) {  // Start from 2 because 1 is not a prime number
            boolean isPrime = true;
            for (int t = 2; t <= Math.sqrt(n); t++) {
                if (n % t == 0) {
                    isPrime = false;
                    break; // No need to check further once a divisor is found
                }
            }
            if (isPrime) {
                localPrimeList.add(n); // Add to the thread's local prime list
            }
        }
    }

    // Get the local prime list for this thread
    public List<Integer> getLocalPrimeList() {
        return localPrimeList;
    }
}
