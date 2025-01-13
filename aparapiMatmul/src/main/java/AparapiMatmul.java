/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emmam
 */
import com.aparapi.Kernel;
import com.aparapi.ProfileInfo;
import com.aparapi.Range;
import com.aparapi.device.Device ;

import com.aparapi.Kernel;
import com.aparapi.ProfileInfo;
import com.aparapi.Range;
import com.aparapi.device.Device ;

public class AparapiMatmul {

    public static final int N = 8192 ; 

    public static void main(String [] args) {

        float [] a = new float [N * N], b = new float [N * N] ;
        float [] c = new float [N * N] ;

        for (int i = 0 ; i < N ; i++) {
            for(int j = 0 ; j < N ; j++) {
                a [N * i + j] = i + j ;
                b [N * i + j] = i - j ;
            }
        }

        Kernel kernel = new Kernel() {
            public void run() {
                int tx = getGlobalId(0) ;
                int ty = getGlobalId(1) ;

                float sum = 0 ;
                for(int k = 0 ; k < N ; k++) {
                    sum += a [N * ty + k] * b [N * k + tx] ;
                }
                c [N * ty + tx] = sum ;
            }
        } ;

        long startTime = System.currentTimeMillis() ;

        Device device = Device.best() ;
       
        Range range = device.createRange2D(N, N) ;
        kernel.execute(range) ;

        long endTime = System.currentTimeMillis() ;

        System.out.println("Device type = " +
                           device.getType());

        long timeMs = endTime - startTime ;
        System.out.println("Matrix multiplication completed in "
                + timeMs + " milliseconds") ;
        System.out.println("Performance = " +
                ((2L * N * N * N) / (1E6 * timeMs)) + " GFLOPS") ;
    }
}