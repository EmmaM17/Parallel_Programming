import java.awt.Color ;
import java.awt.image.BufferedImage ;

import javax.imageio.ImageIO;

import java.io.File ;

public class FourPartMandelbrot extends Thread {

    final static int N = 4096 ;
    final static int CUTOFF = 100 ;

    static int [] [] set = new int [N] [N] ;

    public static void main(String [] args) throws Exception {

        // Calculate set

        long startTime = System.currentTimeMillis();

        FourPartMandelbrot thread0 = new FourPartMandelbrot(0) ;
        FourPartMandelbrot thread1 = new FourPartMandelbrot(1) ;
        FourPartMandelbrot thread2 = new FourPartMandelbrot(2) ;
        FourPartMandelbrot thread3 = new FourPartMandelbrot(3) ;

        thread0.start() ;
        thread1.start() ;
        thread2.start() ;
        thread3.start() ;

        thread0.join() ;
        thread1.join() ;
        thread2.join() ;
        thread3.join() ;

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

        ImageIO.write(img, "PNG", new File("Mandelbrot2.png"));

    }

    int me ;

    public FourPartMandelbrot(int me) {
        this.me = me ; // used to determine which thread is being executed
    }

    public void run() {
            int beginX, endX, beginY, endY;

            if (me == 0) { // Top-left quadrant
                beginX = 0;
                endX = N / 2;
                beginY = 0;
                endY = N / 2;
            } else if (me == 1) {
                beginX = N / 2;
                endX = N;
                beginY = 0;
                endY = N / 2;
            } else if (me == 2) {
                beginX = 0;
                endX = N / 2;
                beginY = N / 2;
                endY = N;
            } else { // Bottom-right quadrant
                beginX = N / 2;
                endX = N;
                beginY = N / 2;
                endY = N;
            }

            for (int i = beginY; i < endY; i++) {
                for (int j = beginX; j < endX; j++) {
                    double cr = (4.0 * j - 2 * N) / N;
                    double ci = (4.0 * i - 2 * N) / N;

                    double zr = cr, zi = ci;

                    int k = 0;
                    while (k < CUTOFF && zr * zr + zi * zi < 4.0) {
                        double newr = cr + zr * zr - zi * zi;
                        double newi = ci + 2 * zr * zi;

                        zr = newr;
                        zi = newi;

                        k++;
                    }

                    set[i][j] = k;
                }
            }







    }


}