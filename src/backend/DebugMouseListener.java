package backend;

import gui.StartupFrame;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * MouseListener used for enabling debug mode in the startup screen.
 */
public class DebugMouseListener implements MouseListener {
    private Thread timer;

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
     * When invoked, the program starts counting 3 seconds, then enabled debug mode.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        this.timer = new Thread(() -> {
            try {
                System.out.println("Hold for 3 seconds to enable debug mode...");
                Thread.sleep(3000);
                JLabel title = (JLabel) e.getSource();
                StartupFrame frame = (StartupFrame) title.getTopLevelAncestor();
                frame.enableDebugMode();
            } catch (InterruptedException ex) {
                System.out.println("Debug mode canceled.");
            }
        });
        this.timer.start();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * When invoked, it interrupts the debug mode countdown.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        this.timer.interrupt();
        this.timer = null;
    }

    /**
     * Invoked when the mouse enters a component.
     * For this application, this does nothing.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     * For this application, this does nothing.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}
