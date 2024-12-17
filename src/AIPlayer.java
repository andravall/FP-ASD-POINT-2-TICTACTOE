/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231159 - Mohammad Ferdinand Valliandra
 * 2 - 5026231135 - Fachreza Aptadhi Kurniawan
 * 3 - 5026231149 - Ananda Donelly Reksana
 */

/**
 * Abstract superclass for all AI players with different strategies.
 */

public abstract class AIPlayer {
    protected int ROWS = Board.ROWS;  // Number of rows
    protected int COLS = Board.COLS; // Number of columns

    protected Cell[][] cells; // papan permainan dalam bentuk array 2D
    protected Seed mySeed;    // Computer's seed
    protected Seed oppSeed;   // Opponent's seed

    /** Constructor with reference to game board */
    public AIPlayer(Board board) {
        cells = board.cells;
    }

    /** Set/change the seed used by computer and opponent */
    public void setSeed(Seed seed) {
        this.mySeed = seed;
        this.oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    /** Abstract method to get next move. Return int[2] of {row, col} */
    public abstract int[] move();
}
