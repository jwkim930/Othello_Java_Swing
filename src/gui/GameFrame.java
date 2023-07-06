package gui;

import backend.Board;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.swing.*;
import java.awt.*;

/**
 * The main window showing the game.
 * This class uses a singleton pattern. Use initialize() in the beginning
 * and getInstance() to access it.
 */
public class GameFrame extends JFrame implements Rebuildable {
    /**
     * The singleton instance of the object.
     */
    private static GameFrame instance;
    /**
     * The panel containing the board.
     */
    private BoardPanel boardPanel;
    /**
     * Contains all the turn indicator and the two buttons.
     */
    private JPanel bottomRowPanel;
    /**
     * Shows the player to move the next move.
     */
    private JPanel turnIndicatorCircle;
    /**
     * The width of the window in pixels.
     */
    public final static int SIZE_X = 800;
    /**
     * The height of the window in pixels.
     */
    public final static int SIZE_Y = 800;

    /**
     * Initializes the singleton instance of the game window.
     * This should be called only once in the beginning.
     *
     * @param boardSize The size of the game board.
     * @throws InstanceAlreadyExistsException If it has already been initialized.
     */
    public static void initialize(int boardSize) throws InstanceAlreadyExistsException {
        if (instance != null) {
            throw new InstanceAlreadyExistsException("The GameFrame has already been initialized.");
        }
        else {
            instance = new GameFrame(boardSize);
        }
    }

    /**
     * Returns the singleton instance of the game window.
     *
     * @return The singleton instance of the game window.
     * @throws InstanceNotFoundException If it hasn't been initialized yet.
     */
    public static GameFrame getInstance() throws InstanceNotFoundException {
        if (instance == null) {
            throw new InstanceNotFoundException("The GameFrame has not been initialized yet.");
        }
        else {
            return instance;
        }
    }

    /**
     * Initializes the game window. This should be called only once
     * in the beginning.
     *
     * @param boardSize The size of the game board.
     */
    private GameFrame(int boardSize) {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        // add an empty space above
        this.add(Box.createRigidArea(new Dimension(0, 30)));

        // add BoardPanel
        this.boardPanel = new BoardPanel(boardSize);
        this.add(this.boardPanel);

        // add an empty space below
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // design turn indicator circle
        this.turnIndicatorCircle = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // draw a circle of the right color
                Board board;
                try {
                    board = Board.getInstance();
                } catch (InstanceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                g.setColor(board.getTurn().getColor());
                g.fillOval(5, 5, 20, 20);
                g.setColor(Color.BLACK);
                g.drawOval(5, 5, 20, 20);
            }
        };
        Dimension circleSizeDimension = new Dimension(30, 30);
        this.turnIndicatorCircle.setSize(circleSizeDimension);
        this.turnIndicatorCircle.setPreferredSize(circleSizeDimension);
        this.turnIndicatorCircle.setMinimumSize(circleSizeDimension);
        this.turnIndicatorCircle.setMaximumSize(circleSizeDimension);

        // build and add bottom row
        this.bottomRowPanel = new JPanel();
        rebuild();
        this.add(bottomRowPanel);
    }

    public void rebuild() {
        // only the turn indicator need to be updated
        // update the entire bottom row for this
        this.bottomRowPanel.removeAll();

        // design the turn indicator
        JPanel turnIndicator = new JPanel();
        turnIndicator.setLayout(new BoxLayout(turnIndicator, BoxLayout.LINE_AXIS));
        JLabel turnLabel = new JLabel("Current turn:");
        turnLabel.setFont(new Font(turnLabel.getFont().getName(), Font.PLAIN, turnLabel.getFont().getSize() * 2));
        turnIndicator.add(turnLabel);
        turnIndicator.add(this.turnIndicatorCircle);

        // design skip and finish button
        Dimension buttonDimension = new Dimension(0, 35);

        JButton skipButton = new JButton("Skip");
        skipButton.setSize(buttonDimension);
        skipButton.setPreferredSize(buttonDimension);
        skipButton.setMinimumSize(buttonDimension);
        skipButton.addActionListener(event -> this.nextTurn());

        JButton finishButton = new JButton("Finish");
        finishButton.setSize(buttonDimension);
        finishButton.setPreferredSize(buttonDimension);
        finishButton.setMinimumSize(buttonDimension);
        finishButton.setMinimumSize(buttonDimension);
        finishButton.addActionListener(event -> finishGame());

        // assemble the bottom row
        GroupLayout bottomRowLayout = new GroupLayout(this.bottomRowPanel);
        this.bottomRowPanel.setLayout(bottomRowLayout);
        bottomRowLayout.setHorizontalGroup(bottomRowLayout.createSequentialGroup()
                .addGap(270)
                .addComponent(turnIndicator).addGap(150)
                .addComponent(skipButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(finishButton)
        );
        bottomRowLayout.setVerticalGroup(bottomRowLayout.createSequentialGroup()
                .addGroup(bottomRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(turnIndicator)
                        .addComponent(skipButton)
                        .addComponent(finishButton)
                )
        );
    }

    /**
     * Finish this turn and let the other player make their move.
     * This also refreshes the turn indicator.
     */
    public void nextTurn() {
        Board board;
        try {
            board = Board.getInstance();
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
        board.nextTurn();
        this.rebuild();
    }

    private void finishGame() {
        // stub for now
    }
}
