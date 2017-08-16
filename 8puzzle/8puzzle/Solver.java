import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private final Board initial;
    private Integer minMoves;
    private LinkedList<Board> solution;
    
    // should implement the Comparable interface, otherwise runtime error
    private class SearchNode implements Comparable<SearchNode> {
        // set it handy to outter class
        public final int heuristic;
        public final Board board;
        public final int moves;
        public final SearchNode parent;
        
        public SearchNode(int movesMade, Board b, SearchNode parent) {
            this.moves = movesMade;
	    this.heuristic = b.hamming() + movesMade;
            // this.heuristic = b.manhattan() + movesMade;
            this.board = b;
            this.parent = parent;
        }
        public int compareTo(SearchNode other) {
            if (heuristic < other.heuristic) return -1;
            if (heuristic == other.heuristic) return 0;
            return 1;
        }
    }
    
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        // reference to the initial board, no copy method in Board API
        this.initial = initial;
    }
    public boolean isSolvable() {           // is the initial board solvable?
        Board twinBoard = initial.twin();
        MinPQ<SearchNode> origPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        Stack<SearchNode> reversePath = new Stack<>();
        solution = new LinkedList<>();
        
        origPQ.insert(new SearchNode(0, initial, null));
        twinPQ.insert(new SearchNode(0, twinBoard, null));
        while (true) {
//            StdOut.println("original PQ:");
//            for (SearchNode node : origPQ) {
//                StdOut.println("moves: " + node.moves + ", hamming: " + 
//                               node.board.hamming() + "\nheuristic: " + 
//                               node.heuristic + "\n" + node.board);
//            }
//            StdOut.println("twin PQ:");
//            for (SearchNode node : twinPQ) {
//                StdOut.println(node.board);
//            }
            SearchNode sno = origPQ.delMin(), snt = twinPQ.delMin();
            Board bo = sno.board, bt = snt.board;
            int movesOrig = sno.moves, movesTwin = snt.moves;
            
            if (bo.isGoal()) {
                SearchNode cur = sno;
                while (cur != null) {
                    reversePath.push(cur);
                    cur = cur.parent;
                }
                while (!reversePath.empty()) {
                    solution.add(reversePath.pop().board);
                }
                minMoves = solution.size() - 1;
                return true;
            }
            else if (bt.isGoal()) {
                minMoves = -1;
                return false;
            }
//            StdOut.println("orig children:\n");
            for (Board neighbor : bo.neighbors()) {
//                StdOut.println(neighbor);
                if (sno.parent != null && neighbor.equals(sno.parent.board)) { 
//                    StdOut.println("repeated"); 
                    continue; 
                }
                origPQ.insert(new SearchNode(movesOrig + 1, neighbor, sno));
            }
//            StdOut.println("twin children:");
            for (Board neighbor : bt.neighbors()) {
//                StdOut.println(neighbor);
                if (snt.parent != null && neighbor.equals(snt.parent.board)) { 
                    continue; 
                }
                twinPQ.insert(new SearchNode(movesTwin + 1,neighbor, null));
            }
//            StdOut.println("--------");
        }
    }
    public final int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if (minMoves == null) isSolvable();
        return minMoves;
    }
    public final Iterable<Board> solution() {   // sequence of boards in a shortest solution; null if unsolvable
        
        return solution;
    }
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
