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
 * AIPlayer that uses simple table lookup to determine the move.
 */

public class AIPlayerTableLookup extends AIPlayer {

    // Preferred moves in priority order
    private final int[][] preferredMoves = {
            {3, 3}, {0, 0}, {0, 6}, {5, 0}, {5, 6}, // Center, corners
            {0, 3}, {3, 0}, {3, 6}, {5, 3}          // Edges
    };

    /** Constructor with reference to the game board */
    public AIPlayerTableLookup(Board board) {
        super(board);
    }

    /** Get the next move using table lookup */
    @Override
    public int[] move() {
        for (int[] move : preferredMoves) {
            if (cells[move[0]][move[1]].content == Seed.NO_SEED) {
                return move; // Return the first available preferred move
            }
        }
        return null; // No valid move found
    }
}
