public class SequentialPi {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int numSteps = 10000000; //number of subdivisions. The more divisions the higher the accuracy of approximation

        double step = 1.0 / (double) numSteps; //This value represents how wide each rectangle will be. For example, if numSteps is 10, then each rectangle will have a width of 0.1

        double sum = 0.0;

        for(int i = 0 ; i < numSteps ; i++){
            double x = (i + 0.5) * step ; //calculates midpoint of subdivision
            sum += 4.0 / (1.0 + x * x);
        }

        double pi = step * sum ;

        System.out.println("Value of pi: " + pi);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("Execution time: " + duration + " ns");

    }

}