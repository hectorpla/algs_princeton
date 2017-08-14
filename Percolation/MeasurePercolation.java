import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdOut;

public class MeasurePercolation {
    private int ntimes, ttimes;
    
    public MeasurePercolation(int ntimes, int ttimes) {
        this.ntimes = ntimes;
        this.ttimes = ttimes;
    }
    
    public void nSeries() {
        int n = 50, t = 100;
        for (int i = 0; i < ntimes; ++i) {
            Stopwatch watch = new Stopwatch();
            PercolationStats pcs = new PercolationStats(n, t);
            StdOut.printf("n = %d, t = %d: %f\n", n, t, watch.elapsedTime());
            n <<= 1;
        }
    }
    
    public static void main(String[] args) { 
        MeasurePercolation mpc = new MeasurePercolation(6, 10);
        mpc.nSeries();
    }
    
    /* ADD YOUR CODE HERE */
    
}
