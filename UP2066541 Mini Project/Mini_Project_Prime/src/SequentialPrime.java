import java.util.ArrayList;
import java.util.List;

public class SequentialPrime {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        int N = 1000000; //Checking numbers up to 10,000,000 to see if prime
        List<Integer> primeList = new ArrayList<>();

        for (int n = 2; n < N; n++) {  // Start from 2 because 1 is not a prime number
            boolean isPrime = true;
            for (int t = 2; t <= Math.sqrt(n); t++) {
                if (n % t == 0) {
                    isPrime = false;
                    break; // No need to check further once a divisor is found
                }
            }
            if (isPrime) {
                primeList.add(n);
            }
        }

        long endTime = System.currentTimeMillis();
        long timeMs = endTime - startTime;
        System.out.println("Number of prime numbers found: "+ primeList.size());
        System.out.println("Calculated in " + timeMs + " milliseconds");
        System.out.println("Performance = " +  ((2L * N * N * N) / (1E6 * timeMs)) + " GFLOPS") ;
    }
}
