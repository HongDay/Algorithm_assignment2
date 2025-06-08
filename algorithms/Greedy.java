package algorithms;

import java.util.ArrayList;
import java.util.List;

public class Greedy {
    public static int[] GreedyInsertion(double[][] xyList){
        int n = xyList.length;
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[n];

        tour.add(0);
        tour.add(0);
        visited[0] = true;
        
        int cnt = 1;
        while (cnt < n) {
            int best = -1;
            int pos = -1;
            double min = Double.MAX_VALUE;

            for (int city = 0; city < n; city++) {
                if (visited[city]) continue;
                for (int j = 0; j < tour.size() - 1; j++) {
                    int a = tour.get(j);
                    int b = tour.get(j + 1);
                    double cost = ClrsApx.distance(xyList, a, city) + ClrsApx.distance(xyList, city, b) - ClrsApx.distance(xyList, a, b);

                    if (cost < min) {
                        min = cost;
                        best = city;
                        pos = j + 1;
                    }
                }
            }
            
            tour.add(pos, best);
            visited[best] = true;
            cnt++;
        }

        return tour.stream().mapToInt(i -> i).toArray();
    }
}
