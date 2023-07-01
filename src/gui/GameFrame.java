package gui;

import backend.Board;
import backend.Stone;

import javax.swing.*;

public class GameFrame extends JFrame implements Rebuildable {
    private BoardPanel boardPanel;
    /**
     * The player of the current turn. Black takes the first turn, then it alternates.
     */
    private Stone turn;
    private JLabel turnIndicator;
    private JButton finishButton;
    public final static int SIZE_X = 800;
    public final static int SIZE_Y = 800;

    /**
     * Initializes the game window. This should be called only once
     * in the beginning.
     */
    public GameFrame() {
        this.turn = Stone.BLACK;
    }

    public void rebuild() {
        // stub for now
    }
}
