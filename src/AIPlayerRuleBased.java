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
 * AIPlayer that uses rule-based strategy.
 * Rule 1: If AI has a winning move, take it.
 * Rule 2: If opponent has a winning move, block it.
 * Rule 3: If AI can create a fork, do it.
 * Rule 4: Block opponent's fork.
 */
/**
 * AIPlayer that uses rule-based strategy.
 */

public class AIPlayerRuleBased extends AIPlayer {

    public AIPlayerRuleBased(Board board) {
        super(board);
    }

    @Override
    public int[] move() {
        // Rule 1: Win if possible
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = mySeed;
                    if (checkWin(mySeed, row, col)) {
                        cells[row][col].content = Seed.NO_SEED;
                        return new int[]{row, col};
                    }
                    cells[row][col].content = Seed.NO_SEED;
                }
            }
        }

        // Rule 2: Block opponent's winning move
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = oppSeed;
                    if (checkWin(oppSeed, row, col)) {
                        cells[row][col].content = Seed.NO_SEED;
                        return new int[]{row, col};
                    }
                    cells[row][col].content = Seed.NO_SEED;
                }
            }
        }

        // Rule 3: Fallback strategy (use simple table lookup)
        AIPlayerTableLookup fallback = new AIPlayerTableLookup(new Board());
        fallback.setSeed(mySeed);
        return fallback.move();
    }

    private boolean checkWin(Seed seed, int row, int col) {
        // Use hasWon logic from the Board class directly
        Board tempBoard = new Board(); // Temporary board instance
        tempBoard.cells = cells;
        return tempBoard.hasWon(seed, row, col);
    }
}