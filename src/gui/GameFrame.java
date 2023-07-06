package gui;

import backend.Board;
import backend.Stone;

import javax.management.InstanceNotFoundException;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements Rebuildable {
    /**
     * The panel containing the board.
     */
    private BoardPanel boardPanel;
    /**
     * Shows the player to move the next move.
     */
    private JPanel turnIndicatorCircle;
    /**
     * When pressed, skip the turn.
     */
    private JButton skipButton;
    /**
     * When pressed, finishes the game and finds the winner.
     */
    private JButton finishButton;
    /**
     * Marks if the game is still being played. Set to false when it is finished.
     */
    private boolean playing;
    /**
     * The width of the window in pixels.
     */
    public final static int SIZE_X = 800;
    /**
     * The height of the window in pixels.
     */
    public final static int SIZE_Y = 800;

    /**
     * Initializes the game window. This should be called only once
     * in the beginning.
     *
     * @param boardSize The size of the game board.
     */
    public GameFrame(int boardSize) {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        // add an empty space above
        this.add(Box.createRigidArea(new Dimension(0, 30)));

        // add BoardPanel
        this.boardPanel = new BoardPanel(boardSize);
        this.add(this.boardPanel);

        // add an empty space below
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // design turn indicator
        JPanel turnIndicator = new JPanel();
        turnIndicator.setLayout(new BoxLayout(turnIndicator, BoxLayout.LINE_AXIS));
        JLabel turnLabel = new JLabel("Current turn:");
        turnLabel.setFont(new Font(turnLabel.getFont().getName(), Font.PLAIN, turnLabel.getFont().getSize() * 2));
        turnIndicator.add(turnLabel);
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
        turnIndicator.add(this.turnIndicatorCircle);

        // design skip and finish button
        Dimension buttonDimension = new Dimension(0, 35);
        this.skipButton = new JButton("Skip");
        this.skipButton.setSize(buttonDimension);
        this.skipButton.setPreferredSize(buttonDimension);
        this.skipButton.setMinimumSize(buttonDimension);
        this.skipButton.addActionListener(event -> this.nextTurn());

        this.finishButton = new JButton("Finish");
        this.finishButton.setSize(buttonDimension);
        this.finishButton.setPreferredSize(buttonDimension);
        this.finishButton.setMinimumSize(buttonDimension);
        this.finishButton.setMinimumSize(buttonDimension);
        this.finishButton.addActionListener(event -> this.playing = false);

        // add the bottom row
        JPanel bottomRowPanel = new JPanel();
        GroupLayout bottomRowLayout = new GroupLayout(bottomRowPanel);
        bottomRowPanel.setLayout(bottomRowLayout);
        bottomRowLayout.setHorizontalGroup(bottomRowLayout.createSequentialGroup()
                .addGap(270)
                .addComponent(turnIndicator).addGap(150)
                .addComponent(this.skipButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.finishButton)
        );
        bottomRowLayout.setVerticalGroup(bottomRowLayout.createSequentialGroup()
                .addGroup(bottomRowLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(turnIndicator)
                        .addComponent(skipButton)
                        .addComponent(finishButton)
                )
        );
        this.add(bottomRowPanel);

        this.playGame();
    }

    public void rebuild() {
        this.turnIndicatorCircle.repaint();
    }

    private void playGame() {
        // stub for now
    }

    private void nextTurn() {
        // stub for now
    }
}
