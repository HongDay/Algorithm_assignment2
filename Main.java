import algorithms.ClrsApx;
import java.io.*;

class Main{
    public static void main(String[] args) throws IOException {
        String filename = "dataset/a280.tsp";
        String optfilename = "dataset/a280.opt.tour";
        double[][] distMatrix = TspParser.parseTSPFile(filename);

        System.gc();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        int[] tour = ClrsApx.ApxTspTour(distMatrix);

        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        double elapsedTime = (endTime - startTime) / 1e6; // ms
        double usedMemory = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0); // MB

        double cost = 0.0;
        for (int i = 0; i < tour.length - 1; i++) {
            cost += distMatrix[tour[i]][tour[i+1]];
        }
        cost += distMatrix[tour[tour.length - 1]][tour[0]];

        double optCost = GtCost.CalCost(optfilename, distMatrix);

        double accuracy = (optCost / cost) * 100.0;
        double apxRatio = cost / optCost;

        System.out.println("\n[ " + filename + " - ApxTspTour" + " ]");
        System.out.printf("input size = %d\n", distMatrix.length);
        System.out.printf("exec time : %.2f ms\n", elapsedTime);
        System.out.printf("mem usage : %.2f MB\n", usedMemory);
        System.out.printf("cost : %.2f\n", cost);
        System.out.printf("opt cost : %.2f\n", optCost);
        System.out.printf("accuracy : %.2f%%\n", accuracy);
        System.out.printf("apx ratio : %.2f\n\n", apxRatio);
    }
}