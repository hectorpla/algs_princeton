//import java.lang.IllegalArgumentException;

public class Percolation {
    boolean block[]; // true indicates open
    int id[];
    int sz[];
    int n;
    int numOpen = 0;
   
   public Percolation(int n)                // create n-by-n grid, with all sites blocked
   {
       if (n <= 0) throw new IllegalArgumentException("n should be positive");
       this.n = n;
       int size = n*n;
       block = new boolean[size];
       for (int i = 0; i < size; ++i) block[i] = false;
       
       id = new int[size + 2];
       sz = new int[size + 2];
       for (int i = 0; i < size + 2; ++i) 
       {
           id[i] = i;
           sz[i] = 1;
       }    

       // union two virtual ends accordingly with the first and last row
//       for (int col = 1; col <= n; ++col)
//       {
//           union(cellNum(1, col), n * n);
//           union(cellNum(n, col), n * n + 1);
//       }
   }
   public void open(int row, int col)       // open site (row, col) if it is not open already
   {
      int cell = cellNum(row, col);
      
      block[cell] = true;
      if (row == 1) union(cell, topVirtual());
      if (row == n) union(cell, bottomVirtual());
      if (row > 1 && isOpen(row - 1, col)) union(cell, cellNum(row - 1, col));
      if (row < n && isOpen(row + 1, col)) union(cell, cellNum(row + 1, col));
      if (col > 1 && isOpen(row, col - 1)) union(cell, cellNum(row, col - 1));
      if (col < n && isOpen(row, col + 1)) union(cell, cellNum(row, col + 1));
   }
   public boolean isOpen(int row, int col)  // is site (row, col) open?
   {
       return  block[cellNum(row, col)];
   }
   public boolean isFull(int row, int col)  // is site (row, col) full?
   {
       return !isOpen(row, col);
   }
   public boolean percolates()              // does the system percolate?
   {
       return connected(topVirtual(),  bottomVirtual());
   }
   
   private int topVirtual() 
   {
       return n * n;
   }
   private int bottomVirtual() 
   {
       return n * n + 1;
   }
   private int find(int p) {
       while (p != id[p]) {
           id[p] = id[id[p]];
           p = id[p];
       }
       return p;
   }
   private void union(int p, int q)
   {
      int proot = find(p);
      int qroot = find(q);
      
      if (proot == qroot) return;
      
      if (sz[proot] >= sz[qroot])
      {
          id[qroot] = proot;
          sz[proot] += sz[qroot];
          System.out.println("union: " + qroot + " -> " + proot + ", size after: " + sz[proot]);
      }
      else
      {
          id[proot] = qroot;
          sz[qroot] += sz[proot];
          System.out.println("union: " + proot + " -> " + qroot + ", size after: " + sz[qroot]);
      }
   }
   private boolean connected(int p, int q) {
       return find(p) == find(q);
   }
   private int cellNum(int row, int col) {
      return (row-1) * n + col - 1;
   }
   private void printBlock() {
       for (int i = 1; i <= n; ++i)
       {
           for (int j = 1; j <= n; ++j)
           {
               System.out.print((isOpen(i, j) ? 1 : 0) + " ");
           }
           System.out.println("");
       }
   }
   private void printInfo()
   {
       int toproot = find(n*n);
       int bottomroot = find(n*n+1);
       System.out.println("top id: " + toproot + 
                              " (size " + sz[toproot] + 
                              "), bottom id: " + bottomroot + 
                              "(size " + sz[bottomroot] + ")");
   }
   public static void main(String[] args)   // test client (optional)
   {
       // small test
       Percolation pc = new Percolation(3);
       pc.printBlock();
       System.out.println("Initially percolates: " + pc.percolates());
       for (int i = 1; i < 3; ++i) 
       {
           pc.open(1, i);
           pc.printInfo();
       }
       assert(pc.connected(0, 1));
       assert(pc.connected(1, 2));
       assert(!pc.connected(2, 3));
       pc.printBlock();
       System.out.println("-------------------------------");
       System.out.println("After ops of row open percolates: " + pc.percolates());
       
       for (int i = 2; i < 4; ++i)
       {
           pc.open(i, 2);
           pc.printInfo();
       }
       assert(pc.percolates());
       System.out.println("-------------------------------");
       pc.printBlock();
       System.out.println("After ops of col open percolates: " + pc.percolates());
   }
}