import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] tiles;
    private final int dim;
    private int man = -1;
    private int ham = -1;

    public Board(int[][] tiles) {
        dim = tiles.length;
        this.tiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                str.append(" ").append(tiles[i][j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        if (ham < 0) {
            ham = 0;
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (tiles[i][j] == 0) continue;
                    if (tiles[i][j] != i * dim + j + 1) ham++;
                }
            }
        }
        return ham;
    }

    public int manhattan() {
        if (man < 0) {
            man = 0;
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (tiles[i][j] == 0) continue;
                    int ri = (tiles[i][j] - 1) / dim;
                    int rj = tiles[i][j] - ri * dim - 1;
                    man += Math.abs(i - ri) + Math.abs(j - rj);
                }
            }
        }
        return man;
    }

    public boolean isGoal() {
        return manhattan() == 0;
    }

    public boolean equals(Object y) {
        if (this == y) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        if (that.dimension() != dimension()) return false;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (that.tiles[i][j] != tiles[i][j]) return false;
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> n = new Stack<>();
        int bi = -1, bj = -1;
        int[][] copy = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                copy[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    bi = i;
                    bj = j;
                }
            }
        }
        if (bi - 1 >= 0) fakeExch(copy, n, bi, bj, -1, 0);
        if (bj + 1 < dim) fakeExch(copy, n, bi, bj, 0, 1);
        if (bi + 1 < dim) fakeExch(copy, n, bi, bj, 1, 0);
        if (bj - 1 >= 0) fakeExch(copy, n, bi, bj, 0, -1);

        return n;
    }

    private void fakeExch(int[][] ts, Stack<Board> stack, int bi, int bj, int roffset,
                          int coffset) {
        int tile = ts[bi + roffset][bj + coffset];
        ts[bi][bj] = tile;
        ts[bi + roffset][bj + coffset] = 0;
        stack.push(new Board(ts));
        ts[bi][bj] = 0;
        ts[bi + roffset][bj + coffset] = tile;
    }

    public Board twin() {
        int bi = -1;
        int bj = -1;
        int[][] copy = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tiles[i][j] == 0) {
                    bi = i;
                    bj = j;
                }
                copy[i][j] = tiles[i][j];
            }
        }
        int i1 = 0, i2 = 1, j1 = 0, j2 = 1;
        if (i1 == bi && j1 == bj) i1++;
        if (i2 == bi && j2 == bj) i2--;

        int swap = copy[i1][j1];
        copy[i1][j1] = copy[i2][j2];
        copy[i2][j2] = swap;

        return new Board(copy);
    }

    public static void main(String[] args) {
        int[][] stiles = { { 1, 2, 3 }, { 0, 7, 6 }, { 5, 4, 8 } };
        int[][] tiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board b = new Board(stiles);
        Board c = new Board(tiles);
        StdOut.println(b);
        StdOut.println("Dimension: " + b.dimension());
        StdOut.println("Hamming: " + b.hamming());
        StdOut.println("Manhattan: " + b.manhattan());
        StdOut.println("Is goal: " + b.isGoal());
        StdOut.println("b equals to c: " + b.equals(c));

        for (Board board : b.neighbors())
            StdOut.println(board);
        StdOut.println("Twin:\n" + b.twin());
    }

}
