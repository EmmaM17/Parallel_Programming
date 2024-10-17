public class MultiThreading {

        public static void main(String[] args) throws Exception {
            long startTime = System.nanoTime();

            int numThreads = 16; // Number of threads to utilize hyper-threading
            ParallelPi[] threads = new ParallelPi[numThreads];

            for (int i = 0; i < threads.length; i++) {
                threads[i] = new ParallelPi();
                threads[i].begin = i * (numSteps / numThreads);
                threads[i].end = (i + 1) * (numSteps / numThreads);
            }

            // Start all threads
            for (ParallelPi thread : threads) {
                thread.start();
            }

            // Wait for all threads to finish
            for (ParallelPi thread : threads) {
                thread.join();
            }

            // Combine results
            double pi = step * 0; // Initialize to 0
            for (ParallelPi thread : threads) {
                pi += thread.sum;
            }

            System.out.println("Value of pi: " + pi);

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.println("Execution time: " + duration + " ns");
        }

        static int numSteps = 10000000; // Total number of steps
        static double step = 1.0 / (double) numSteps; // Step size
        double sum; // Sum for each thread
        int begin, end; // Range for each thread

        public void run() {
            sum = 0.0;
            for (int i = begin; i < end; i++) {
                double x = (i + 0.5) * step;
                sum += 4.0 / (1.0 + x * x);
            }
        }

}
