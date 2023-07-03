package gui;

import javax.swing.*;
import java.awt.*;

import backend.Board;

public class StartupFrame extends JFrame {
    private JLabel title;
    private JPanel sizeSliderPanel;
    private JButton startButton;
    /** The horizontal size of the window in pixels */
    public final static int SIZE_X = 400;
    /** The vertical size of the window in pixels */
    public final static int SIZE_Y = 200;
    /** Minimum board size */
    private final static int SIZE_MIN = 4;
    /** Maximum board size */
    private final static int SIZE_MAX = 24;
    /** Default board size */
    private final static int SIZE_INITIAL = 8;

    public StartupFrame() {
        super("Othello");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        // place window in the middle of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (int) (screenSize.getWidth() - SIZE_X) / 2;
        int locationY = (int) (screenSize.getHeight() - SIZE_Y) / 2;
        this.setLocation(new Point(locationX, locationY));

        // Title label
        this.title = new JLabel("Othello");
        this.title.setAlignmentX(0.5f);
        Font titleFont = new Font(this.title.getFont().getName(), Font.BOLD, this.title.getFont().getSize() * 4);
        this.title.setFont(titleFont);
        this.add(this.title);

        // Empty space for spacing
        this.add(Box.createRigidArea(new Dimension(0, SIZE_Y / 12)));

        // Slider
        this.sizeSliderPanel = new JPanel();
        this.sizeSliderPanel.setLayout(new BoxLayout(this.sizeSliderPanel, BoxLayout.PAGE_AXIS));
        this.sizeSliderPanel.setAlignmentX(0.5f);
        JLabel sizeLabel = new JLabel("Board size: " + SIZE_INITIAL);
        sizeLabel.setAlignmentX(0.5f);
        JSlider sizeSlider = new JSlider(SIZE_MIN, SIZE_MAX, SIZE_INITIAL);
        sizeSlider.setSize(new Dimension((int) (SIZE_X * 0.8), 18));
        sizeSlider.setPreferredSize(new Dimension((int) (SIZE_X * 0.8), 18));
        sizeSlider.setMinimumSize(new Dimension((int) (SIZE_X * 0.8), 18));
        sizeSlider.setMaximumSize(new Dimension((int) (SIZE_X * 0.8), 18));
        sizeSlider.addChangeListener(e -> sizeLabel.setText("Board size: " + sizeSlider.getValue()));
        this.sizeSliderPanel.add(sizeLabel);
        this.sizeSliderPanel.add(sizeSlider);
        this.add(this.sizeSliderPanel);

        // Empty space for spacing
        this.add(Box.createRigidArea(new Dimension(0, SIZE_Y / 12)));

        // Start button
        this.startButton = new JButton("Start");
        this.startButton.setAlignmentX(0.5f);
        this.startButton.addActionListener(e -> startGame(((JSlider) this.sizeSliderPanel.getComponent(1)).getValue()));
        this.add(startButton);
    }

    public void startGame(int size) {
        Board.setSize(size);
        JFrame frame = new GameFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(GameFrame.SIZE_X, GameFrame.SIZE_Y);
        // place window in the middle of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (int) (screenSize.getWidth() - GameFrame.SIZE_X) / 2;
        int locationY = (int) (screenSize.getHeight() - GameFrame.SIZE_Y) / 2;
        frame.setLocation(new Point(locationX, locationY));
        frame.setTitle("Othello: " + size + " x " + size);
        frame.setResizable(false);
        this.setVisible(false);
        frame.setVisible(true);
        this.dispose();
    }
}
