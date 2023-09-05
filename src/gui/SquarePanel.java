package gui;

import backend.Board;
import backend.Direction;
import backend.SquareMouseListener;
import backend.Stone;

import javax.swing.*;
import java.awt.*;

import static gui.GameFrame.isDebugMode;

/**
 * An interactable panel that represents a square on the board.
 * May contain a stone. It is no longer (directly) interactable
 * if there is a stone.
 */
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
     * The coordinate of this square on the board. Uses the format [row, col].
     */
    private int[] coordinate;
    /**
     * The background color of the square when not interacting with anything.
     * In regular game play, this is light gray. In debug mode, it's light green.
     */
    private static Color defaultBackgroundColor;
    /**
     * The current background color of the square when not interacting with anything.
     * "Not interacting" does not include the square flashing from invalid move.
     * Changed in debug mode to show move history.
     */
    private Color currentIdleColor;
    /**
     * Background color to be used when the cursor enters the square.
     */
    public static final Color MOUSE_ENTERED_COLOR = Color.GRAY;
    /**
     * Background color to be used when the square is clicked.
     */
    public static final Color MOUSE_CLICKED_COLOR = Color.DARK_GRAY;

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
        this.addMouseListener(new SquareMouseListener());
        defaultBackgroundColor = isDebugMode() ? new Color(143, 204, 143) : Color.LIGHT_GRAY;
        this.currentIdleColor = defaultBackgroundColor;
        this.backgroundColor = this.currentIdleColor;
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
            if (Board.getInstance().isInteractable()) {
                // placed by a player
                this.removeMouseListener(this.getMouseListeners()[0]);
                this.idleBackground();
            }
        }
    }

    /**
     * Removes the stone on this square and refreshes the panel.
     * This re-enables this square for user interaction.
     * This is only intended to be called in debug mode.
     *
     * @throws IllegalStateException If the square doesn't have a stone.
     */
    public void remove() {
        if (this.stone == null) {
            throw new IllegalStateException("No stone exists on this square.");
        }
        else {
            this.stone = null;
            this.addMouseListener(new SquareMouseListener());
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
     * Unsets the square size so that it can be set again.
     */
    public static void resetSquareSize() {
        squareSize = -1;
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
     * This also redraws the square to show the new color.
     *
     * @param color The color to be used as the background.
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        this.rebuild();
    }

    /**
     * Flashes the square to indicate that the move was invalid.
     */
    public void invalidMoveFlash() {
        setBackgroundColor(new Color(230, 69, 69));
        Thread setColorBack = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.idleBackground();
        });
        setColorBack.start();
    }

    /**
     * Returns the coordinate of this square.
     *
     * @return The coordinate of this square as [row, col].
     */
    public int[] getCoordinate() {
        return this.coordinate.clone();
    }

    /**
     * Returns the SquarePanel that is adjacent to this SquarePanel in the specified direction.
     *
     * @param dir The direction to move towards.
     * @return The adjacent SquarePanel in the direction, {@code null} if it is out of board.
     */
    public SquarePanel getAdjacent(Direction dir) {
        int row = this.coordinate[0], col = this.coordinate[1];
        int[] result = dir.moveThisWay(row, col);
        if (result != null) {
            return Board.getInstance().getSquareAt(result[0], result[1]);
        }
        else {
            return null;
        }
    }

    /**
     * Sets the background color to its idle background color.
     * This also redraws the square to show the change.
     */
    public void idleBackground() {
        this.setBackgroundColor(this.currentIdleColor);
    }

    /**
     * Sets the color to be used for idle squares.
     * This also redraws the square to show the change.
     *
     * @param color The color to be used as the idle color.
     */
    public void setIdleColor(Color color) {
        this.currentIdleColor = color;
        idleBackground();
    }

    /**
     * Returns the default idle background color to be used.
     *
     * @return The default idle color to be used.
     */
    public static Color getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }
}
