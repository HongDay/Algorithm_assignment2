package algorithms;

import java.util.*;

public class MyOwn {
    private static int n;
    private static int cnt;
    private static int div = 21;

    public static int[] HKGreedySubTour(double[][] xyList){
        int root = 0;
        double minSum = Double.MAX_VALUE;
        for (int i = 0; i < xyList.length; i++) {
            double sum = Math.sqrt(xyList[i][0] * xyList[i][0] + xyList[i][1] * xyList[i][1]);
            if (sum < minSum) {
                minSum = sum;
                root = i;
            }
        }
        n = xyList.length;

        cnt = 1;
        boolean[] visited = new boolean[n];
        for (int i = 1; i < n; i++) visited[i] = false;
        List<Integer> tour = new ArrayList<>();
        tour.add(root);

        while (cnt < n) {
            int k = Math.min(20, n - cnt);
            int[] near20 = nearCities(visited, xyList, root);
            double[][] subxyList = new double[k + 1][2];
            for (int i = 0; i < k + 1; i++) {
                subxyList[i][0] = xyList[near20[i]][0];
                subxyList[i][1] = xyList[near20[i]][1];
            }
            int[] subTour = HeldKarp.HeldKarpTour(subxyList);

            for (int i = 1; i < subTour.length; i++) {
                tour.add(near20[subTour[i]]);
            }

            root = near20[subTour[subTour.length - 1]];
            cnt += 20;
        }

        return tour.stream().mapToInt(i -> i).toArray();
    }

    private static int[] nearCities(boolean[] visited, double[][] xyList, int city) {
        int k = Math.min(div, n - cnt);
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, (p1, p2) -> Double.compare(ClrsApx.distance(xyList, city, p2), ClrsApx.distance(xyList, city, p1)));
    
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) continue;

            if (maxHeap.size() < k) maxHeap.offer(i);
            else if (ClrsApx.distance(xyList, city, i) < ClrsApx.distance(xyList, city, maxHeap.peek())) {
                maxHeap.poll();
                maxHeap.offer(i);
            }
        }

        int[] result = new int[k + 1];
        result[0] = city;
        int idx = 1;
        while (!maxHeap.isEmpty()) {
            int p = maxHeap.poll();
            result[idx++] = p;
            visited[p] = true;
        }
        

        return result;
    }
}
