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

// Class untuk mengelola papan permainan Connect Four
public class Board {
    // Konstanta untuk ukuran papan
    public static final int ROWS = 6;  // Jumlah baris pada papan
    public static final int COLS = 7;  // Jumlah kolom pada papan
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // Lebar kanvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS; // Tinggi kanvas
    public static final int GRID_WIDTH = 8;  // Lebar garis grid
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Setengah lebar garis grid
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // Warna grid

    // Matriks sel yang membentuk papan permainan
    Cell[][] cells;

    // Konstruktor untuk menginisialisasi papan
    public Board() {
        initGame(); // Panggil metode untuk menginisialisasi sel
    }

    // Inisialisasi papan permainan dengan sel kosong
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // Alokasikan array 2D untuk sel
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col); // Buat objek sel untuk setiap posisi
            }
        }
    }

    // Cari baris terendah yang tersedia dalam kolom tertentu
    public int getAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) { // Mulai dari baris terbawah
            if (cells[row][col].content == Seed.NO_SEED) { // Jika sel kosong
                return row; // Kembalikan baris
            }
        }
        return -1; // Jika kolom penuh, kembalikan -1
    }

    // Metode untuk memeriksa apakah pemain telah menang
    public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
        // Periksa secara horizontal
        int count = 0;
        for (int col = 0; col < COLS; ++col) {
            if (cells[rowSelected][col].content == theSeed) {
                count++;
                if (count == 4) return true; // Ada 4 berturut-turut
            } else {
                count = 0; // Reset jika tidak berturut-turut
            }
        }

        // Periksa secara vertikal
        count = 0;
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][colSelected].content == theSeed) {
                count++;
                if (count == 4) return true; // Ada 4 berturut-turut
            } else {
                count = 0; // Reset jika tidak berturut-turut
            }
        }

        // Periksa diagonal (kiri atas ke kanan bawah)
        count = 0;
        int startRow = Math.max(0, rowSelected - colSelected);
        int startCol = Math.max(0, colSelected - rowSelected);
        while (startRow < ROWS && startCol < COLS) {
            if (cells[startRow][startCol].content == theSeed) {
                count++;
                if (count == 4) return true; // Ada 4 berturut-turut
            } else {
                count = 0; // Reset jika tidak berturut-turut
            }
            startRow++;
            startCol++;
        }

        // Periksa diagonal (kanan atas ke kiri bawah)
        count = 0;
        startRow = Math.max(0, rowSelected - (COLS - 1 - colSelected));
        startCol = Math.min(COLS - 1, colSelected + rowSelected);
        while (startRow < ROWS && startCol >= 0) {
            if (cells[startRow][startCol].content == theSeed) {
                count++;
                if (count == 4) return true; // Ada 4 berturut-turut
            } else {
                count = 0; // Reset jika tidak berturut-turut
            }
            startRow++;
            startCol--;
        }

        return false; // Tidak ada 4 berturut-turut
    }

    // Periksa apakah permainan berakhir seri
    public boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return false; // Jika ada sel kosong, permainan belum seri
                }
            }
        }
        return true; // Semua sel terisi, permainan seri
    }

    // Melukis papan permainan
    public void paint(Graphics g) {
        // Gambar grid
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRect(0, row * Cell.SIZE - GRID_WIDTH_HALF, CANVAS_WIDTH - 1, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRect(col * Cell.SIZE - GRID_WIDTH_HALF, 0, GRID_WIDTH, CANVAS_HEIGHT - 1);
        }

        // Gambar setiap sel
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g); // Minta setiap sel menggambar dirinya sendiri
            }
        }
    }
}
