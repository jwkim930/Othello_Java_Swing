package gui;

import backend.Board;
import backend.MoveHistory;
import backend.MoveHistoryStack;
import backend.Stone;
import exceptions.SingletonAlreadyExistsException;
import exceptions.SingletonNotYetExistsException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

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
    public final static int SIZE_Y = 250;
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
     * When {@code true}, placing a stone does not change turn.
     */
    private boolean dontChangeTurn;
    /**
     * This records the stone selected to be placed (the one appears next to "Current Turn"
     * in the game window) when the previous button in the time machine is used.
     * This is cleared when you go to the latest move (either by next button or making a new move).
     */
    Stone lastStoneSelectedBefore;

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

        this.add(Box.createVerticalStrut(5));

        // Text instruction
        this.add(new JLabel("Right click to place a stone anywhere,"));
        this.add(new JLabel("regardless of whether it is a valid move."));

        this.add(Box.createVerticalGlue());

        // Turn indicator
        this.turnIndicator = new JLabel();
        this.updateTurnIndicator();
        this.turnIndicator.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(this.turnIndicator);
        // Don't change turn checkbox
        this.dontChangeTurn = false;
        JCheckBox dontChangeTurnCheck = new JCheckBox("Don't change turn after making a move");
        dontChangeTurnCheck.addActionListener(e -> {
            JCheckBox box = (JCheckBox) e.getSource();
            this.dontChangeTurn = box.isSelected();
        });
        this.add(dontChangeTurnCheck);

        this.add(Box.createVerticalGlue());

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
        showMoveCheck.addActionListener(event -> {
            JCheckBox box = (JCheckBox) event.getSource();
            this.showMove = box.isSelected();
            try {
                if (this.showMove) {
                    this.drawMoveHighlights();
                } else {
                    this.clearMoveHighlights();
                }
            }
            catch (NullPointerException e) {
                // Interacted with checkbox before history is initialized, ignore it.
            }
        });
        timeMachinePanel.add(prevButton);
        timeMachinePanel.add(nextButton);
        timeMachinePanel.add(showMoveCheck);
        timeMachinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(timeMachinePanel);

        this.add(Box.createVerticalGlue());

        // make random move button
        JPanel randomMovePanel = new JPanel();
        randomMovePanel.setLayout(new BoxLayout(randomMovePanel, BoxLayout.LINE_AXIS));
        randomMovePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton randomMoveButton = new JButton("Random Move");
        JLabel randomMoveResultLabel = new JLabel();
        BiConsumer<JLabel, String> showTextInLabelThenDisappear = (label, message) -> {
            Thread worker = new Thread(() -> {
                label.setText(message);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                label.setText("");
            });
            worker.start();
        };
        randomMoveButton.addActionListener(e -> {
            boolean success = this.makeRandomMove();
            if (success) {
                showTextInLabelThenDisappear.accept(randomMoveResultLabel, "Success");
            }
            else {
                showTextInLabelThenDisappear.accept(randomMoveResultLabel, "Fail");
            }
        });
        randomMovePanel.add(randomMoveButton);
        randomMovePanel.add(randomMoveResultLabel);
        this.add(randomMovePanel);

        this.add(Box.createVerticalGlue());

        // interaction toggle button
        JButton toggleInteractionButton = new JButton("Toggle Interaction");
        toggleInteractionButton.addActionListener(e -> Board.getInstance().toggleInteractable());
        this.add(toggleInteractionButton);

        this.add(Box.createVerticalStrut(5));
    }

    /**
     * Update the turn indicator to the current turn number.
     */
    private void updateTurnIndicator() {
        int currentTurn = this.moveHistories == null ? 1 : this.moveHistories.currentPosition() + 2;
        int totalTurn = this.moveHistories == null ? 1 : this.moveHistories.size() + 1;
        this.turnIndicator.setText("Current Turn count: " + currentTurn + "/" + totalTurn);
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
        this.lastStoneSelectedBefore = null;
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

        MoveHistory history = this.moveHistories.current();
        if (history != null) {
            // not looking at the state before the first move, there highlights to draw
            Board board = Board.getInstance();
            int[] placeCoor = history.getLocation();
            board.getSquareAt(placeCoor[0], placeCoor[1]).setIdleColor(stonePlaced);
            for (int[] flipCoor : history.getFlipped()) {
                board.getSquareAt(flipCoor[0], flipCoor[1]).setIdleColor(stoneFlipped);
            }
        }
    }

    /**
     * Removes highlights showing the move.
     */
    private void clearMoveHighlights() {
        MoveHistory history = this.moveHistories.current();
        if (history != null) {
            // not looking at the state before the first move, there are highlights to remove
            Board board = Board.getInstance();
            int[] placeCoor = history.getLocation();
            board.getSquareAt(placeCoor[0], placeCoor[1]).setIdleColor(SquarePanel.getDefaultBackgroundColor());
            for (int[] flipCoor : history.getFlipped()) {
                board.getSquareAt(flipCoor[0], flipCoor[1]).setIdleColor(SquarePanel.getDefaultBackgroundColor());
            }
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
            boolean changeTurn;
            if (next) {
                MoveHistory move = this.moveHistories.next();
                Stone stone = move.getStone();
                int[] placeCoor = move.getLocation();
                board.getSquareAt(placeCoor[0], placeCoor[1]).place(stone);
                for (int[] flipCoor : move.getFlipped()) {
                    board.getSquareAt(flipCoor[0], flipCoor[1]).flip();
                }
                // peek into the next move to see what stone was placed in it
                if (!this.moveHistories.atLastMove()) {
                    changeTurn = !this.moveHistories.next().getStone().equals(board.getTurn());
                    this.moveHistories.previous();
                }
                else {
                    changeTurn = !this.lastStoneSelectedBefore.equals(board.getTurn());
                    this.lastStoneSelectedBefore = null;
                }
            } else {
                Stone stoneOnScreen = board.getTurn();
                if (this.moveHistories.atLastMove()) {
                    this.lastStoneSelectedBefore = stoneOnScreen;
                }
                MoveHistory move = this.moveHistories.current();
                changeTurn = !move.getStone().equals(stoneOnScreen);
                int[] placeCoor = move.getLocation();
                board.getSquareAt(placeCoor[0], placeCoor[1]).remove();
                for (int[] flipCoor : move.getFlipped()) {
                    board.getSquareAt(flipCoor[0], flipCoor[1]).flip();
                }
                this.moveHistories.previous();
            }
            if (changeTurn) {
                GameFrame.getInstance().nextTurn();
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
     * Tells whether the debug window is telling the application
     * to change turn upon making a move.
     *
     * @return {@code true} if the turn should be changed (i.e. default
     *         behaviour), and {@code false} otherwise.
     */
    public boolean shouldChangeTurn() {
        return !dontChangeTurn;
    }

    /**
     * Makes a random move, placing the current stone in a valid square.
     * If no stone can be placed, this does nothing.
     *
     * @return {@code true} if a stone was placed, {@code false} otherwise.
     */
    public boolean makeRandomMove() {
        int size = Board.getInstance().getSize();
        // create a list of coordinates, then shuffle order to simulate random choices
        List<int[]> coordinates = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int[] ar = {row, col};
                coordinates.add(ar);
            }
        }
        Collections.shuffle(coordinates);
        // go through the list and make attempts
        for (int[] coor : coordinates) {
            int row = coor[0], col = coor[1];
            // if attempt was successful, end loop
            if (Board.getInstance().placeStone(row, col)) {
                return true;
            }
        }
        // no attempt was successful
        return false;
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
