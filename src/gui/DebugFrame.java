package gui;

import exceptions.SingletonAlreadyExistsException;
import exceptions.SingletonNotYetExistsException;

import javax.swing.*;
import java.awt.*;

/**
 * Window containing menu for the debug features.
 * This uses a singleton pattern. Use initialize() in the beginning
 * and getInstance() to access it.
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
     * Records how many turns have passed since the beginning.
     */
    private int currentTurn;
    /**
     * Shows how many turns have passed since the beginning.
     */
    private JLabel turnIndicator;

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
        this.currentTurn = 1;
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
        prevButton.addActionListener(e -> this.subTurn());
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> this.addTurn());
        JCheckBox showMoveCheck = new JCheckBox("Show movements");
        timeMachinePanel.add(prevButton);
        timeMachinePanel.add(nextButton);
        timeMachinePanel.add(showMoveCheck);
        timeMachinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(timeMachinePanel);
    }

    /**
     * Add 1 to the current turn number in the debug window.
     */
    public void addTurn() {
        this.currentTurn++;
        this.updateTurnIndicator();
    }

    /**
     * Subtract 1 from the current turn number in the debug window.
     */
    public void subTurn() {
        this.currentTurn--;
        this.updateTurnIndicator();
    }

    /**
     * Update the turn indicator to the current turn number.
     */
    private void updateTurnIndicator() {
        this.turnIndicator.setText("Current Turn: " + this.currentTurn);
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
