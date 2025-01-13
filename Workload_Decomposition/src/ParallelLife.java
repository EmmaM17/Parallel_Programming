import java.awt.* ;
import javax.swing.* ;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ParallelLife extends Thread {

    final static int N = 1024; // Number of pixels width, height
    final static int CELL_SIZE = 1;
    final static int DELAY = 0;
    final static int P = 8; // Number of threads

    static int[][] state = new int[N][N];
    static int[][] sums = new int[N][N];

    static Display display = new Display();
    static CyclicBarrier barrier = new CyclicBarrier(P); // Synchronization barrier

    public static void main(String args[]) throws Exception {
        long progStartTime = System.currentTimeMillis(); // Start time for the entire program

        // Define initial state of Life board
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                state[i][j] = Math.random() > 0.5 ? 1 : 0;
            }
        }

        // Create threads
        ParallelLife[] threads = new ParallelLife[P]; // Create an array of threads
        for (int me = 0; me < P; me++) {
            threads[me] = new ParallelLife(me);
            threads[me].start();
        }

        // Wait for all threads to finish
        for (int me = 0; me < P; me++) {
            threads[me].join();
        }

        long progEndTime = System.currentTimeMillis(); // End time for the entire program
        System.out.println("Program completed in " + (progEndTime - progStartTime) + " milliseconds");

        // Display initial state
        display.repaint();
        pause();
    }

    static class Display extends JPanel {

        final static int WINDOW_SIZE = N * CELL_SIZE;

        Display() {
            setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));

            JFrame frame = new JFrame("Life");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }

        public void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
            g.setColor(Color.WHITE);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (state[i][j] == 1) {
                        g.fillRect(CELL_SIZE * i, CELL_SIZE * j,
                                CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    static void pause() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    int me;

    public ParallelLife(int me) {
        this.me = me;
    }

    public void run() {

        // BLOCK WISE
        int begin, end;
        int b = N / P;  // block size
        begin = me * b;
        end = begin + b;

        // Main update loop.
        int iter = 0;
        int maxIterations = 500;
        while (iter < maxIterations) {
            long startTime = System.currentTimeMillis(); // Start time for this iteration

            // Calculate neighbour sums.
            for (int i = begin; i < end; i++) {
                for (int j = 0; j < N; j++) {

                    // Find neighbours...
                    int ip = (i + 1) % N;
                    int im = (i - 1 + N) % N;
                    int jp = (j + 1) % N;
                    int jm = (j - 1 + N) % N;

                    sums[i][j] =
                            state[im][jm] + state[im][j] + state[im][jp] +
                                    state[i][jm] + state[i][jp] +
                                    state[ip][jm] + state[ip][j] + state[ip][jp];
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // Update state of board values.
            for (int i = begin; i < end; i++) {
                for (int j = 0; j < N; j++) {
                    switch (sums[i][j]) {
                        case 2:
                            break;
                        case 3:
                            state[i][j] = 1;
                            break;
                        default:
                            state[i][j] = 0;
                            break;
                    }
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            if (me == 0) {
                display.repaint();
                long endTime = System.currentTimeMillis(); // End time for this iteration
                synchronized (ParallelLife.class) {
                    System.out.println("Iteration " + iter + " completed in " + (endTime - startTime) + " milliseconds");
                }
            }

            pause();
            iter++;
        }
    }
}
