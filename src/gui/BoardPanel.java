package gui;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.swing.*;

import backend.Board;

import java.awt.*;

/**
 * A subclass of JPanel that displays the game board.
 */
public class BoardPanel extends JPanel {
    /**
     * The width/height of this panel in pixels.
     */
    public static final int SIZE = 650;

    /**
     * Initializes the panel. Should only be called once in the beginning.
     *
     * @param boardSize The size of the game board.
     */
    public BoardPanel(int boardSize) {
        super();
        try {
            Board.initialize(boardSize);
        } catch (InstanceAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        this.setLayout(new GridLayout(boardSize, boardSize));
        Dimension sizeDimension = new Dimension(SIZE, SIZE);
        this.setSize(sizeDimension);
        this.setPreferredSize(sizeDimension);
        this.setMinimumSize(sizeDimension);
        this.setMaximumSize(sizeDimension);
        SquarePanel[][] squares;
        try {
            squares = Board.getInstance().getSquares();
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                SquarePanel square = squares[row][col];
                this.add(square);
            }
        }
    }
}
