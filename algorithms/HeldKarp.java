package algorithms;

public class HeldKarp {
    public static int[] HeldKarpTour(double[][] xyList){
        int root = 0;
        int n = xyList.length;
        int mask = 1 << n;

        double[][] g = new double[mask][n];
        int[][] parent = new int[mask][n];

        for (int i = 0; i < mask; i++){
            for (int j = 0; j < n; j++){
                g[i][j] = Double.MAX_VALUE;
            }
        }
        g[1][root] = 0.0;

        for (int S = 1; S < mask; S++){
            if ((S & 1) == 0) continue;      // S should contain root (the most bottom case)
            
            for (int e = 1; e < n; e++){
                if ((S & (1 << e)) == 0) continue;   // our target e
                
                int Si = S ^ (1 << e);               // remove e from S
                
                for (int i = 0; i < n; i++){
                    if ((Si & (1 << i)) == 0) continue; // i != e and i should be in S
                    double cost = g[Si][i] + ClrsApx.distance(xyList, i, e);
                    if (cost < g[S][e]) {
                        g[S][e] = cost;
                        parent[S][e] = i;
                    }
                }
            }

        }

        double minval = Double.MAX_VALUE;
        int last = -1;
        for (int i = 1; i < n; i++){
            double cost = g[mask - 1][i] + ClrsApx.distance(xyList, i, root);
            if (cost < minval) {
                minval = cost;
                last = i;
            }
        }

        int[] tour = new int[n];
        int newmask = mask - 1;
        
        for (int i = n - 1; i > 0; i--){
            tour[i] = last;
            int prev = parent[newmask][last];
            newmask ^= (1 << last);
            last = prev;
        }
        tour[0] = root;

        return tour;
    }
}
