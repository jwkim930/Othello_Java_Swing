package listeners;

import backend.Board;
import backend.SquareBackgroundManager;
import entities.Stone;
import gui.DebugFrame;
import gui.GameFrame;
import gui.SquarePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * MouseListener used for SquarePanel. Makes the squares user-interactable.
 */
public class SquareMouseListener implements MouseListener {
    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     * For this application, this does nothing.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     * For this application, it only has a graphical change.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (GameFrame.isDebugMode() || SwingUtilities.isLeftMouseButton(e)) {
            SquarePanel square = (SquarePanel) e.getSource();
            square.getBackgroundManager().setCurrentColor(SquareBackgroundManager.MOUSE_CLICKED_COLOR);
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * A left click places the stone if it's a valid move, or flashes the
     * square otherwise. If debug mode is enabled, then a right click
     * places the stone regardless.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        SquarePanel square = (SquarePanel) e.getSource();
        Board board = Board.getInstance();
        int row = square.getCoordinate()[0], col = square.getCoordinate()[1];
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!board.placeStone(row, col)) {
                square.getBackgroundManager().invalidMoveFlash();
            }
        }
        else if (SwingUtilities.isRightMouseButton(e) && GameFrame.isDebugMode()) {
            DebugFrame frame = DebugFrame.getInstance();
            Stone stone = board.getTurn();
            square.place(stone);
            frame.addMoveHistory(stone, row, col, new int[0][2]);
            if (frame.shouldChangeTurn()) {
                GameFrame.getInstance().nextTurn();
            }
        }
    }

    /**
     * Invoked when the mouse enters a component.
     * For this application, it only has a graphical change.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        SquarePanel square = (SquarePanel) e.getSource();
        square.getBackgroundManager().setCurrentColor(SquareBackgroundManager.MOUSE_ENTERED_COLOR);
    }

    /**
     * Invoked when the mouse exits a component.
     * For this application, it only has a graphical change.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        SquarePanel square = (SquarePanel) e.getSource();
        Color squareColor = square.getBackgroundManager().getCurrentColor();
        if (squareColor.equals(SquareBackgroundManager.MOUSE_ENTERED_COLOR) || squareColor.equals(SquareBackgroundManager.MOUSE_CLICKED_COLOR)) {
            // square isn't flashing for invalid move
            square.getBackgroundManager().idleBackground();
        }
    }
}
