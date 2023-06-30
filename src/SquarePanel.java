import javax.swing.*;

public class SquarePanel extends JPanel {
    private int stone;

    public SquarePanel() {
        this.stone = Board.NO_STONE;
        this.addMouseListener(new SquareMouseListener());
        // fill more
    }
}
