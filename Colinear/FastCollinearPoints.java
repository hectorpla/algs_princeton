import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private Point[] points;
    private Point[] endPoints;
    private double[] existSlopes;
    private Integer n = null;
    private Integer nEnd = null;
    private LineSegment[] segments;
    
    public FastCollinearPoints(Point[] points) {
        // copy paste
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; ++j) {
                if (points[j] == null || points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
    }
    public int numberOfSegments() {
        if (n != null) return n;
        findCollinear();
        return n;
    }
    public LineSegment[] segments() {
        findCollinear(); // have to enumerate twice, bad?
        findCollinear();
        return segments;
    }
    private void findCollinear() {
        if (segments != null) return;
        if (segments == null && n != null) { 
            segments = new LineSegment[n]; 
        }
        if (endPoints == null) {
            endPoints = new Point[points.length];
            existSlopes = new double[points.length]; // qradratic
        }
        n = 0; // reset, bad
        nEnd = 0;
        for (int i = 0; i < points.length - 3; ++i) {
            Point[] temp = Arrays.copyOfRange(points, i + 1, points.length);
            Arrays.sort(temp, points[i].slopeOrder());
            double prevSlope = points[i].slopeTo(temp[0]);
            int count = 1;
            
//            StdOut.println(points[i] + ":");
            for (int j = 1; j < temp.length; ++j) {
                double slope = points[i].slopeTo(temp[j]);
//                StdOut.print(slope + ", ");
                if (slope == prevSlope) {
                    count++;
                }
                else {
                    if (count > 2 && checkRange(temp, j - count, j, prevSlope)) {
                        Arrays.sort(temp, j - count, j); // may cause problem
                        endPoints[nEnd] = temp[j - 1];
                        existSlopes[nEnd] = prevSlope;
                        nEnd++;
                        if (segments != null) {
                            segments[n] = new LineSegment(points[i], temp[j - 1]);
                        }
//                        StdOut.println("\n+++++ added" + points[i] + ", " + points[j - 1]);
                        n++;
                    }
                    count = 1;
                    prevSlope = slope;
                }
            }
            // too verbose: almost same as the above code
            int len = temp.length;
            double slope = points[i].slopeTo(temp[len - 1]);
            if (count > 2 && checkRange(temp, len - count, len, slope)) {
                Arrays.sort(temp, len - count, len);
                endPoints[nEnd] = temp[len - 1];
                existSlopes[nEnd] = slope;
                nEnd++;
                if (segments != null) {
                    segments[n] = new LineSegment(points[i], temp[len - 1]);
                }
                n++;
            }
//            StdOut.println("");
        }
//        StdOut.println("\n--------------------");
    }
    private Point maxPoint(Point[] ps) {
        Point result = ps[0];
        for (int i = 1; i < ps.length; ++i) {
            if (ps[i].compareTo(result) > 0) {
                result = ps[i];
            }
        }
        return result;
    }
    private void printPoints(Point[] ps) {
        for (int k = 0; k < ps.length; ++k) {
            StdOut.print(ps[k] + ", ");
        }
        StdOut.println("");
    }
    private boolean checkRange(Point[] ps, int start, int end, double slope) {
        // scanning from right to left makes it faster
        for (int i = start; i < end; ++i) {
            if (!check(ps[i], slope)) { return false; }
        }
        return true;
    }
    private boolean check(Point p, double slope) {
        for (int i = 0; i < nEnd; ++i) {
            if (p.compareTo(endPoints[i]) == 0 && existSlopes[i] == slope) { 
                return false; 
            } 
        }
        return true;
    }
    public static void main(String[] args) {
        
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}