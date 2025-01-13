import java.util.ArrayList;
import java.util.List;

public class CyclicPrime extends Thread {
    final static int N = 10000000;  // Checking numbers up to 10,000,000 to see if prime
    final static int P = 24;  // Number of threads
    static List<Integer> primeList = new ArrayList<>(); // Global list of prime numbers

    // Thread-local variable to store the primes found by each thread
    private List<Integer> localPrimeList = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        CyclicPrime[] threads = new CyclicPrime[P];
        long startTime = System.currentTimeMillis();  // Start time for total computation

        // Create and start all threads
        for (int me = 0; me < P; me++) {
            threads[me] = new CyclicPrime(me);
            threads[me].start();
        }

        // Wait for all threads to finish
        for (int me = 0; me < P; me++) {
            threads[me].join();
            // After a thread finishes, add its local primes to the global primeList
            synchronized (primeList) {
                primeList.addAll(threads[me].getLocalPrimeList());
            }
        }

        long endTime = System.currentTimeMillis();  // End time for total computation
        System.out.println("Number of prime numbers found: " + primeList.size());
        System.out.println("Total computation time: " + (endTime - startTime) + " milliseconds");
    }

    int me;

    CyclicPrime(int me) {
        this.me = me;
    }

    final static int B = N / P;  // block size (not used directly in cyclic decomposition)

    public void run() {
        // Cyclic Decomposition: Each thread checks numbers spaced by P
        for (int n = me + 2; n <= N; n += P) {  // Start from me + 2 and check every P-th number
            boolean isPrime = true;
            for (int t = 2; t <= Math.sqrt(n); t++) {
                if (n % t == 0) {
                    isPrime = false;
                    break;  // No need to check further once a divisor is found
                }
            }
            if (isPrime) {
                localPrimeList.add(n);  // Add to the thread's local prime list
            }
        }
    }

    // Get the local prime list for this thread
    public List<Integer> getLocalPrimeList() {
        return localPrimeList;
    }
}


