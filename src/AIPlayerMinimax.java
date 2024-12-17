/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231159 - Mohammad Ferdinand Valliandra
 * 2 - 5026231135 - Fachreza Aptadhi Kurniawan
 * 3 - 5026231149 - Ananda Donelly Reksana
 */

import java.util.*;

/**
 * AIPlayer using the Minimax algorithm with Alpha-Beta Pruning for Connect Four.
 */

public class AIPlayerMinimax extends AIPlayer {

    public AIPlayerMinimax(Board board) {
        super(board);
    }

    @Override
    public int[] move() {
        int[] result = minimax(4, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{result[1], result[2]}; // Return row, col
    }

    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        List<int[]> nextMoves = generateMoves();

        int bestRow = -1, bestCol = -1;
        int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                cells[move[0]][move[1]].content = player;
                int score = minimax(depth - 1, (player == mySeed) ? oppSeed : mySeed, alpha, beta)[0];
                cells[move[0]][move[1]].content = Seed.NO_SEED;

                if (player == mySeed) { // Maximizing player
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                    alpha = Math.max(alpha, bestScore);
                } else { // Minimizing player
                    if (score < bestScore) {
                        bestScore = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                    beta = Math.min(beta, bestScore);
                }
                if (alpha >= beta) break; // Alpha-beta pruning
            }
        }
        return new int[]{bestScore, bestRow, bestCol};
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            int row = getAvailableRow(col);
            if (row != -1) {
                nextMoves.add(new int[]{row, col});
            }
        }
        return nextMoves;
    }

    private int getAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (cells[row][col].content == Seed.NO_SEED) {
                return row;
            }
        }
        return -1;
    }

    private int evaluate() {
        int score = 0;

        // Evaluate score for all rows, columns, and diagonals
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == mySeed || cells[row][col].content == oppSeed) {
                    score += evaluatePosition(row, col, mySeed);
                    score -= evaluatePosition(row, col, oppSeed);
                }
            }
        }
        return score;
    }

    private int evaluatePosition(int row, int col, Seed seed) {
        int score = 0;

        // Check horizontal, vertical, and diagonal lines
        score += countLine(row, col, 0, 1, seed); // Horizontal
        score += countLine(row, col, 1, 0, seed); // Vertical
        score += countLine(row, col, 1, 1, seed); // Diagonal (top-left to bottom-right)
        score += countLine(row, col, 1, -1, seed); // Diagonal (top-right to bottom-left)

        return score;
    }

    private int countLine(int row, int col, int deltaRow, int deltaCol, Seed seed) {
        int count = 0;

        // Check forward direction
        for (int i = 0; i < 4; i++) {
            int newRow = row + i * deltaRow;
            int newCol = col + i * deltaCol;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                    && cells[newRow][newCol].content == seed) {
                count++;
            } else {
                break;
            }
        }

        // Check backward direction
        for (int i = 1; i < 4; i++) {
            int newRow = row - i * deltaRow;
            int newCol = col - i * deltaCol;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                    && cells[newRow][newCol].content == seed) {
                count++;
            } else {
                break;
            }
        }

        // Scoring logic
        if (count >= 4) return 100; // Winning move
        if (count == 3) return 10;  // Good move
        if (count == 2) return 1;   // Decent move
        return 0;
    }
}
