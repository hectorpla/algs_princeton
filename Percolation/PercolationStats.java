import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private int n;
    private int trials;
    private double[] thresholds;
    private double samplemean;
    private double s;
    
    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) 
            throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        this.thresholds = new double[trials];
        System.out.println("Initialize: about to do trials");
        do_trials();
    }
    public double mean() {    // sample mean of percolation threshold
        double sum = 0;
        for (int i = 0; i < thresholds.length; ++i) {
            sum += thresholds[i];
        }
        double m = sum / trials;
        samplemean = m;
        return m;
    }
    public double stddev() {   // sample standard deviation of percolation threshold
        double m = samplemean;
        double sum = 0;
        for (int i = 0; i < thresholds.length; ++i) {
            sum += (thresholds[i] - m) * (thresholds[i] - m);
        }
        this.s = Math.sqrt(sum / (trials - 1)); // T = 1?
        return this.s;
    }
    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        double m = samplemean;
        return m - 1.96 * s / Math.sqrt(trials - 1);
    }
    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        double m = samplemean;
        return m + 1.96 * s / Math.sqrt(trials - 1);
    }
    private void do_trials() {
        for (int i = 0; i < trials; ++i) {
            thresholds[i] = do_trial();
//            System.out.print(thresholds[i] + " ");
        }
//        System.out.println("");
        mean();
        stddev();
    }
    private double do_trial() {
        Percolation pc = new Percolation(n);
        double count = 0;
        while (!pc.percolates()) {
            int row, col;
            do {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
            } while (pc.isOpen(row, col));
            pc.open(row, col);
            count += 1;
//            System.out.println(row + ", " + col);
        }
        return count / (n * n);
    }
    public static void main(String[] args) {       // test client (described below)
        int n = 200;
        int trials = 100;
        PercolationStats pcs = new PercolationStats(n, trials);
        System.out.println("mean: " + pcs.mean());
        System.out.println("stddev: " + pcs.stddev());
        System.out.println("95% confidence interval: [" + 
                         pcs.confidenceLo() + ", " + pcs.confidenceHi() + "]");
    }
}
