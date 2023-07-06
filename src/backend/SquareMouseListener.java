package backend;

import gui.GameFrame;
import gui.SquarePanel;

import javax.management.InstanceNotFoundException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
        SquarePanel square = (SquarePanel) e.getSource();
        square.setBackgroundColor(Color.DARK_GRAY);
        square.rebuild();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * This places the stone if it's a valid move, or flashes the
     * square otherwise.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Board board;
        try {
            board = Board.getInstance();
        } catch (InstanceNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        SquarePanel square = (SquarePanel) e.getSource();
        int row = square.getCoordinate()[0], col = square.getCoordinate()[1];
        Stone stone = board.getTurn();
        Direction[] flippableDirections = board.getFlippingDirections(stone, row, col);
        if (flippableDirections.length == 0) {
            square.invalidMoveFlash();
        }
        else {
            // place stone and flip appropriately
            board.placeStone(stone, row, col);
            for (Direction dir : flippableDirections) {
                // getFlippingDirections() already ensures all stones in the direction are flippable
                for (int[] coor = dir.moveThisWay(row, col);
                  board.getSquareAt(coor[0], coor[1]).getStone().equals(stone.getOpposite());
                  coor = dir.moveThisWay(coor[0], coor[1])) {
                    board.flipAt(coor[0], coor[1]);
                }
            }
            try {
                GameFrame.getInstance().nextTurn();
            } catch (InstanceNotFoundException ex) {
                throw new RuntimeException(ex);
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
        square.setBackgroundColor(Color.GRAY);
        square.rebuild();
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
        Color squareColor = square.getBackgroundColor();
        if (squareColor.equals(Color.GRAY) || squareColor.equals(Color.DARK_GRAY)) {
            // square isn't flashing for invalid move
            square.setBackgroundColor(Color.LIGHT_GRAY);
            square.rebuild();
        }
    }
}
