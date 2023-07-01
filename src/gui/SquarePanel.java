package gui;

import backend.Board;
import backend.Direction;
import backend.SquareMouseListener;

import javax.swing.*;
import java.util.TreeMap;

public class SquarePanel extends JPanel implements Rebuildable {
    private int stone;
    TreeMap<Direction, SquarePanel> adjacent;

    public SquarePanel() {
        this.stone = Board.NO_STONE;
        this.addMouseListener(new SquareMouseListener());
        // fill more
    }

    public void rebuild() {
        // stub for now
    }
}
