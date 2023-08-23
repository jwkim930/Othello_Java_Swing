package gui;

import backend.Board;
import backend.MoveHistory;
import backend.MoveHistoryStack;
import backend.Stone;
import exceptions.SingletonAlreadyExistsException;
import exceptions.SingletonNotYetExistsException;

import javax.swing.*;
import java.awt.*;

/**
 * Window containing menu for the debug features.
 * This uses a singleton pattern. Use initialize() in the beginning
 * and getInstance() to access it.
 * Methods in this class assume that debug mode is enabled.
 */
public class DebugFrame extends JFrame {
    /**
     * The singleton instance of this class.
     */
    private static DebugFrame instance;
    /**
     * The width of the window in pixels.
     */
    public final static int SIZE_X = 300;
    /**
     * The height of the window in pixels.
     */
    public final static int SIZE_Y = 120;
    /**
     * Shows how many turns have passed since the beginning.
     */
    private JLabel turnIndicator;
    /**
     * When {@code true}, where the stone was placed and what stones were flipped
     * in the last move will be highlighted.
     */
    private boolean showMove;
    /**
     * Records the history of all moves made.
     */
    private MoveHistoryStack moveHistories;

    /**
     * Initializes the singleton instance of the debug window.
     * This should be called only once in the beginning.
     *
     * @throws SingletonAlreadyExistsException If it has already been initialized.
     */
    public static void initialize() throws SingletonAlreadyExistsException {
        if (instance != null) {
            throw new SingletonAlreadyExistsException("The DebugFrame has already been initialized.");
        }
        else {
            instance = new DebugFrame();
        }
    }

    /**
     * Returns the singleton instance of the debug window.
     *
     * @return The instance of the debug window.
     * @throws SingletonNotYetExistsException If the debug window has not been initialized yet.
     */
    public static DebugFrame getInstance() throws SingletonNotYetExistsException {
        if (instance == null) {
            throw new SingletonNotYetExistsException("The DebugFrame has not been initialized yet.");
        }
        else {
            return instance;
        }
    }

    /**
     * Removes the singleton instance of this object so that it can be initialized again.
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Instantiates an object instance. Things such as size, location, visibility,
     * exit behaviour, and title are left as default.
     */
    private DebugFrame() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        // Turn indicator
        this.turnIndicator = new JLabel();
        this.updateTurnIndicator();
        this.turnIndicator.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(this.turnIndicator);

        this.add(Box.createRigidArea(new Dimension(0, 10)));

        // Time machine
        this.add(new JLabel("Time Machine:"));
        JPanel timeMachinePanel = new JPanel();
        timeMachinePanel.setLayout(new BoxLayout(timeMachinePanel, BoxLayout.LINE_AXIS));
        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> {
            this.goToAdjacentMove(false);
            this.updateTurnIndicator();
        });
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {
            this.goToAdjacentMove(true);
            this.updateTurnIndicator();
        });
        JCheckBox showMoveCheck = new JCheckBox("Show movements");
        this.showMove = false;
        showMoveCheck.addActionListener(e -> {
            JCheckBox box = (JCheckBox) e.getSource();
            this.showMove = box.isSelected();
            if (this.showMove) {
                this.drawMoveHighlights();
            }
            else {
                this.clearMoveHighlights();
            }
        });
        timeMachinePanel.add(prevButton);
        timeMachinePanel.add(nextButton);
        timeMachinePanel.add(showMoveCheck);
        timeMachinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(timeMachinePanel);
    }

    /**
     * Update the turn indicator to the current turn number.
     */
    private void updateTurnIndicator() {
        int currentTurn = this.moveHistories == null ? 1 : this.moveHistories.currentPosition() + 2;
        int totalTurn = this.moveHistories == null ? 1 : this.moveHistories.size() + 1;
        this.turnIndicator.setText("Current Turn: " + currentTurn + "/" + totalTurn);
    }

    /**
     * Adds a move to the move history. If it is the first move, the history stack is initialized.
     *
     * @param stone   The stone placed in the move.
     * @param row     The row (or y-coordinate) of where the stone was placed. Start from 0.
     * @param col     The column (or x-coordinate) of where the stone was placed. Start from 0.
     * @param flipped The coordinates (array of 2 integers as [row, col]) of where the stones
     *                were flipped from this move.
     */
    public void addMoveHistory(Stone stone, int row, int col, int[][] flipped) {
        if (this.moveHistories == null) {
            this.moveHistories = new MoveHistoryStack(stone, row, col, flipped);
            if (this.showMove) {
                this.drawMoveHighlights();
            }
        }
        else {
            if (this.showMove) {
                this.clearMoveHighlights();
            }
            this.moveHistories.push(stone, row, col, flipped);
            if (this.showMove) {
                this.drawMoveHighlights();
            }
        }
        this.updateTurnIndicator();
    }

    /**
     * Changes background color of squares to mark where the stone was placed
     * and which stones were flipped according to the current move history
     * being viewed.
     *
     * Where the stone is placed will be marked blue,
     * and where the stones were flipped will be marked cyan.
     *
     * @throws IllegalStateException If the checkbox for showing move is unchecked.
     */
    private void drawMoveHighlights() {
        if (!this.showMove) {
            throw new IllegalStateException("Call to highlight drawer when move is not being shown.");
        }
        Color stonePlaced = new Color(143, 173, 204);
        Color stoneFlipped = new Color(143, 204, 204);

        Board board = Board.getInstance();
        MoveHistory history = this.moveHistories.current();
        int[] placeCoor = history.getLocation();
        board.getSquareAt(placeCoor[0], placeCoor[1]).setIdleColor(stonePlaced);
        for (int[] flipCoor : history.getFlipped()) {
            board.getSquareAt(flipCoor[0], flipCoor[1]).setIdleColor(stoneFlipped);
        }
    }

    /**
     * Removes highlights showing the move.
     */
    private void clearMoveHighlights() {
        Board board = Board.getInstance();
        MoveHistory history = this.moveHistories.current();
        int[] placeCoor = history.getLocation();
        board.getSquareAt(placeCoor[0], placeCoor[1]).setIdleColor(SquarePanel.getDefaultBackgroundColor());
        for (int[] flipCoor : history.getFlipped()) {
            board.getSquareAt(flipCoor[0], flipCoor[1]).setIdleColor(SquarePanel.getDefaultBackgroundColor());
        }
    }

    /**
     * Shows the move before/after the current move.
     * This moves the current position in move history stack.
     * This method does nothing if there is no next/previous move
     * in the stack.
     *
     * @param next {@code true} if going to the move after the current move,
     *             {@code false} if going to the move before.
     */
    private void goToAdjacentMove(boolean next) {
        if (this.showMove) {
            this.clearMoveHighlights();
        }
        Board board = Board.getInstance();
        try {
            if (next) {
                MoveHistory move = this.moveHistories.next();
                Stone stone = move.getStone();
                int[] placeCoor = move.getLocation();
                board.getSquareAt(placeCoor[0], placeCoor[1]).place(stone);
                for (int[] flipCoor : move.getFlipped()) {
                    board.getSquareAt(flipCoor[0], flipCoor[1]).flip();
                }
            } else {
                MoveHistory move = this.moveHistories.current();
                int[] placeCoor = move.getLocation();
                board.getSquareAt(placeCoor[0], placeCoor[1]).remove();
                for (int[] flipCoor : move.getFlipped()) {
                    board.getSquareAt(flipCoor[0], flipCoor[1]).flip();
                }
                this.moveHistories.previous();
            }
        }
        catch (NullPointerException e) {
            // next/previous move does not exist, do nothing
        }
        if (this.showMove) {
            this.drawMoveHighlights();
        }
        this.updateTurnIndicator();
    }

    /**
     * Spawns a debug window. This is mostly for testing the layout.
     */
    public static void main(String[] args) {
        DebugFrame.initialize();
        DebugFrame frame = DebugFrame.getInstance();
        frame.setTitle("Debug Menu");
        frame.setSize(DebugFrame.SIZE_X, DebugFrame.SIZE_Y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
