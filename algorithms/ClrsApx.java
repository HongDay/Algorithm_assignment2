package algorithms;

import java.util.*;

public class ClrsApx {

    private static boolean[] visited;

    public static int[] ApxTspTour(double[][] xyList){
        int root = 0;

        int[] parent = MSTPrim(xyList, root);

        int n = xyList.length;

        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) adjList.add(new ArrayList<>());
        for (int i = 0; i < n; i++) {
            if(!(parent[i] == -1)) adjList.get(parent[i]).add(i);
        }
        
        visited = new boolean[n];
        int[] tour = new int[n + 1];
        int[] idx = {0};
        PreOrder(root, idx, tour, adjList);

        return tour;
    }

    private static void PreOrder(int u, int[] idx, int[] tour, List<List<Integer>> adjList) {
        visited[u] = true;
        tour[idx[0]++] = u;
        for (int v : adjList.get(u)) {
            if (!visited[v]) {
                PreOrder(v, idx, tour, adjList);
            }
        }
    }
    
    private static int[] MSTPrim(double[][] xyList, int root){
        int n = xyList.length;           // 첫번째 차원의 길이
        double[] key = new double[n];    // 각 정점에 대해 다른 정점과 연결되는 간선들 중 최소 가중치값
        int[] parent = new int[n];       // 각 정점의 부모 정점
        boolean[] inQ = new boolean[n];  // Q : 아직 방문하지 않은 정점집합 (G.V로 초기화)
        TreeSet<Integer> Q = new TreeSet<>(Comparator.comparingDouble(i -> key[i]));
        // java의 pq는 .remove(v) 연산이 O(n) 이므로, v.key 갱신 후 decrease-key 연산이 O(n + logV) 시간복잡도임

        for (int i = 0; i < n; i++) {
            key[i] = Double.MAX_VALUE;
            parent[i] = -1;
            inQ[i] = true;
            Q.add(i);
        }
        
        Q.remove(root);
        key[root] = 0;
        Q.add(root);

        while (!Q.isEmpty()){
            int u = Q.pollFirst();
            inQ[u] = false;

            for (int v = 0; v < n; v++){
                double w = distance(xyList, u, v);
                if (inQ[v] && w < key[v]){
                    Q.remove(v);
                    parent[v] = u;
                    key[v] = w;
                    Q.add(v);
                }
            }
        }

        return parent;
    }

    public static double distance(double[][] xyList, int p1, int p2){
        double dx = xyList[p1][0] - xyList[p2][0];
        double dy = xyList[p1][1] - xyList[p2][1];
        return Math.sqrt(dx * dx + dy * dy);
    }
}
