import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private final int n;
    private final int trials;
    private double[] thresholds;
    private double samplemean;
    private double s;
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) 
            throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        this.thresholds = new double[trials];
//        System.out.println("Initialize: about to do trials");
        doTrials();
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
    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trials <= 1) 
            return Double.NaN;
        double m = samplemean;
        double sum = 0;
        for (int i = 0; i < thresholds.length; ++i) {
            sum += (thresholds[i] - m) * (thresholds[i] - m);
        }
        this.s = Math.sqrt(sum / (trials - 1)); // T = 1?
        return this.s;
    }
    public double confidenceLo() {  // low  endpoint of 95% confidence interval
        double m = samplemean;
        return m - 1.96 * s / Math.sqrt(trials);
    }
    public double confidenceHi() { // high endpoint of 95% confidence interval
        double m = samplemean;
        return m + 1.96 * s / Math.sqrt(trials);
    }
    private void doTrials() {
        for (int i = 0; i < trials; ++i) {
            thresholds[i] = doTrial();
//            System.out.print(thresholds[i] + " ");
        }
//        System.out.println("");
        mean();
        stddev();
    }
    private double doTrial() {
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
    public static void main(String[] args) {  // test client (described below)
        int n = 200;
        int trials = 100;
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
            } 
            catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + 
                                   " must be an integer.");
            }
            if (args.length > 1) {
                try {
                    trials = Integer.parseInt(args[0]);
                } 
                catch (NumberFormatException e) {
                    System.err.println("Argument" + args[0] + 
                                       " must be an integer.");
                }
            }
        }
        PercolationStats pcs = new PercolationStats(n, trials);
        System.out.println("mean: " + pcs.mean());
        System.out.println("stddev: " + pcs.stddev());
        System.out.println("95% confidence interval: [" + 
                         pcs.confidenceLo() + ", " + pcs.confidenceHi() + "]");
    }
}
