/*
import mpi.*;
import java.util.Arrays;

public class MPJSieveOfEratosthenes {
    public static void main(String[] args) throws Exception {
        // Initialize MPI
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();  // Get the rank of the current process (ID)
        int P = MPI.COMM_WORLD.Size();   // Get the total number of processes

        // Define the range of numbers to check for primality
        int N = 10000000;  // Up to 10 million

        // Ensure there is more than 1 process (1 master + at least 1 worker)
        if (P < 2) {
            System.out.println("This program requires at least 2 processes.");
            MPI.Finalize();
            return;
        }

        // Initialize the prime list array for the master node
        boolean[] primeList = new boolean[N + 1];
        if (me == 0) {
            Arrays.fill(primeList, true);  // Initially, assume all numbers are prime
            primeList[0] = primeList[1] = false; // 0 and 1 are not primes
        }

        // Divide the work among the workers
        int chunkSize = N / (P - 1);  // Divide the range among worker processes
        int start = (me - 1) * chunkSize + 2;

        // Rename the `end` variable in the worker block to avoid conflict
        int localEnd;
        if (me == P - 1) {
            localEnd = N;  // Last worker gets the remaining portion
        } else {
            localEnd = start + chunkSize - 1;
        }

        // Send the chunk ranges to workers
        if (me == 0) {
            // Master process sends the ranges to each worker
            for (int i = 1; i < P; i++) {
                int[] taskRange = new int[]{(i - 1) * chunkSize + 2, (i == P - 1) ? N : i * chunkSize + 1};
                MPI.COMM_WORLD.Send(taskRange, 0, 2, MPI.INT, i, 2);
            }

            long startTime = System.currentTimeMillis();

            // Collect results from all workers
            for (int i = 1; i < P; i++) {
                int[] buffer = new int[chunkSize]; // Buffer to hold primes from each worker
                Status status = MPI.COMM_WORLD.Recv(buffer, 0, chunkSize, MPI.INT, i, 1);

                // Mark the prime numbers in the main prime list
                for (int j = 0; j < chunkSize; j++) {
                    if (buffer[j] != 0) {
                        primeList[buffer[j]] = true;  // Mark as prime
                    }
                }
            }

            // Apply the Sieve of Eratosthenes algorithm in the master process
            for (int p = 2; p * p <= N; p++) {
                if (primeList[p]) {
                    for (int i = p * p; i <= N; i += p) {
                        primeList[i] = false;
                    }
                }
            }

            // Count the number of primes and print the result
            int primeCount = 0;
            for (int i = 2; i <= N; i++) {
                if (primeList[i]) {
                    primeCount++;
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Number of prime numbers found: " + primeCount);
            System.out.println("Calculated in " + (endTime - startTime) + " milliseconds");

        } else {
            // Worker process (rank > 0)
            int[] taskRange = new int[2];
            MPI.COMM_WORLD.Recv(taskRange, 0, 2, MPI.INT, 0, 2);
            int begin = taskRange[0];
            localEnd = taskRange[1];  // Now use the renamed variable

            // Perform the sieve for this worker's chunk
            boolean[] localPrimeList = new boolean[N + 1];
            Arrays.fill(localPrimeList, true);  // Initially, assume all numbers in the range are prime
            localPrimeList[0] = localPrimeList[1] = false;  // 0 and 1 are not primes

            // Apply the Sieve of Eratosthenes for the range [begin, localEnd]
            for (int p = 2; p * p <= N; p++) {
                if (localPrimeList[p]) {
                    for (int i = Math.max(p * p, begin); i <= localEnd; i += p) {
                        localPrimeList[i] = false;
                    }
                }
            }

            // Collect the prime numbers found by this worker
            int[] resultBuffer = new int[chunkSize];
            int index = 0;
            for (int i = begin; i <= localEnd; i++) {
                if (localPrimeList[i]) {
                    resultBuffer[index++] = i;
                }
            }

            // Send the prime numbers found by the worker back to the master
            MPI.COMM_WORLD.Send(resultBuffer, 0, index, MPI.INT, 0, 1);
        }

        // Finalize MPI
        MPI.Finalize();
    }
}
*/
