package gui;

import backend.SquareMouseListener;
import backend.Stone;

import javax.swing.*;
import java.awt.*;

public class SquarePanel extends JPanel implements Rebuildable {
    /**
     * The stone on this square. {@code null} if there is no stone.
     */
    private Stone stone;
    /**
     * The size of this square in pixels. It is -1 before it is set.
     */
    private static int squareSize = -1;
    /**
     * The color of the background of the square. This changes upon
     * being interacted.
     */
    private Color backgroundColor;
    /**
     * The mouse listener to be associated with this square.
     * Added upon creation, removed upon placing a stone.
     */
    private static final SquareMouseListener LISTENER = new SquareMouseListener();
    /**
     * The coordinate of this square on the board. Uses the format [row, col].
     */
    private int[] coordinate;

    /**
     * Initializes a SquarePanel. There is no stone on it initially.
     *
     * @param row The row of the square.
     * @param col The column of the square.
     */
    public SquarePanel(int row, int col) {
        super();
        int size = getSquareSize();
        Dimension sizeDimension = new Dimension(size, size);
        this.setSize(sizeDimension);
        this.setPreferredSize(sizeDimension);
        this.setMinimumSize(sizeDimension);
        this.setMaximumSize(sizeDimension);
        this.addMouseListener(LISTENER);
        this.backgroundColor = Color.LIGHT_GRAY;
        this.coordinate = new int[] {row, col};
    }

    @Override
    public void paintComponent(Graphics g) {
        int size = getSquareSize();
        // draw the square
        g.setColor(this.backgroundColor);
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
        this.repaint();
    }

    /**
     * Returns the stone on this square.
     *
     * @return The Stone on this square. If there is no stone, this will be {@code null}.
     */
    public Stone getStone() {
        return this.stone;
    }

    /**
     * Places the stone and refreshes the panel.
     * Once a stone is placed, the square no longer reacts to the mouse.
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
            this.removeMouseListener(LISTENER);
            this.setBackgroundColor(Color.LIGHT_GRAY);
            this.rebuild();
        }
    }

    /**
     * Replaces the stone on this square with the opposite stone.
     * @throws IllegalStateException If there is no stone on the square.
     */
    public void flip() {
        if (this.stone == null) {
            throw new IllegalStateException("No stone exists on this square.");
        }
        else {
            this.stone = this.stone.getOpposite();
            this.rebuild();
        }
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

    /**
     * Gets the current background color of this square.
     *
     * @return The background color of this square.
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the background color of this square.
     * This method does not repaint the square.
     *
     * @param color The color to be used as the background.
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * Flashes the square to indicate that the move was invalid.
     */
    public void invalidMoveFlash() {
        setBackgroundColor(new Color(230, 69, 69));
        this.rebuild();
        Thread setColorBack = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.setBackgroundColor(Color.LIGHT_GRAY);
            this.rebuild();
        });
        setColorBack.start();
    }

    /**
     * Returns the coordinate of this square.
     *
     * @return The coordinate of this square as [row, col].
     */
    public int[] getCoordinate() {
        return this.coordinate;
    }
}
