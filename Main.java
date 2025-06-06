import algorithms.ClrsApx;
import java.io.*;

class Main{
    public static void main(String[] args) throws IOException {
        double[][] distMatrix = TspParser.parseTSPFile("dataset/a280.tsp");

        int[] tour = ClrsApx.ApxTspTour(distMatrix);
        
    }
}