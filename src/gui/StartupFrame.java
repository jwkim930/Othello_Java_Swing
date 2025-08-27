package gui;

import ai.*;
import backend.Board;
import listeners.DebugMouseListener;
import entities.Stone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

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
    public final static int SIZE_Y = 300;
    /** Minimum board size */
    private final static int SIZE_MIN = 4;
    /** Maximum board size */
    private final static int SIZE_MAX = 24;
    /** Default board size */
    private final static int SIZE_INITIAL = 8;
    /** If {@code true}, debug mode is enabled.
      * Click and hold the title text for three seconds to enable this. */
    private boolean debug = false;
    /** Specifies the game mode such as PvP and vs AI. */
    private String playOption;
    /** Specifies the AI model to be used as an opponent. */
    private String AIOpponent;
    /** The scale factor used for size scaling. */
    private static double scaleFactor;

    /**
     * Calculates the scale factor based on screen height compared to 1080p
     * and saves it to the static variable.
     * If the screen is vertical (i.e. longer vertically than horizontally),
     * then the width will be used instead.
     */
    public static void setScaleFactor() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        scaleFactor = Math.min(screenSize.getWidth(), screenSize.getHeight()) / 1080.0;
    }

    /**
     * Returns the scale factor used for size scaling.
     *
     * @return The scale factor that can be multiplied to scale any dimension.
     */
    public static double getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Scales a dimension value based on the screen scale factor.
     *
     * @param baseValue The value to be scaled.
     * @return The scaled value.
     */
    public static int scale(int baseValue) {
        return (int) (baseValue * getScaleFactor());
    }

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

        // Slider
        this.sizeSliderPanel = new JPanel();
        this.sizeSliderPanel.setLayout(new BoxLayout(this.sizeSliderPanel, BoxLayout.PAGE_AXIS));
        this.sizeSliderPanel.setAlignmentX(0.5f);
        JLabel sizeLabel = new JLabel("Board size: " + SIZE_INITIAL);
        sizeLabel.setAlignmentX(0.5f);
        // slider values are divided and then multiplied by 2 so that only even number can be used
        JSlider sizeSlider = new JSlider(SIZE_MIN / 2, SIZE_MAX / 2, SIZE_INITIAL / 2);
        int sliderWidth = (int) (SIZE_X * 0.8);
        int sliderHeight = 18;
        sizeSlider.setSize(new Dimension(sliderWidth, sliderHeight));
        sizeSlider.setPreferredSize(new Dimension(sliderWidth, sliderHeight));
        sizeSlider.setMinimumSize(new Dimension(sliderWidth, sliderHeight));
        sizeSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        sizeSlider.addChangeListener(e -> sizeLabel.setText("Board size: " + sizeSlider.getValue() * 2));
        this.sizeSliderPanel.add(sizeLabel);
        this.sizeSliderPanel.add(sizeSlider);

        // AI selection box
        JPanel AISelectionPanel = new JPanel();
        AISelectionPanel.setLayout(new BoxLayout(AISelectionPanel, BoxLayout.LINE_AXIS));
        JLabel AISelectorLabel = new JLabel("AI Opponent: ");
        AISelectionPanel.add(AISelectorLabel);
        AISelectorLabel.setEnabled(false);   // disabled since the default option is pvp
        String[] AIOptions = {"Randomazo", "Hastyn"};
        this.AIOpponent = "Randomazo";
        JComboBox<String> AISelector = new JComboBox<>(AIOptions);
        Dimension selectorSize = new Dimension(scale(200), scale(30));
        AISelector.setSize(selectorSize);
        AISelector.setPreferredSize(selectorSize);
        AISelector.setMaximumSize(selectorSize);
        AISelector.setMinimumSize(selectorSize);
        AISelector.addActionListener(e -> this.AIOpponent = (String) AISelector.getSelectedItem());
        AISelector.setEnabled(false);   // disabled since the default option is pvp
        AISelectionPanel.add(AISelector);

        // Play options
        JPanel playOptionPanel = new JPanel();
        playOptionPanel.setLayout(new BoxLayout(playOptionPanel, BoxLayout.LINE_AXIS));
        JCheckBox pvpCheckbox = new JCheckBox("PvP", true);
        JCheckBox vsAIFirstCheckbox = new JCheckBox("vs AI, move first", false);
        JCheckBox vsAILaterCheckbox = new JCheckBox("vs AI, move later", false);
        pvpCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                vsAIFirstCheckbox.setSelected(false);
                vsAIFirstCheckbox.setEnabled(true);
                vsAILaterCheckbox.setSelected(false);
                vsAILaterCheckbox.setEnabled(true);
                pvpCheckbox.setEnabled(false);
                this.playOption = "pvp";
                AISelectorLabel.setEnabled(false);
                AISelector.setEnabled(false);
            }
        });
        vsAIFirstCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pvpCheckbox.setSelected(false);
                pvpCheckbox.setEnabled(true);
                vsAILaterCheckbox.setSelected(false);
                vsAILaterCheckbox.setEnabled(true);
                vsAIFirstCheckbox.setEnabled(false);
                this.playOption = "random_first";
                AISelectorLabel.setEnabled(true);
                AISelector.setEnabled(true);
            }
        });
        vsAILaterCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                pvpCheckbox.setSelected(false);
                pvpCheckbox.setEnabled(true);
                vsAIFirstCheckbox.setSelected(false);
                vsAIFirstCheckbox.setEnabled(true);
                vsAILaterCheckbox.setEnabled(false);
                this.playOption = "random_later";
                AISelectorLabel.setEnabled(true);
                AISelector.setEnabled(true);
            }
        });
        this.playOption = "pvp";
        pvpCheckbox.setEnabled(false);
        playOptionPanel.add(pvpCheckbox);
        playOptionPanel.add(vsAIFirstCheckbox);
        playOptionPanel.add(vsAILaterCheckbox);

        // Start button
        this.startButton = new JButton("Start");
        this.startButton.setAlignmentX(0.5f);
        this.startButton.addActionListener(e -> startGame(((JSlider) this.sizeSliderPanel.getComponent(1)).getValue() * 2));

        // Place the elements and spacings
        this.add(this.title);
        this.add(Box.createRigidArea(new Dimension(0, SIZE_Y / 12)));
        this.add(this.sizeSliderPanel);
        this.add(Box.createRigidArea(new Dimension(0, SIZE_Y / 12)));
        this.add(playOptionPanel);
        this.add(Box.createRigidArea(new Dimension(0, SIZE_Y / 16)));
        this.add(AISelectionPanel);
        this.add(Box.createRigidArea(new Dimension(0, SIZE_Y / 16)));
        this.add(startButton);
    }

    /**
     * Initialize and display the main game frame.
     *
     * @param size The size of the game board.
     */
    public void startGame(int size) {
        // determine the AI player to use
        AIPlayer ai = null;
        if (this.playOption.equals("pvp")) {
            ai = new NoAI();
        }
        else {
            Stone aiStone;
            if (this.playOption.equals("random_first")) {
                aiStone = Stone.WHITE;
            }
            else {
                aiStone = Stone.BLACK;
            }
            switch (this.AIOpponent) {
                case "Randomazo" -> ai = new Randomazo(aiStone);
                case "Hastyn" -> ai = new Hastyn(aiStone);
            }
        }
        GameFrame.initialize(size, this.debug, ai);
        JFrame frame = GameFrame.getInstance();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        // if debug mode is enabled, open debug window
        if (this.debug) {
            DebugFrame.initialize();
            JFrame dFrame = DebugFrame.getInstance();
            dFrame.setTitle("Debug Menu");
            dFrame.setSize(DebugFrame.SIZE_X, DebugFrame.SIZE_Y);
            dFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            dFrame.setResizable(false);
            // place window to the right of the game window
            locationX += GameFrame.SIZE_X;
            dFrame.setLocation(new Point(locationX, locationY));
            dFrame.setVisible(true);
        }
        // place the 4 starting stones
        Board board = Board.getInstance();
        int topLeft = size / 2 - 1;
        board.getSquareAt(topLeft, topLeft).place(Stone.WHITE);
        board.getSquareAt(topLeft, topLeft + 1).place(Stone.BLACK);
        board.getSquareAt(topLeft + 1, topLeft).place(Stone.BLACK);
        board.getSquareAt(topLeft + 1, topLeft + 1).place(Stone.WHITE);

        this.dispose();
        // invoke AI to make a move
        ai.makeMove();
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
