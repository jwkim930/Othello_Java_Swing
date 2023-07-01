package gui;

import backend.Direction;
import backend.SquareMouseListener;
import backend.Stone;

import javax.swing.*;
import java.util.TreeMap;

public class SquarePanel extends JPanel implements Rebuildable {
    /**
     * The stone on this square. null if there is no stone.
     */
    private Stone stone;
    TreeMap<Direction, SquarePanel> adjacent;

    public SquarePanel() {
        this.addMouseListener(new SquareMouseListener());
        // fill more
    }

    public void rebuild() {
        // stub for now
    }

    public Stone getStone() {
        return this.stone;
    }

    public SquarePanel getAdjacent(Direction dir) {
        // stub for now
        return new SquarePanel();
    }

    public void place(Stone stone) {
        // stub for now
    }

    public void flip() {
        // stub for now
    }
}
