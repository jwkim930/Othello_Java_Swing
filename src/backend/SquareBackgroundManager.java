package backend;

import gui.GameFrame;
import gui.SquarePanel;

import java.awt.*;

/**
 * Controls the background color of the square.
 */
public class SquareBackgroundManager {
    /**
     * The {@code SquarePanel} object associated with this object.
     */
    private SquarePanel square;
    /**
     * The current color of the background of the square.
     * This changes upon interaction.
     */
    private Color backgroundColor;
    /**
     * The default background color of the square when not interacting
     * with anything. This should be set only once per playthrough.
     */
    private static Color defaultIdleColor;
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
     * The color to be used when an invalid move is made.
     */
    public static final Color INVALID_MOVE_COLOR = new Color(230, 69, 69);
    /**
     * Background color to be used when not interacting with anything.
     */
    public static final Color DEFAULT_IDLE_COLOR = Color.LIGHT_GRAY;
    /**
     * Background color to be used when not interacting with anything
     * and in debug mode.
     */
    public static final Color DEBUG_IDLE_COLOR = new Color(143, 204, 143);

    public SquareBackgroundManager(SquarePanel square) {
        this.square = square;
        defaultIdleColor = GameFrame.isDebugMode() ? DEBUG_IDLE_COLOR : DEFAULT_IDLE_COLOR;
        this.currentIdleColor = defaultIdleColor;
        this.backgroundColor = this.currentIdleColor;
    }

    /**
     * Gets the current background color of this square.
     *
     * @return The background color of this square.
     */
    public Color getCurrentColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the background color of this square.
     * This also redraws the square to show the new color.
     *
     * @param color The color to be used as the background.
     */
    public void setCurrentColor(Color color) {
        this.backgroundColor = color;
        this.square.rebuild();
    }

    /**
     * Sets the background color to its idle background color.
     * This also redraws the square to show the change.
     */
    public void idleBackground() {
        this.setCurrentColor(this.currentIdleColor);
    }

    /**
     * Sets the color to be used for idle squares.
     * This also redraws the square to show the change.
     *
     * @param color The color to be used as the idle color.
     */
    public void setIdleColor(Color color) {
        this.currentIdleColor = color;
        this.idleBackground();
    }

    /**
     * Returns the default idle background color to be used.
     *
     * @return The default idle color to be used.
     */
    public static Color getDefaultIdleColor() {
        return defaultIdleColor;
    }

    /**
     * Flashes the square to indicate that the move was invalid.
     */
    public void invalidMoveFlash() {
        this.setCurrentColor(INVALID_MOVE_COLOR);
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
}
