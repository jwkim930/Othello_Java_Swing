package gui;

import backend.Board;
import backend.Stone;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements Rebuildable {
    /**
     * The panel containing the board.
     */
    private BoardPanel boardPanel;
    /**
     * The player of the current turn. Black takes the first turn, then it alternates.
     */
    private Stone turn;
    /**
     * Shows the player to move the next move.
     */
    private JPanel turnIndicatorCircle;
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
     */
    public GameFrame() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        // First turn is black
        this.turn = Stone.BLACK;

        // add BoardPanel
        this.boardPanel = new BoardPanel();
        this.add(this.boardPanel);

        // add turn indicator
        JPanel turnIndicator = new JPanel();
        turnIndicator.setLayout(new BoxLayout(turnIndicator, BoxLayout.LINE_AXIS));
        turnIndicator.add(new JLabel("Current turn:"));
        this.turnIndicatorCircle = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // draw a circle of the right color
                Color color = turn.equals(Stone.BLACK) ? Color.BLACK : Color.WHITE;
                g.setColor(color);
                g.fillOval(0, 0, 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(0, 0, 10, 10);
            }
        };
        turnIndicator.add(this.turnIndicatorCircle);

        // add finish button
        this.finishButton = new JButton("Finish");
        this.finishButton.addActionListener(event -> {
            this.playing = false;
        });

        this.playGame();
    }

    public void rebuild() {
        // stub for now
    }

    private void playGame() {
        // stub for now
    }
}
