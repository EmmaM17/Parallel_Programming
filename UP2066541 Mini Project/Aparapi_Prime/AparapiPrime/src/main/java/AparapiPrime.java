import com.aparapi.*;
import com.aparapi.device.Device;
import java.util.Arrays;

public class AparapiPrime {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int N = 100000; // Checking primes up to 10 million
        boolean[] primeList = new boolean[N + 1];
        Arrays.fill(primeList, true); // Assume all numbers are prime initially
        primeList[0] = primeList[1] = false; // 0 and 1 are not primes

        // Create an Aparapi Kernel for parallel sieve execution
        Kernel kernel = new Kernel() {
            public void run() {
                int p = getGlobalId(0) + 2;  // Start from 2 as 0 and 1 are not prime
                if (primeList[p]) {
                    // Mark multiples of p as non-prime, starting from p^2
                    for (int i = p * p; i <= N; i += p) {
                        primeList[i] = false;
                    }
                }
            }
        };

        // Select the best available device (GPU/CPU)
        Device device = Device.best();
        System.out.println("Using device: " + device.getType());

        // Create range for the kernel (1D range for each potential prime number)
        Range range = device.createRange(N - 1);  // We need to check from 2 to N

        // Execute the kernel
        kernel.execute(range);

        // Count the number of primes
        int primeCount = 0;
        for (int i = 2; i <= N; i++) {
            if (primeList[i]) {
                primeCount++;
            }
        }

        ;

        // Print the results
        long endTime = System.currentTimeMillis();
        long timeMs = endTime - startTime;
        System.out.println("Number of prime numbers found: "+ primeCount);
        System.out.println("Calculated in " + timeMs + " milliseconds");
        System.out.println("Performance = " +  ((2L * N * N * N) / (1E6 * timeMs)) + " GFLOPS") ;
        // Release resources
        kernel.dispose();
    }
}
