package gui;

import backend.DebugMouseListener;

import javax.swing.*;
import java.awt.*;

/**
 * A window responsible for the initial setup. The board size is
 * determined in this window.
 */
public class StartupFrame extends JFrame {
    /**
     * A {@code Component} that displays the title text.
     */
    private JLabel title;
    /**
     * A {@code Component} that displays the slider used to configure
     * the board size.
     */
    private JPanel sizeSliderPanel;
    /**
     * A {@code Component} that initializes the board upon being pressed.
     */
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
    /** If {@code true}, debug mode is enabled.
      * Click and hold the title text for three seconds to enable this. */
    private boolean debug = false;

    /**
     * Initializes the startup window.
     */
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
        // add debug mode listener
        this.title.addMouseListener(new DebugMouseListener());
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

    /**
     * Initialize and display the main game frame.
     *
     * @param size The size of the game board.
     */
    public void startGame(int size) {
        GameFrame.initialize(size, this.debug);
        JFrame frame = GameFrame.getInstance();
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

    /**
     * Enables debug mode. This makes the title text green.
     */
    public void enableDebugMode() {
        this.debug = true;
        System.out.println("Debug mode is enabled.");
        this.title.setForeground(Color.green);
    }
}
