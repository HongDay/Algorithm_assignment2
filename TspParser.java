import java.io.*;
import java.util.*;

public class TspParser {
    public static double[][] parseTSPFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<double[]> coords = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.startsWith("NODE_COORD_SECTION")) break;
        }

        while ((line = br.readLine()) != null && !line.startsWith("EOF") && !line.startsWith("-1")) {
            String[] parts = line.trim().split("\\s+");
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            coords.add(new double[]{x, y});
        }
        br.close();

        int n = coords.size();
        double[][] distMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double dx = coords.get(i)[0] - coords.get(j)[0];
                double dy = coords.get(i)[1] - coords.get(j)[1];
                distMatrix[i][j] = Math.sqrt(dx * dx + dy * dy);
            }
        }
        return distMatrix;
    }

    public static double[][] parseTSPFile2(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<double[]> coords = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.startsWith("NODE_COORD_SECTION")) break;
        }

        while ((line = br.readLine()) != null && !line.startsWith("EOF") && !line.startsWith("-1")) {
            String[] parts = line.trim().split("\\s+");
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            coords.add(new double[]{x, y});
        }
        br.close();

        int n = coords.size();
        double[][] xyList = new double[n][2];
        for (int i = 0; i < n; i++) {
            xyList[i][0] = coords.get(i)[0];
            xyList[i][1] = coords.get(i)[1];
        }
        return xyList;
    }
}
