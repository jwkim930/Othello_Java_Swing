package gui;

import javax.swing.*;

import backend.Board;

import java.awt.*;

public class BoardPanel extends JPanel implements Rebuildable {
    /**
     * The board for the current game.
     */
    Board board;
    /**
     * The width/height of this panel in pixels.
     */
    public static final int SIZE = 600;

    /**
     * Initializes the panel. Should only be called once in the beginning.
     */
    public BoardPanel() {
        super();
        this.board = new Board();
        int boardSize = Board.getSize();
        this.setLayout(new GridLayout(boardSize, boardSize));
        Dimension sizeDimension = new Dimension(SIZE, SIZE);
        this.setSize(sizeDimension);
        this.setPreferredSize(sizeDimension);
        this.setMinimumSize(sizeDimension);
        this.setMaximumSize(sizeDimension);
        SquarePanel[][] squares = board.getSquares();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                SquarePanel square = squares[row][col];
                this.add(square);
            }
        }
    }

    public void rebuild() {
        // stub for now
    }
}
