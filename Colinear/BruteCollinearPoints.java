import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private Point[] points;
    private Integer n = null;
    private LineSegment[] segments;
    
    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; ++j) {
                if (points[j] == null || points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        // should create a copy
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
    }
    public int numberOfSegments() { // the number of line segments
        if (n == null) findLineSegments();
        return n;
    }
    public LineSegment[] segments() {  // the line segments
        // should return a copy
        if (segments != null) { return Arrays.copyOf(segments, segments.length); }
        findLineSegments();
        segments = new LineSegment[n];
        findLineSegments();
        return Arrays.copyOf(segments, segments.length);
    }
    private void findLineSegments() {
        n = 0;
        for (int i = 0; i < points.length - 3; ++i) {
            for (int j = i + 1; j < points.length - 2; ++j) {
                for (int k = j + 1; k < points.length - 1; ++k) {
                    for (int l = k + 1; l < points.length; ++l) {
                        if (points[i].slopeTo(points[j]) == 
                            points[i].slopeTo(points[k]) &&
                            points[i].slopeTo(points[j])==
                            points[i].slopeTo(points[l])) {
                            if (segments != null) {
                                segments[n] = new LineSegment(points[i], points[l]); 
                            }
                            n += 1;
                        }
                    }
                }
            }
        }
    }
    
    private static final int DELAY = 50;
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();
        Point[] points = new Point[n];
        
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setScale(-10000, +38000);
        
        int p = 0;
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();
            StdOut.println(x + ", " + y);
            points[p++] = new Point(x, y);
            points[p-1].draw();
            StdDraw.show();
//            StdDraw.pause(DELAY);
        }
        
        
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
        LineSegment[] segs = bc.segments();
        for (int i = 0; i < segs.length; ++i) {
            StdOut.println(segs[i]);
            segs[i].draw();
            StdDraw.show();
            StdDraw.pause(DELAY);
        }
        
    }
}