public class ParallelPi extends Thread {

    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();

        ParallelPi thread1 = new ParallelPi();
        thread1.begin = 0 ; //processes the first half (from 0 to numSteps/2).
        thread1.end = numSteps / 2 ;

        ParallelPi thread2 = new ParallelPi();
        thread2.begin = numSteps / 2 ; //processes 2nd half
        thread2.end = numSteps ;

        thread1.start(); // invokes the run() method in each thread concurrently. This method contains the computation logic for approximating pi
        thread2.start();

        thread1.join(); //  This ensures that the main thread waits for both threads to complete their execution before proceeding, which is essential for correctness in parallel programming.
        thread2.join();

        double pi = step * (thread1.sum + thread2.sum) ;

        System.out.println("Value of pi: " + pi);
        long endTime = System.nanoTime();
        long duration = endTime - startTime; // duration in nanoseconds

        System.out.println("Execution time: " + duration + " ns");
    }

    static int numSteps = 10000000; // number of steps with be divided between 2 threads

    static double step = 1.0 / (double) numSteps;

    double sum ;
    int begin, end ;

    public void run() {

        sum = 0.0 ;

        for(int i = begin ; i < end ; i++){
            double x = (i + 0.5) * step ;
            sum += 4.0 / (1.0 + x * x);
        }
    }
}
