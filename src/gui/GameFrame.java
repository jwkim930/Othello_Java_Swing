package gui;

import backend.Board;
import exceptions.SingletonAlreadyExistsException;
import exceptions.SingletonNotYetExistsException;
import backend.Stone;

import javax.swing.*;
import java.awt.*;

/**
 * The main window showing the game.
 * This class uses a singleton pattern. Use initialize() in the beginning
 * and getInstance() to access it.
 */
public class GameFrame extends JFrame implements Rebuildable {
    /**
     * The singleton instance of this class.
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
     * Shows the player to make the next move.
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
     * {@code true} if debug mode is enabled.
     */
    private static boolean debug;

    /**
     * Initializes the singleton instance of the game window.
     * This should be called only once in the beginning.
     *
     * @param boardSize The size of the game board.
     * @param dbg       If {@code true}, debug mode is enabled.
     * @throws SingletonAlreadyExistsException If it has already been initialized.
     */
    public static void initialize(int boardSize, boolean dbg) throws SingletonAlreadyExistsException {
        if (instance != null) {
            throw new SingletonAlreadyExistsException("The GameFrame has already been initialized.");
        }
        else {
            debug = dbg;
            instance = new GameFrame(boardSize);
        }
    }

    /**
     * Removes the singleton instance of this object so that it can be initialized again.
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Returns the singleton instance of the game window.
     *
     * @return The singleton instance of the game window.
     * @throws SingletonNotYetExistsException If it hasn't been initialized yet.
     */
    public static GameFrame getInstance() throws SingletonNotYetExistsException {
        if (instance == null) {
            throw new SingletonNotYetExistsException("The GameFrame has not been initialized yet.");
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
        this.turnIndicatorCircle = this.createIndicatorCircle(Board.getInstance().getTurn());

        // build and add bottom row
        this.bottomRowPanel = new JPanel();
        this.rebuild();
        this.add(this.bottomRowPanel);
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
        Board board = Board.getInstance();
        board.nextTurn();
        this.turnIndicatorCircle = this.createIndicatorCircle(board.getTurn());
        this.rebuild();
    }

    /**
     * Finishes the game, blocking access to the board and determining the winner.
     */
    private void finishGame() {
        Board board = Board.getInstance();

        // disable board panel and determine winner
        Stone winner;
        int whiteWinningCount = 0;
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                SquarePanel square = board.getSquareAt(row, col);
                square.setBackgroundColor(SquarePanel.MOUSE_ENTERED_COLOR);
                if (square.getStone() == null) {
                    if (board.isInteractable()) {
                        square.removeMouseListener(square.getMouseListeners()[0]);
                    }
                }
                else if (square.getStone().equals(Stone.WHITE)) {
                    whiteWinningCount++;
                }
                else {
                    whiteWinningCount--;
                }
            }
        }
        if (whiteWinningCount > 0) {
            winner = Stone.WHITE;
        }
        else if (whiteWinningCount < 0) {
            winner = Stone.BLACK;
        }
        else {
            winner = null;
        }

        this.bottomRowPanel.removeAll();
        // design the winner indicator
        JPanel winnerIndicator = new JPanel();
        winnerIndicator.setLayout(new BoxLayout(winnerIndicator, BoxLayout.LINE_AXIS));
        JLabel winnerLabel = new JLabel();
        winnerLabel.setFont(new Font(winnerLabel.getFont().getName(), Font.PLAIN, winnerLabel.getFont().getSize() * 2));
        if (winner == null) {
            // it's a draw
            winnerLabel.setText("The game is a draw!");
        }
        else {
            // someone won
            winnerIndicator.add(this.createIndicatorCircle(winner));
            winnerLabel.setText(" won the game!");
        }
        winnerIndicator.add(winnerLabel);

        // design the restart and exit buttons
        Dimension buttonDimension = new Dimension(0, 35);

        JButton restartButton = new JButton("Restart");
        restartButton.setSize(buttonDimension);
        restartButton.setPreferredSize(buttonDimension);
        restartButton.setMinimumSize(buttonDimension);
        restartButton.addActionListener(event -> this.restart());

        JButton exitButton = new JButton("Exit");
        exitButton.setSize(buttonDimension);
        exitButton.setPreferredSize(buttonDimension);
        exitButton.setMinimumSize(buttonDimension);
        exitButton.setMinimumSize(buttonDimension);
        exitButton.addActionListener(event -> this.dispose());

        // assemble the bottom row
        GroupLayout bottomRowLayout = new GroupLayout(this.bottomRowPanel);
        this.bottomRowPanel.setLayout(bottomRowLayout);
        bottomRowLayout.setHorizontalGroup(bottomRowLayout.createSequentialGroup()
                .addGap(270)
                .addComponent(winnerIndicator).addGap(93)
                .addComponent(restartButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitButton)
        );
        bottomRowLayout.setVerticalGroup(bottomRowLayout.createSequentialGroup()
                .addGroup(bottomRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(winnerIndicator)
                        .addComponent(restartButton)
                        .addComponent(exitButton)
                )
        );
        // if in debug mode, close debug window
        if (isDebugMode()) {
            DebugFrame.getInstance().dispose();
        }
    }

    /**
     * Creates a panel that shows a circle indicating the current turn.
     *
     * @param stone The stone of the current turn.
     * @return The JPanel that shows the circle.
     */
    private JPanel createIndicatorCircle(Stone stone) {
        JPanel result = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // draw a circle of the right color
                g.setColor(stone.getColor());
                g.fillOval(5, 5, 20, 20);
                g.setColor(Color.BLACK);
                g.drawOval(5, 5, 20, 20);
            }
        };
        Dimension circleSizeDimension = new Dimension(30, 30);
        result.setSize(circleSizeDimension);
        result.setPreferredSize(circleSizeDimension);
        result.setMinimumSize(circleSizeDimension);
        result.setMaximumSize(circleSizeDimension);

        return result;
    }

    /**
     * Restarts the game, opening up the initial setup window and closing this one.
     */
    private void restart() {
        GameFrame.reset();
        Board.reset();
        if (isDebugMode()) {
            DebugFrame.reset();
        }
        SquarePanel.resetSquareSize();
        JFrame frame = new StartupFrame();
        frame.setSize(StartupFrame.SIZE_X, StartupFrame.SIZE_Y);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        this.dispose();
    }

    /**
     * Tells whether debug mode is enabled or not.
     *
     * @return {@code true} if debug mode is enabled, and {@code false} otherwise.
     */
    public static boolean isDebugMode() {
        return debug;
    }
}
