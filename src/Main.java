import gui.StartupFrame;

import javax.swing.*;

/**
 * Used for startup.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new StartupFrame();
        frame.setSize(StartupFrame.SIZE_X, StartupFrame.SIZE_Y);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
