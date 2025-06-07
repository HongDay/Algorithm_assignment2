import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class makePng {

    public static void drawTourToPNG(double[][] xyList, int[] tour, String filename) throws IOException {
        int width = 800;
        int height = 800;
        int margin = 50;

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (double[] coord : xyList) {
            minX = Math.min(minX, coord[0]);
            maxX = Math.max(maxX, coord[0]);
            minY = Math.min(minY, coord[1]);
            maxY = Math.max(maxY, coord[1]);
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2));

        for (int i = 0; i < tour.length - 1; i++) {
            Point p1 = scale(xyList[tour[i]], minX, maxX, minY, maxY, width, height, margin);
            Point p2 = scale(xyList[tour[i + 1]], minX, maxX, minY, maxY, width, height, margin);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        Point last = scale(xyList[tour[tour.length - 1]], minX, maxX, minY, maxY, width, height, margin);
        Point first = scale(xyList[tour[0]], minX, maxX, minY, maxY, width, height, margin);
        g.drawLine(last.x, last.y, first.x, first.y);

        g.setColor(Color.RED);
        for (int i : tour) {
            Point p = scale(xyList[i], minX, maxX, minY, maxY, width, height, margin);
            g.fillOval(p.x - 3, p.y - 3, 6, 6);
        }

        g.dispose();

        ImageIO.write(image, "png", new File(filename));
    }

    private static Point scale(double[] coord, double minX, double maxX, double minY, double maxY,
                               int width, int height, int margin) {
        int x = (int) ((coord[0] - minX) / (maxX - minX) * (width - 2 * margin) + margin);
        int y = (int) ((coord[1] - minY) / (maxY - minY) * (height - 2 * margin) + margin);
        return new Point(x, height - y);
    }
}
