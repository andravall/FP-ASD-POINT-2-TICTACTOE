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
 * AIPlayer using heuristic evaluation to determine the move.
 */

public class AIPlayerHeuristic extends AIPlayer {

    /** Constructor with reference to the game board */
    public AIPlayerHeuristic(Board board) {
        super(board);
    }

    /** Get the next move using heuristic evaluation */
    @Override
    public int[] move() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    // Try the move
                    cells[row][col].content = mySeed;
                    int score = evaluateBoard();
                    cells[row][col].content = Seed.NO_SEED; // Undo the move

                    // Update the best score and move
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }
        return bestMove;
    }

    /** Evaluate the board and return a score */
    private int evaluateBoard() {
        int score = 0;
        // Evaluate score for all rows, columns, and diagonals
        for (int row = 0; row < ROWS; row++) {
            score += evaluateLine(row, 0, row, 1, row, 2, row, 3);
        }
        for (int col = 0; col < COLS; col++) {
            score += evaluateLine(0, col, 1, col, 2, col, 3, col);
        }
        score += evaluateLine(0, 0, 1, 1, 2, 2, 3, 3); // Main diagonal
        score += evaluateLine(0, 3, 1, 2, 2, 1, 3, 0); // Anti-diagonal
        return score;
    }

    /**
     * Evaluate one line of the board and return a score.
     */
    private int evaluateLine(int r1, int c1, int r2, int c2, int r3, int c3, int r4, int c4) {
        int score = 0;

        // Count my seeds and opponent's seeds in this line
        int mySeedCount = 0;
        int oppSeedCount = 0;

        if (cells[r1][c1].content == mySeed) mySeedCount++;
        else if (cells[r1][c1].content == oppSeed) oppSeedCount++;

        if (cells[r2][c2].content == mySeed) mySeedCount++;
        else if (cells[r2][c2].content == oppSeed) oppSeedCount++;

        if (cells[r3][c3].content == mySeed) mySeedCount++;
        else if (cells[r3][c3].content == oppSeed) oppSeedCount++;

        if (cells[r4][c4].content == mySeed) mySeedCount++;
        else if (cells[r4][c4].content == oppSeed) oppSeedCount++;

        // Calculate score based on counts
        if (mySeedCount == 4) {
            score += 100; // 4-in-a-line for AI
        } else if (mySeedCount == 3 && oppSeedCount == 0) {
            score += 10; // 3-in-a-line for AI
        } else if (mySeedCount == 2 && oppSeedCount == 0) {
            score += 1; // 2-in-a-line for AI
        } else if (oppSeedCount == 4) {
            score -= 100; // 4-in-a-line for opponent
        } else if (oppSeedCount == 3 && mySeedCount == 0) {
            score -= 10; // 3-in-a-line for opponent
        } else if (oppSeedCount == 2 && mySeedCount == 0) {
            score -= 1; // 2-in-a-line for opponent
        }

        return score;
    }
}
