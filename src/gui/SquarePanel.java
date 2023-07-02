package gui;

import backend.Board;
import backend.SquareMouseListener;
import backend.Stone;

import javax.swing.*;
import java.awt.*;

public class SquarePanel extends JPanel implements Rebuildable {
    /**
     * The stone on this square. null if there is no stone.
     */
    private Stone stone;
    /**
     * The size of this square in pixels. It is -1 before it is set.
     */
    private static int squareSize = -1;

    public SquarePanel() {
        super();
        int size = getSquareSize();
        Dimension sizeDimension = new Dimension(size, size);
        this.setSize(sizeDimension);
        this.setPreferredSize(sizeDimension);
        this.setMinimumSize(sizeDimension);
        this.setMaximumSize(sizeDimension);
        this.addMouseListener(new SquareMouseListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        int size = getSquareSize();
        // draw the square
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, size, size);
        // draw the stone
        if (this.stone != null) {
            int stoneSize = (int) (size * 0.3);
            int stoneLocation = (size - stoneSize) / 2;
            g.setColor(this.stone.getColor());
            g.fillOval(stoneLocation, stoneLocation, stoneSize, stoneSize);
            g.setColor(Color.BLACK);
            g.drawOval(stoneLocation, stoneLocation, stoneSize, stoneSize);
        }
    }

    public void rebuild() {
        this.removeAll();
        this.repaint();
    }

    public Stone getStone() {
        return this.stone;
    }

    /**
     * Places the stone and refreshes the panel.
     *
     * @param stone The stone to be placed.
     * @throws IllegalStateException If the square already has a stone.
     */
    public void place(Stone stone) {
        if (this.stone != null) {
            throw new IllegalStateException("A stone already exists on this square.");
        }
        else {
            this.stone = stone;
            this.rebuild();
        }
    }

    public void flip() {
        // stub for now
    }

    /**
     * Sets the size of all squares. Must be called once and only once.
     *
     * @param sz The size of the square in pixel.
     * @throws IllegalArgumentException If called again after the size has been set.
     */
    public static void setSquareSize(int sz) {
        if (squareSize != -1) {
            throw new IllegalStateException("setSquareSize() called after size is set.");
        }
        else {
            squareSize = sz;
        }
    }

    /**
     * Gets the size of the square.
     *
     * @return The size of the square.
     * @throws IllegalStateException If size is not set yet.
     */
    public static int getSquareSize() {
        if (squareSize == -1) {
            throw new IllegalStateException("Size is not set yet.");
        }
        else {
            return squareSize;
        }
    }
}
