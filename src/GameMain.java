/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231159 - Mohammad Ferdinand Valliandra
 * 2 - 5026231135 - Fachreza Aptadhi Kurniawan
 * 3 - 5026231149 - Ananda Donelly Reksana
 */

import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import javax.swing.*;
import java.util.Random;

public class GameMain extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "TicTacToe Connect Four Human vs AI Connect";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private final JLabel statusBar;    // for displaying status message
    private AIPlayer aiPlayer;   // AI player instance
    private final Random random; // Random generator for deciding first player

    /** Constructor to set up the UI and game components */
    public GameMain() {
        random = new Random(); // Initialize random generator

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int col = mouseX / Cell.SIZE; // Get the column clicked

                if (currentState == State.PLAYING) {
                    if (col >= 0 && col < Board.COLS) {
                        int row = board.getAvailableRow(col); // Get lowest available row
                        if (row != -1) { // Valid move
                            board.cells[row][col].content = currentPlayer; // Drop the piece
                            SoundEffect.EAT_FOOD.play(); // Play sound for human move
                            updateGame(currentPlayer, row, col); // Check game state

                            // If still playing and it's AI's turn, let AI make a move
                            if (currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                                int[] aiMove = aiPlayer.move();
                                board.cells[aiMove[0]][aiMove[1]].content = aiPlayer.mySeed;
                                SoundEffect.EXPLODE.play(); // Play sound for AI move
                                updateGame(aiPlayer.mySeed, aiMove[0], aiMove[1]);
                            }
                        }
                    }
                } else { // Game over
                    newGame();  // Restart the game
                }
                repaint(); // Refresh the drawing canvas
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();  // Allocate the game-board

        // Instantiate AI Player (using Minimax strategy)
        aiPlayer = new AIPlayerMinimax(board);
        aiPlayer.setSeed(Seed.NOUGHT); // AI is 'O'

        // Preload sound effects
        SoundEffect.initGame();
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // All cells empty
            }
        }
        currentState = State.PLAYING;  // Ready to play

        // Randomize who plays first
        if (random.nextBoolean()) {
            currentPlayer = Seed.CROSS; // Human plays first
            statusBar.setText("Human starts! X's Turn");
        } else {
            currentPlayer = Seed.NOUGHT; // AI plays first
            statusBar.setText("AI starts! O's Turn");

            // If AI starts, make the first move
            int[] aiMove = aiPlayer.move();
            board.cells[aiMove[0]][aiMove[1]].content = aiPlayer.mySeed;
            SoundEffect.EXPLODE.play(); // Play sound for AI move
            updateGame(aiPlayer.mySeed, aiMove[0], aiMove[1]);
        }
    }

    /** Update the game state after a move */
    public void updateGame(Seed player, int row, int col) {
        if (board.hasWon(player, row, col)) { // Check for win
            currentState = (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;

            // Play sound when human wins
            if (currentState == State.CROSS_WON) {
                SoundEffect.WIN.play();
            }

            // Play sound when AI wins
            if (currentState == State.NOUGHT_WON) {
                SoundEffect.DIE.play();
            }

        } else if (board.isDraw()) { // Check for draw
            currentState = State.DRAW;
        } else {
            currentState = State.PLAYING; // Continue playing
            currentPlayer = (player == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS; // Switch player
        }
        updateStatusBar();
    }

    /** Update the status bar message */
    private void updateStatusBar() {
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "Human's Turn" : "AI's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Human' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'AI' Won! Click to play again.");
        }
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG); // Set its background color

        board.paint(g);  // Ask the game board to paint itself
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new GameMain());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the application window
            frame.setVisible(true);            // Show it
        });
    }
}
