import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private final SearchNode searchNode;

    private class SearchNode {
        public final int moves;
        public final Board board;
        public final SearchNode prev;

        public SearchNode(Board b, int m, SearchNode previous) {
            prev = previous;
            board = b;
            moves = m;
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        searchNode = solve(initial);
    }

    private SearchNode solve(Board initial) {
        MinPQ<SearchNode> mainPQ = new MinPQ<>(comparator());
        MinPQ<SearchNode> twinPQ = new MinPQ<>(comparator());

        SearchNode node = new SearchNode(initial, 0, null),
                twin = new SearchNode(initial.twin(), 0, null);

        while (true) {
            if (node.board.isGoal()) return node;
            if (twin.board.isGoal()) return null;

            for (Board board : node.board.neighbors()) {
                if (node.prev != null && board.equals(node.prev.board)) continue;
                SearchNode s = new SearchNode(board, node.moves + 1, node);
                mainPQ.insert(s);
            }
            node = mainPQ.delMin();

            for (Board board : twin.board.neighbors()) {
                if (twin.prev != null && board.equals(twin.prev.board)) continue;
                SearchNode s = new SearchNode(board, twin.moves + 1, twin);
                twinPQ.insert(s);
            }
            twin = twinPQ.delMin();
        }
    }

    private Comparator<SearchNode> comparator() {
        return (n1, n2) -> {
            int p1 = n1.board.manhattan() + n1.moves;
            int p2 = n2.board.manhattan() + n2.moves;
            return p1 - p2;
        };
    }

    public boolean isSolvable() {
        return searchNode != null;
    }

    public int moves() {
        return searchNode == null ? -1 : searchNode.moves;
    }

    public Iterable<Board> solution() {
        if (searchNode == null) return null;
        SearchNode node = searchNode;

        Stack<Board> stack = new Stack<>();
        while (node != null) {
            stack.push(node.board);
            node = node.prev;
        }
        return stack;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
