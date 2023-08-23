package gui;

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
            this.updateBoard(this.moveHistories.previous(), false);
            this.updateTurnIndicator();
        });
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {
            this.updateBoard(this.moveHistories.next(), true);
            this.updateTurnIndicator();
        });
        JCheckBox showMoveCheck = new JCheckBox("Show movements");
        this.showMove = false;
        showMoveCheck.addActionListener(e -> {
            JCheckBox box = (JCheckBox) e.getSource();
            this.showMove = box.isSelected();
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
        }
        else {
            this.moveHistories.push(stone, row, col, flipped);
        }
    }

    /**
     * Make the board show a new state in the history.
     *
     * @param move The movement made.
     * @param next {@code true} if going forward in movement (that is,
     *             the stone should be placed) and {@code false} if going backward
     *             (that is, the stone should be removed).
     */
    public void updateBoard(MoveHistory move, boolean next) {
        // stub for now
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
