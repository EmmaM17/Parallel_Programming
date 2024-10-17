public class QuadCorePi extends Thread {

    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();

        ParallelPi thread1 = new ParallelPi();
        thread1.begin = 0 ; //processes the first quarter
        thread1.end = numSteps / 4 ;

        ParallelPi thread2 = new ParallelPi();
        thread2.begin = numSteps / 4 ; //processes 2nd quarter
        thread2.end = numSteps / 2;

        ParallelPi thread3 = new ParallelPi();
        thread3.begin = numSteps / 2 ; //processes 3rd quarter
        thread3.end = (numSteps / 4) * 3;

        ParallelPi thread4 = new ParallelPi();
        thread4.begin = (numSteps / 4) * 3; //processes final quarter
        thread4.end = numSteps ;

        thread1.start(); // invokes the run() method in each thread concurrently. This method contains the computation logic for approximating pi
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join(); //  This ensures that the main thread waits for both threads to complete their execution before proceeding, which is essential for correctness in parallel programming.
        thread2.join();
        thread3.join();
        thread4.join();

        double pi = step * (thread1.sum + thread2.sum + thread3.sum + thread4.sum);

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
