/*
import mpi.*;
import java.util.ArrayList;
import java.util.List;

public class MPJPrime {
    final static int N = 10000000; // Checking numbers up to 10 million to see if prime

    static List<Integer> primeList = new ArrayList<>();

    // Tag values for communication
    final static int TAG_HELLO = 0;
    final static int TAG_RESULT = 1;
    final static int TAG_TASK = 2;
    final static int TAG_GOODBYE = 3;

    public static void main(String[] args) throws Exception {

        // Initialize MPI
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();  // Get rank of the current process (ID)
        int P = MPI.COMM_WORLD.Size();  // Get total number of processes

        // Ensure there is more than 1 process (1 master + 1 worker)
        if (P < 2) {
            System.out.println("This program requires at least 2 processes.");
            MPI.Finalize();
            return;
        }

        if (me == 0) { // Master node (rank 0)
            long startTime = System.currentTimeMillis();

            // Distribute work to worker nodes
            int numWorkers = P - 1;  // Subtract 1 for master node
            int chunkSize = N / numWorkers;

            for (int worker = 1; worker <= numWorkers; worker++) {
                int start = (worker - 1) * chunkSize + 2;
                int end = worker * chunkSize + 1;

                // Send task to each worker
                int[] taskRange = new int[]{start, end};
                MPI.COMM_WORLD.Send(taskRange, 0, 2, MPI.INT, worker, TAG_TASK);
            }

            // Collect results from all workers
            for (int worker = 1; worker <= numWorkers; worker++) {
                // Prepare buffer to receive the result from each worker
                int[] buffer = new int[chunkSize]; // Buffer to hold primes from each worker
                Status status = MPI.COMM_WORLD.Recv(buffer, 0, buffer.length, MPI.INT, worker, TAG_RESULT);

                // Add received primes to the prime list
                for (int i = 0; i < buffer.length; i++) {
                    if (buffer[i] != 0) {
                        primeList.add(buffer[i]); // Store primes received from workers
                    }
                }

            }

            // Output result
            long endTime = System.currentTimeMillis();
            System.out.println("Number of prime numbers found: " + primeList.size());
            System.out.println("Calculated in " + (endTime - startTime) + " milliseconds");

        } else { // Worker nodes (rank > 0)
            // Prepare buffer to receive task (start and end range)
            int[] taskRange = new int[2];
            MPI.COMM_WORLD.Recv(taskRange, 0, 2, MPI.INT, 0, TAG_TASK);
            int begin = taskRange[0];
            int end = taskRange[1];

            List<Integer> workerPrimeList = new ArrayList<>();

            // Perform primality testing for the assigned range
            for (int n = begin; n <= end; n++) {
                boolean isPrime = true;
                for (int t = 2; t <= Math.sqrt(n); t++) {
                    if (n % t == 0) {
                        isPrime = false;
                        break; // No need to check further once a divisor is found
                    }
                }
                if (isPrime) {
                    workerPrimeList.add(n); // Add prime number found to the list
                }
            }

            // Prepare buffer to send primes to master node
            int[] resultBuffer = new int[workerPrimeList.size()];
            for (int i = 0; i < workerPrimeList.size(); i++) {
                resultBuffer[i] = workerPrimeList.get(i); // Fill buffer with prime numbers
            }

            // Send prime numbers back to master node
            MPI.COMM_WORLD.Send(resultBuffer, 0, resultBuffer.length, MPI.INT, 0, TAG_RESULT);
        }

        // Finalize MPI
        MPI.Finalize();
    }
}
*/
