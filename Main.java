import algorithms.ClrsApx;
import algorithms.Greedy;
import java.io.*;

class Main{
    public static void main(String[] args) throws IOException {
        String filename = "ulysses22.tsp";
        String optfilename = "dataset/ulysses22.opt.tour";
        // double[][] distMatrix = TspParser.parseTSPFile(filename);
        double[][] xyList = TspParser.parseTSPFile2("dataset/" + filename);

        System.gc();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        int[] tour = Greedy.GreedyInsertion(xyList);
        String algname = "GreedyInsert";

        long endTime = System.nanoTime();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        double elapsedTime = (endTime - startTime) / 1e6; // ms
        double usedMemory = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0); // MB

        makeTour.TourFile(tour, "output/" + filename + "_" + algname + ".tour");
        makePng.drawTourToPNG(xyList, tour, "output/" + filename + "_" + algname + ".png");

        double cost = 0.0;
        for (int i = 0; i < tour.length - 1; i++) {
            cost += ClrsApx.distance(xyList, tour[i], tour[i+1]);
        }
        cost += ClrsApx.distance(xyList, tour[tour.length - 1], tour[0]);

        double optCost = GtCost.CalCost(optfilename, xyList);
        // double optCost = 5757084;

        double accuracy = (optCost / cost) * 100.0;
        double apxRatio = cost / optCost;
        // double gap = (cost - optCost) / optCost * 100.0;

        System.out.println("\n[ " + filename + " - " + algname + " ]");
        System.out.printf("input size = %d\n", xyList.length);
        System.out.printf("exec time : %.2f ms\n", elapsedTime);
        System.out.printf("mem usage : %.2f MB\n", usedMemory);
        System.out.printf("cost : %.2f\n", cost);
        System.out.printf("opt cost : %.2f\n", optCost);
        System.out.printf("accuracy : %.2f%%\n", accuracy);
        System.out.printf("apx ratio : %.2f\n\n", apxRatio);
        // System.out.printf("gap : %.2f%%\n", gap);
    }
}