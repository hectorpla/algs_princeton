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
        n = blocks.length;
        this.blocks = newBoard(blocks);
    }
    public int dimension() {                // board dimension n
        return n;
    }
    public int hamming() {                 // number of blocks out of place
        int place = 0, count = 0;
        for (int p = 0; p < n * n - 1; ++p) {
            if (blocks[p / n][p % n] != p + 1) {
                count++;
            }
        }
        StdOut.println("hamming: " + count);
        return count;
    }
    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int place = 0, sum = 0;
        for (int[] row : blocks) {
            for (int cell : row) {
                place++;
                if (cell == 0) { continue; }
                int m1 = (cell - 1) / n, n1 = (cell - 1) % n;
                int m2 = (place - 1) / n, n2 = (place - 1) % n;
                sum += Math.abs(m1 - m2) + Math.abs(n1 - n2);
            }
        }
        StdOut.println("manhattan: " + sum);
        return sum;
    }
    public boolean isGoal() {               // is this board the goal board?
        return hamming() == 0;
    }
    public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
        int[][] tw = newBoard(blocks);
        if (n > 1) {
            exch(tw, 0, 0, 0, 1);
        }
        return new Board(tw);
    }
    public boolean equals(Object y) {       // does this board equal y?
        Board yboard = (Board) y;
        if (yboard.n != this.n) { return false; }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blocks[i][j] != yboard.blocks[i][j]) { return false; }
            }
        }
        return true;
    }
    public Iterable<Board> neighbors() {   // all neighboring boards
        int iZero = 0, jZero = 0;
        int[] deltaX = {1, 0, 0, -1};
        int[] deltaY = {0, 1, -1, 0};
        int[][] board = newBoard(blocks);
        LinkedList<Board> neighbors = new LinkedList<>();
        
//        StdOut.println(new Board(board));
        for (int p = 0; p < n * n - 1; ++p) {
//            StdOut.println(p);
            if (board[p / n][p % n] == 0) {
                iZero = p / n;
                jZero = p % n;
                break;
            }
        }
//        StdOut.println(iZero + ", " + jZero);
        for (int d = 0; d < 4; ++d) {
            int i = iZero + deltaX[d], j = jZero + deltaY[d];
            if (i < 0 || i >= n || j < 0 || j >= n) { continue; }
            StdOut.println(deltaX[d] + ", " + deltaY[d]);
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
            sb.append("\n");
        }
        return sb.toString();
    }
    private int[][] newBoard(int[][] old) {
        // copyOf: shallow copy
//        return Arrays.copyOf(blocks, blocks.length);
        int[][] board = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                board[i][j] = old[i][j];
            }
        }
        return board;
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
        assert board.hamming() == 1;
        assert board.manhattan() == 1;
        assert board.isGoal() == false;
        
        StdOut.println("original:\n" + board);
        StdOut.println("twin:\n" + board.twin());
        
        StdOut.println("neighbors:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
        
        StdOut.println("--------------------------------");
        
        // test on 3 x 3 puzzle
        In in2 = new In("8puzzle/puzzle3x3-19.txt");
        int n2 = in2.readInt();
        int[][] blocks2 = new int[n2][n2];
        for (int i = 0; i < n2; i++)
            for (int j = 0; j < n2; j++)
            blocks2[i][j] = in2.readInt();
        Board board2 = new Board(blocks2);
        StdOut.println("original:\n" + board2);
        
        assert board2.dimension() == n2;
        assert board2.hamming() == 7;
        assert board2.manhattan() == 19;
        assert board2.isGoal() == false;
        
        StdOut.println("twin:\n" + board2.twin());
        
        StdOut.println("neighbors:");
        for (Board neighbor : board2.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}