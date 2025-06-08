package algorithms;

import java.util.*;

public class MyOwn {
    private static int n;
    private static int cnt;
    private static int div = 21;

    static class Cluster {
        Cluster[] subClusters;
        int[] cities;
        double[] center;
        
        Cluster(int[] cities , double[] center) {
            this.cities = cities;
            this.center = center;
        }

        Cluster(Cluster[] subs, double[] center) {
            this.subClusters = subs;
            this.center = center;
        }

        boolean isLeaf() {
            return subClusters == null;
        }
    }

    public static int[] HKMSTHybrid(double[][] xyList) {
        List<Cluster> currentLevel = new ArrayList<>();

        List<List<Integer>> groups = clusterPointIndices(xyList, div);

        for (List<Integer> group : groups) {
            int[] cities = group.stream().mapToInt(i -> i).toArray();
            double[][] coords = Arrays.stream(cities).mapToObj(i -> xyList[i]).toArray(double[][]::new);
            double[] center = computeMedian(coords);
            currentLevel.add(new Cluster(cities, center));
        }

        double[][] centers = currentLevel.stream().map(c -> c.center).toArray(double[][]::new);
        Cluster[] children = new Cluster[currentLevel.size()];
        for (int i = 0; i < currentLevel.size(); i++) {
            children[i] = currentLevel.get(i);
        }
        double[] newCenter = computeMedian(Arrays.stream(children).map(c -> c.center).toArray(double[][]::new));
        Cluster c = new Cluster(children, newCenter);


        List<Integer> finalTour = new ArrayList<>();

        if (groups.size() > 22) {
            int[] tour = ClrsApx.ApxTspTour(centers);
            for (int i = 0; i < tour.length; i++){
                Cluster subc = c.subClusters[tour[i]];
                if (subc.cities.length <= 3) {
                    for (int city : subc.cities) {
                        finalTour.add(city);
                    }
                    break;
                }
                double[][] subCoords = Arrays.stream(subc.cities).mapToObj(k -> xyList[k]).toArray(double[][]::new);
                int[] localTour = HeldKarp.HeldKarpTour(subCoords);
                for (int j : localTour) {
                    finalTour.add(subc.cities[j]);
                }
                
            }
        }
        else {
            int[] tour = HeldKarp.HeldKarpTour(centers);
            for (int i = 0; i < tour.length; i++){
                Cluster subc = c.subClusters[tour[i]];
                if (subc.cities.length <= 3) {
                    for (int city : subc.cities) {
                        finalTour.add(city);
                    }
                    break;
                }
                double[][] subCoords = Arrays.stream(subc.cities).mapToObj(k -> xyList[k]).toArray(double[][]::new);
                int[] localTour = HeldKarp.HeldKarpTour(subCoords);
                for (int j : localTour) {
                    finalTour.add(subc.cities[j]);
                }
                
            }
        }
        return finalTour.stream().mapToInt(i -> i).toArray();
    }

    public static int[] HKDivideConquer(double[][] xyList) {
        n = xyList.length;
        cnt = 0;

        List<Integer> all = new ArrayList<>();
        for (int i = 0; i < xyList.length; i++) all.add(i);
        Cluster root = buildDivideConquer(xyList, all, div);
        List<Integer> finalTour = new ArrayList<>();
        computeTour(xyList, root, finalTour);
        return finalTour.stream().mapToInt(i -> i).toArray();
    }

    private static void computeTour(double[][] xyList, Cluster c, List<Integer> finalTour) {
        if (c.isLeaf()) {
            if (c.cities.length <= 3) {
                for (int city : c.cities) {
                    finalTour.add(city);
                }
                return;
            }
            double[][] subCoords = Arrays.stream(c.cities).mapToObj(i -> xyList[i]).toArray(double[][]::new);
            int[] localTour = HeldKarp.HeldKarpTour(subCoords);
            for (int i : localTour) {
                finalTour.add(c.cities[i]);
            }
        }
        else {
            double[][] centers = Arrays.stream(c.subClusters).map(cl -> cl.center).toArray(double[][]::new);
            System.out.println(Arrays.toString(c.subClusters[0].center) + " & " + Arrays.toString(c.subClusters[c.subClusters.length - 1].center));
            int[] tour = HeldKarp.HeldKarpTour(centers);
            for (int i = 0; i < tour.length; i++) {
                System.out.println(i + " / " + tour.length);
                computeTour(xyList, c.subClusters[tour[i]], finalTour);
            }
        }
    }

    private static Cluster buildDivideConquer(double[][] xyList, List<Integer> cityIndices, int k) {
        List<Cluster> currentLevel = new ArrayList<>();

        List<List<Integer>> groups = clusterPointIndices(cityIndices.stream().map(i -> xyList[i]).toArray(double[][]::new), k);

        for (List<Integer> group : groups) {
            int[] cities = group.stream().mapToInt(i -> cityIndices.get(i)).toArray();
            double[][] coords = Arrays.stream(cities).mapToObj(i -> xyList[i]).toArray(double[][]::new);
            double[] center = computeMedian(coords);
            currentLevel.add(new Cluster(cities, center));
        }

        boolean firstMerge = (currentLevel.size() != 1);

        while (currentLevel.size() > 1 || firstMerge) {
            firstMerge = false;
            List<Cluster> nextLevel = new ArrayList<>();
            double[][] centers = currentLevel.stream().map(c -> c.center).toArray(double[][]::new);
            List<List<Integer>> newGroups = clusterPointIndices(centers, k);

            for (List<Integer> group : newGroups) {
                Cluster[] children = new Cluster[group.size()];
                for (int i = 0; i < group.size(); i++) {
                    children[i] = currentLevel.get(group.get(i));
                }
                double[] newCenter = computeMedian(Arrays.stream(children).map(c -> c.center).toArray(double[][]::new));
                nextLevel.add(new Cluster(children, newCenter));
            }
            currentLevel = nextLevel;
        }

        return currentLevel.get(0);
    }

    private static List<List<Integer>> clusterPointIndices(double[][] xyList, int k) {
        int groupn = xyList.length;
        boolean[] visited = new boolean[groupn];
        List<List<Integer>> groupList = new ArrayList<>();

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (double[] p : xyList) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);
            minY = Math.min(minY, p[1]);
            maxY = Math.max(maxY, p[1]);
        }

        int cols = (int)Math.ceil(Math.sqrt(n / (double)k));
        int rows = cols;
        double cellWidth = (maxX - minX) / cols + 1e-6;
        double cellHeight = (maxY - minY) / rows + 1e-6;

        Map<Integer, List<Integer>> gridMap = new HashMap<>();
        for (int i = 0; i < groupn; i++) {
            int col = (int)((xyList[i][0] - minX) / cellWidth);
            int row = (int)((xyList[i][1] - minY) / cellHeight);
            int key = row * cols + col;
            gridMap.putIfAbsent(key, new ArrayList<>());
            gridMap.get(key).add(i);
        }

        int subcnt = 0;

        for (int key : gridMap.keySet()) {
            List<Integer> cellPoints = gridMap.get(key);

            for (int i = 0; i < cellPoints.size(); i ++) {
                if (subcnt >= groupn) break;
                int start = cellPoints.get(i);
                if (visited[start]) continue;
                
                int[] cluster = nearCities(visited, xyList, start, groupn - subcnt);

                List<Integer> group = new ArrayList<>();
                for (int j = 0; j < cluster.length - 1; j++) {
                    int idx = cluster[j];
                    if (!visited[idx]) visited[idx] = true;
                    group.add(idx);
                }

                visited[start] = true;
                groupList.add(group);
                subcnt += group.size();
            }
        }

        return groupList;
    }


    private static double[] computeMedian(double[][] coords) {
        int midn = coords.length;
        double[] xs = new double[midn];
        double[] ys = new double[midn];
        for (int i = 0; i < midn; i++) {
            xs[i] = coords[i][0];
            ys[i] = coords[i][1];
        }
        Arrays.sort(xs);
        Arrays.sort(ys);
        return new double[]{xs[midn / 2], ys[midn / 2]};
    }

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
            int[] near20 = nearCities(visited, xyList, root, n - cnt);
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

    private static int[] nearCities(boolean[] visited, double[][] xyList, int city, int diff) {
        int k = Math.min(div, diff);
        // System.out.println(k + " " + diff);
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
