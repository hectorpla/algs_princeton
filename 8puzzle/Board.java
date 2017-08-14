import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.Math;
import java.lang.StringBuilder;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] blocks;
    private final int n;
    
    public Board(int[][] blocks) {         // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        this.blocks = Arrays.copyOf(blocks, blocks.length);
        n = blocks.length;
    }
    public int dimension() {                // board dimension n
        return n;
    }
    public int hamming() {                 // number of blocks out of place
        int count = 0, place = 1;
        for (int[] row : blocks) {
            for (int cell : row) {
                if (cell != place++) { count++; }
            }
        }
        if (blocks[n-1][n-1] == 0) { count++; }
        return count;
    }
    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int place = 0, sum = 0;
        for (int[] row : blocks) {
            for (int cell : row) {
                place++;
                if (cell == 0) { continue; }
                int dist = Math.abs(cell - place);
                sum += dist / n + dist % n;
            }
        }
        return sum;
    }
    public boolean isGoal() {               // is this board the goal board?
        int place = 0;
        for (int p = 0; p < n * n - 1; ++p) {
            if (blocks[p / n][p % n] != p + 1) {
                return false;
            }
        }
        return true;
    }
    public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
        int[][] tw = newBoard();
        if (n > 1) {
            exch(blocks, 0, 0, 0, 1);
        }
        return new Board(tw);
    }
    public boolean equals(Object y) {       // does this board equal y?
        Board yboard = (Board) y;
        if (yboard.n != this.n) { return false; }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++i) {
                if (blocks[i][j] != yboard.blocks[i][j]) { return false; }
            }
        }
        return true;
    }
    public Iterable<Board> neighbors() {   // all neighboring boards
        int iZero = 0, jZero = 0;
        int[] deltaX = {1, 0, 0, -1};
        int[] deltaY = {0, 1, -1, 0};
        int[][] board = newBoard();
        LinkedList<Board> neighbors = new LinkedList<>();
        
        for (int p = 0; p < n * n - 1; ++p) {
            if (blocks[p / n][p % n] == 0) {
                iZero = p / n;
                jZero = p % n;
                break;
            }
        }
        for (int d = 0; d < 4; ++d) {
            int i = iZero + deltaX[d], j = jZero + deltaY[d];
            if (i == 0 || i >= n || j == 0 || j >= n) { continue; }
            exch(board, iZero, jZero, i, j);
            Board neighbor = new Board(board);
            exch(board, i, j, iZero, jZero);
            neighbors.add(neighbor);
        }
        return neighbors;
    }
    public String toString() {             // string representation of this board (in the output format specified below)
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int[] row : blocks) {
            for (int cell : row) {
                sb.append(" " + cell);
            }
        }
        return sb.toString();
    }
    private int[][] newBoard() {
        // assume deep copy
        return Arrays.copyOf(blocks, blocks.length);
    }
    private void exch(int[][] board, int i1, int j1, int i2, int j2) {
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }
    public static void main(String[] args) { // unit tests (not graded)
        In in = new In("8puzzle/puzzle01.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board board = new Board(blocks);
        
        assert board.dimension() == n;
        assert board.hamming() == 2;
        assert board.manhattan() == 1;
        assert board.isGoal() == false;
    }
}