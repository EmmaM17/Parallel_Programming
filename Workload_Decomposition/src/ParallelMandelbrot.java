import java.awt.Color ;
import java.awt.image.BufferedImage ;

import javax.imageio.ImageIO;

import java.io.File ;

public class ParallelMandelbrot extends Thread {

    final static int N = 4096 ; //Number of pixels width, height
    final static int CUTOFF = 100 ;
    final static int P = 8; // Generalise program to run on a number of threads defined by constant P

    static int [] [] set = new int [N] [N] ;

    public static void main(String [] args) throws Exception {

        // Calculate set

        long startTime = System.currentTimeMillis();

        ParallelMandelbrot [] threads = new ParallelMandelbrot [P] ; //Create an array of threads and complete them in loops
        for(int me = 0 ; me < P ; me++) {
            threads [me] = new ParallelMandelbrot(me) ;
            threads [me].start() ;
        }

        for(int me = 0 ; me < P ; me++) {
            threads [me].join() ;
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Calculation completed in " +
                (endTime - startTime) + " milliseconds");

        // Plot image

        BufferedImage img = new BufferedImage(N, N,
                BufferedImage.TYPE_INT_ARGB) ;

        // Draw pixels

        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N ; j++) {

                int k = set [i] [j] ;

                float level ;
                if(k < CUTOFF) {
                    level = (float) k / CUTOFF ;
                }
                else {
                    level = 0 ;
                }
                Color c = new Color(0, level, 0) ;  // Green
                img.setRGB(i, j, c.getRGB()) ;
            }
        }


        // Print file

        ImageIO.write(img, "PNG", new File("Mandelbrot.png"));
    }

    int me ;

    public ParallelMandelbrot(int me) {
        this.me = me ;
    }

    public void run() {

        int begin, end ;
        int b = N/P ;
        int remainder = N % P;

        if (N % P == 0) {
            begin = me * b;
            end = begin + b;
        } else {

            if (me < remainder) { //if thread is less than remainder, handle one extra pixel
                begin = me * (b + 1);
                end = begin + (b + 1);
            } else { //when the number of threads is equal to the remainder or more don't include the extra pixel
                begin = me * b + remainder;
                end = begin + b;
            }
        }


        System.out.println(b);



        for(int i = me ; i < N ; i+=P) { //int i = begin ; i < end ; i++
            for(int j = 0 ; j < N ; j++) {

                double cr = (4.0 * i - 2 * N) / N ;
                double ci = (4.0 * j - 2 * N) / N ;

                double zr = cr, zi = ci ;

                int k = 0 ;
                while (k < CUTOFF && zr * zr + zi * zi < 4.0) {

                    // z = c + z * z

                    double newr = cr + zr * zr - zi * zi ;
                    double newi = ci + 2 * zr * zi ;

                    zr = newr ;
                    zi = newi ;

                    k++ ;
                }

                set [i] [j] = k ;
            }
        }
    }

}
