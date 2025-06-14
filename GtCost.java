import java.io.*;
import java.util.*;

import algorithms.ClrsApx;

public class GtCost {
    public static double CalCost(String filename, double[][] xyList) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        List<Integer> tour = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("TOUR_SECTION")) break;
        }

        while ((line = br.readLine()) != null && !line.startsWith("EOF") && !line.startsWith("-1")) {
            tour.add(Integer.parseInt(line.trim()) - 1);
        }
        br.close();

        double cost = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            cost += ClrsApx.distance(xyList, tour.get(i), tour.get(i+1));
        }
        cost += ClrsApx.distance(xyList, tour.get(tour.size() - 1), tour.get(0));
        return cost;
    }
}
