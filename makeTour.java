import java.io.*;

public class makeTour {
    public static void TourFile(int[] tour, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write("NAME: " + filename + "\n");
        writer.write("TYPE: TOUR\n");
        writer.write("DIMENSION: " + tour.length + "\n");
        writer.write("TOUR_SECTION\n");
        for (int i = 0; i < tour.length; i++) {
            int city = tour[i] + 1;
            writer.write(city + "\n");
        }
        writer.write("-1\n");
        writer.flush();
        writer.close();
    }
}
