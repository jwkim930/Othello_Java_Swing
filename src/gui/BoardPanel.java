package gui;

import javax.swing.*;

import backend.Board;

import static gui.StartupFrame.scale;

import java.awt.*;

/**
 * A subclass of JPanel that displays the game board.
 */
public class BoardPanel extends JPanel {
    /**
     * Base width/height for 1080p.
     */
    private final static int BASE_SIZE = 650;

    /**
     * The scaled width/height of this panel in pixels.
     */
    public static final int SIZE = scale(BASE_SIZE);


    /**
     * Initializes the panel. Should only be called once in the beginning.
     *
     * @param boardSize The size of the game board.
     */
    public BoardPanel(int boardSize) {
        super();
        Board.initialize(boardSize);
        this.setLayout(new GridLayout(boardSize, boardSize));
        Dimension sizeDimension = new Dimension(SIZE, SIZE);
        this.setSize(sizeDimension);
        this.setPreferredSize(sizeDimension);
        this.setMinimumSize(sizeDimension);
        this.setMaximumSize(sizeDimension);
        SquarePanel[][] squares;
        squares = Board.getInstance().getSquares();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                SquarePanel square = squares[row][col];
                this.add(square);
            }
        }
    }
}
