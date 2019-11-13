
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Candidate 164656
 * @date Apr 2017
 */
public class Minesweeper {

    private JFrame frame;
    private JMenuBar menubar;
    private JPanel[][] tile;
    private JLabel[][] tileLabel;
    private JLabel minesLeft, timePlayed;
    private Timer timer;
    private Minefield minefield;
    private final ImageIcon flagIcon = new ImageIcon("flag.png");
    private final ImageIcon mineIcon = new ImageIcon("mine.png");
    private String time, difficulty = "Beginner";
    private boolean gameover, timerRunning;
    private int marked;

    /**
     *
     * @param rows number of rows in minefield
     * @param cols number of rows in minefield
     * @param mines number of mines in minefield New instance of Minesweeper
     * creates a new interface with a new minefield
     */
    public Minesweeper(int rows, int cols, int mines) {

        createMenu();
        createNewGame(rows, cols, mines);
    }

    /**
     * Creates the menu for the game, containing settings the user can change
     */
    private void createMenu() {

        JMenu game = new JMenu("Game");
        JMenu help = new JMenu("Help");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem quit = new JMenuItem("Quit");
        JMenuItem howtoplay = new JMenuItem("How to play");
        JCheckBoxMenuItem beginner = new JCheckBoxMenuItem("Beginner", true);
        JCheckBoxMenuItem intermediate = new JCheckBoxMenuItem("Intermediate");
        JCheckBoxMenuItem expert = new JCheckBoxMenuItem("Expert");
        JCheckBoxMenuItem custom = new JCheckBoxMenuItem("Custom");

        menubar = new JMenuBar();
        menubar.add(game);
        menubar.add(help);
        game.add(newGame);
        game.addSeparator();
        game.add(beginner);
        game.add(intermediate);
        game.add(expert);
        game.add(custom);
        game.addSeparator();
        game.add(quit);
        help.add(howtoplay);

        /**
         * The ActionListener for the menu buttons
         */
        ActionListener a = (ActionEvent e) -> {

            if (e.getSource() == newGame) {
                stopTime();
                frame.dispose();
                createNewGame(minefield.getRows(), minefield.getCols(), minefield.getMaxMines());
            } else if (e.getSource() == beginner) {
                stopTime();
                frame.dispose();
                difficulty = "Beginner";
                createNewGame(9, 9, 10);
                beginner.setState(true);
                intermediate.setState(false);
                expert.setState(false);
                custom.setState(false);
            } else if (e.getSource() == intermediate) {
                stopTime();
                frame.dispose();
                difficulty = "Intermediate";
                createNewGame(16, 16, 40);
                beginner.setState(false);
                intermediate.setState(true);
                expert.setState(false);
                custom.setState(false);
            } else if (e.getSource() == expert) {
                stopTime();
                frame.dispose();
                difficulty = "Expert";
                createNewGame(16, 31, 99);
                beginner.setState(false);
                intermediate.setState(false);
                expert.setState(true);
                custom.setState(false);
            } else if (e.getSource() == custom) {
                stopTime();
                difficulty = "Custom";
                setupCustomGame();
                beginner.setState(false);
                intermediate.setState(false);
                expert.setState(false);
                custom.setState(true);
            } else if (e.getSource() == quit) {
                System.exit(0);
            } else if (e.getSource() == howtoplay) {
                howToPlay();
            }
        };

        newGame.addActionListener(a);
        beginner.addActionListener(a);
        intermediate.addActionListener(a);
        expert.addActionListener(a);
        custom.addActionListener(a);
        quit.addActionListener(a);
        howtoplay.addActionListener(a);
    }

    /**
     * @param rows rows the minefield contains
     * @param cols columns the minefield contains
     * @param mines mines the minefield contains Creates a new game, resets the
     * timer and the marked count
     */
    private void createNewGame(int rows, int cols, int mines) {

        frame = new JFrame("Minesweeper");
        tile = new JPanel[rows][cols];
        tileLabel = new JLabel[rows][cols];
        minefield = new Minefield(rows, cols, mines);
        minefield.populate();
        timer = new Timer();
        time = "0:00";
        gameover = false;
        timerRunning = false;
        marked = 0;

        JPanel infoPanel = new JPanel(new FlowLayout());
        JLabel gameDifficulty = new JLabel("  " + difficulty + "  ");
        minesLeft = new JLabel("Mines left: " + mines);
        timePlayed = new JLabel("Time: " + time);
        minesLeft.setForeground(Color.white);
        timePlayed.setForeground(Color.white);
        gameDifficulty.setForeground(Color.white);
        infoPanel.add(minesLeft);
        infoPanel.add(gameDifficulty);
        infoPanel.add(timePlayed);
        infoPanel.setBackground(new Color(37, 110, 125));

        JPanel guiPanel = new JPanel(new BorderLayout());
        JPanel gamePanel = new JPanel(new BorderLayout());
        JPanel minefieldPanel = new JPanel(new GridLayout(rows, cols));
        gamePanel.add(minefieldPanel);
        guiPanel.add(infoPanel, BorderLayout.NORTH);
        guiPanel.add(gamePanel);
        minefieldPanel.setBackground(new Color(137, 210, 225));
        minefieldPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Border border = BorderFactory.createLineBorder(new Color(137, 210, 225), 2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tile[i][j] = new JPanel();
                tileLabel[i][j] = new JLabel("");
                minefieldPanel.add(tile[i][j]);
                tile[i][j].addMouseListener(m);
                tile[i][j].add(tileLabel[i][j]);
                tile[i][j].setBorder(border);
                tile[i][j].setBackground(Color.white);
            }
        }

        frame.setJMenuBar(menubar);
        frame.add(guiPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setSize(cols * 40, rows * 42);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * The frame created to setup a custom game is opened when the user clicks
     * on create custom game from the menu
     */
    private void setupCustomGame() {

        JFrame custom = new JFrame("Custom");
        JPanel panel = new JPanel();
        JLabel rowslabel = new JLabel("Number of rows:");
        JLabel columnslabel = new JLabel("Number of columns:");
        JLabel mineslabel = new JLabel("Number of mines:");
        JLabel wronginput = new JLabel("Please enter integers.");
        wronginput.setVisible(false);
        JTextField rowinput = new JTextField();
        JTextField columninput = new JTextField();
        JTextField mineinput = new JTextField();
        JButton confirm = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");

        confirm.addActionListener((ActionEvent e) -> {
            int customRows = 9, customCols = 9, customMines = 10;
            boolean customIsMade = false;
            try {
                customRows = Integer.parseInt(rowinput.getText());
                customCols = Integer.parseInt(columninput.getText());
                customMines = Integer.parseInt(mineinput.getText());
                if (customRows < 6) {
                    customRows = 6;
                }
                if (customCols < 6) {
                    customCols = 6;
                }
                if (customMines < 2) {
                    customMines = 2;
                }
                customIsMade = true;
            } catch (NumberFormatException ex) {
                wronginput.setVisible(true);
            }
            if (customIsMade) {
                frame.dispose();
                createNewGame(customRows, customCols, customMines);
                custom.dispose();
            }
        });

        cancel.addActionListener((ActionEvent e) -> {
            custom.dispose();
        });

        /**
         * Pressing enter key to start game as it is more convenient than
         * pressing confirm button
         */
        KeyListener k = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirm.doClick();
                }
            }
        };

        rowinput.addKeyListener(k);
        columninput.addKeyListener(k);
        mineinput.addKeyListener(k);
        rowinput.setPreferredSize(new Dimension(30, 30));
        columninput.setPreferredSize(new Dimension(30, 30));
        mineinput.setPreferredSize(new Dimension(30, 30));

        panel.add(wronginput);
        panel.add(rowslabel);
        panel.add(rowinput);
        panel.add(columnslabel);
        panel.add(columninput);
        panel.add(mineslabel);
        panel.add(mineinput);
        panel.add(confirm);
        panel.add(cancel);

        custom.setResizable(false);
        custom.add(panel);
        custom.setLocationRelativeTo(null);
        custom.setSize(220, 200);
        custom.setVisible(true);
    }

    /**
     * The frame created when the game is won
     */
    private void gameWon() {

        stopTime();
        //reveal all mines and mark them
        for (int i = 0; i < minefield.getRows(); i++) {
            for (int j = 0; j < minefield.getCols(); j++) {
                if (minefield.getMinefield()[i][j].isMined()) {
                    tileLabel[i][j].setIcon(flagIcon);
                    tile[i][j].setBackground(Color.green);
                }
            }
        }

        JFrame winframe = new JFrame("Winner!");
        JPanel winpanel = new JPanel();
        JLabel label = new JLabel("Well done, you beat the game!");
        JLabel label2 = new JLabel("  Time taken: " + time + "      ");
        JButton close = new JButton("Close");
        JButton restart = new JButton("Restart");

        close.addActionListener((ActionEvent e) -> {
            winframe.dispose();
        });

        restart.addActionListener((ActionEvent e) -> {
            frame.dispose();
            winframe.dispose();
            createNewGame(minefield.getRows(), minefield.getCols(), minefield.getMaxMines());
        });

        winpanel.add(label);
        winpanel.add(label2);
        winpanel.add(close);
        winpanel.add(restart);
        winframe.add(winpanel);
        winframe.setLocationRelativeTo(null);
        winframe.setSize(220, 110);
        winframe.setResizable(false);
        winframe.setVisible(true);
    }

    /**
     * The frame created when the game is lost
     */
    private void gameLost() {

        stopTime();
        //reveal all mines
        for (int i = 0; i < minefield.getRows(); i++) {
            for (int j = 0; j < minefield.getCols(); j++) {
                if (minefield.getMinefield()[i][j].isMined()) {
                    tileLabel[i][j].setIcon(mineIcon);
                }
            }
        }

        JFrame loseframe = new JFrame("Loser!");
        JPanel losepanel = new JPanel();
        JLabel label = new JLabel("Game over, you stepped on a mine!");
        JButton close = new JButton("Close");
        JButton restart = new JButton("Restart");

        close.addActionListener((ActionEvent e) -> {
            loseframe.dispose();
        });

        restart.addActionListener((ActionEvent e) -> {
            frame.dispose();
            loseframe.dispose();
            createNewGame(minefield.getRows(), minefield.getCols(), minefield.getMaxMines());
        });

        losepanel.add(label);
        losepanel.add(close);
        losepanel.add(restart);
        loseframe.add(losepanel);
        loseframe.setLocationRelativeTo(null);
        loseframe.setSize(240, 80);
        loseframe.setVisible(true);
        loseframe.setResizable(false);
    }

    /**
     * The frame created when the user has marked more tiles than there are
     * mines
     */
    private void markLimitExceeded() {

        JFrame popup = new JFrame("Mark limit Exceeded");
        JPanel popuppanel = new JPanel();
        JButton close = new JButton("Close");
        JLabel label = new JLabel("You can't mark more tiles until you unmark other ones.");

        close.addActionListener((ActionEvent e) -> {
            popup.dispose();
        });

        popuppanel.add(label);
        popuppanel.add(close);
        popup.add(popuppanel);
        popup.setLocationRelativeTo(null);
        popup.setSize(380, 80);
        popup.setVisible(true);
        popup.setResizable(false);
    }

    /**
     * The frame created when How to play is pressed
     */
    private void howToPlay() {

        String s = "You are presented with a grid of tiles, some of which contain mines.\n"
                + "The aim of Minesweeper is to reveal all tiles in the grid which do not contain mines.\n"
                + "To reveal a tile, click on it. If the tile you reveal is a mine, you lose the game.\n"
                + "If the tile you reveal is empty, all surrounding tiles will be revealed, \n"
                + "and if those are also empty, their surrounding tiles will be revealed too.\n"
                + "Tiles containing numbers indicate how many mines there are surrounding that tile.\n"
                + "If you think a tile contains a mine, right click on it to mark it as a mine.\n"
                + "You win the game once either you mark all mines, or reveal all tiles which are not mines.";

        JFrame helpframe = new JFrame("How to play");
        JPanel helppanel = new JPanel();
        JButton close = new JButton("Got it!");
        JTextArea text = new JTextArea(s);

        close.addActionListener((ActionEvent ev) -> {
            helpframe.dispose();
        });

        text.setEditable(false);
        text.setOpaque(false);
        text.setWrapStyleWord(true);
        helppanel.add(text);
        helppanel.add(close);
        helpframe.add(helppanel);
        helpframe.setLocationRelativeTo(null);
        helpframe.setSize(560, 200);
        helpframe.setVisible(true);
        helpframe.setResizable(false);
    }

    /**
     * The MouseAdapter which lets users control the game
     */
    MouseAdapter m = new MouseAdapter() {

        /**
         * @param e the event of pressing the tile
         */
        @Override
        public void mousePressed(MouseEvent e) {

            //start the time after the first tile is stepped on
            if (!timerRunning && !gameover) {
                startTime();
            }
            //find which specific tile has been pressed
            for (int i = 0; i < minefield.getRows(); i++) {
                for (int j = 0; j < minefield.getCols(); j++) {
                    //check for left click
                    if (e.getSource().equals(tile[i][j])
                            && e.getButton() == MouseEvent.BUTTON1
                            && !minefield.getMinefield()[i][j].isMarked()
                            && !gameover) {
                        minefield.step(i, j);
                        revealTiles();
                        //check if the game has been won or lost after the user steps on a tile
                        if (minefield.getMinefield()[i][j].isMined()) {
                            gameLost();
                        } else if (minefield.areAllNonMinesRevealed()) {
                            gameWon();
                        }
                    } //check for right click
                    else if (e.getSource().equals(tile[i][j])
                            && e.getButton() == MouseEvent.BUTTON3
                            && !minefield.getMinefield()[i][j].isRevealed()
                            && !gameover) {
                        //if the tile is marked, unmark it
                        if (minefield.getMinefield()[i][j].isMarked()) {
                            minefield.mark(i, j);
                            tileLabel[i][j].setIcon(null);
                            marked--;
                            minesLeft.setText("Mines left: " + (minefield.getMaxMines() - marked));
                        } else {
                            //check if user can still mark a tile
                            if (marked < minefield.getMaxMines()) {
                                minefield.mark(i, j);
                                tileLabel[i][j].setIcon(flagIcon);
                                marked++;
                                minesLeft.setText("Mines left: " + (minefield.getMaxMines() - marked));
                                //check if the last mine has been marked, to win the game
                                if (minefield.areAllMinesMarked()) {
                                    gameWon();
                                }
                            } else {
                                markLimitExceeded();
                            }
                        }
                    }
                }
            }
        }

        /**
         * @param e the event of the mouse entering the tile
         */
        @Override
        public void mouseEntered(MouseEvent e) {

            for (int i = 0; i < minefield.getRows(); i++) {
                for (int j = 0; j < minefield.getCols(); j++) {
                    if (e.getSource().equals(tile[i][j])
                            && !minefield.getMinefield()[i][j].isRevealed()
                            && !gameover) {
                        tile[i][j].setBackground(Color.orange);
                    }
                }
            }
        }

        /**
         * @param e the event of the mouse exiting the tile
         */
        @Override
        public void mouseExited(MouseEvent e) {

            for (int i = 0; i < minefield.getRows(); i++) {
                for (int j = 0; j < minefield.getCols(); j++) {
                    if (e.getSource().equals(tile[i][j])
                            && !minefield.getMinefield()[i][j].isRevealed()
                            && !gameover) {
                        tile[i][j].setBackground(Color.white);
                    }
                }
            }
        }
    };

    /**
     * Reveals the relevant tiles after the user steps on a tile Sets the
     * appropriate text colour if the tile revealed is a number
     */
    private void revealTiles() {

        for (int i = 0; i < minefield.getRows(); i++) {
            for (int j = 0; j < minefield.getCols(); j++) {
                if (minefield.getMinefield()[i][j].isRevealed()
                        && !minefield.getMinefield()[i][j].isMined()) {
                    tileLabel[i][j].setText("<html><b>" + minefield.getMinefield()[i][j].getMinedNeighbours() + "</html></b>");
                    tile[i][j].setBackground(new Color(208, 237, 243));
                    switch (tileLabel[i][j].getText()) {
                        case "<html><b>0</html></b>":
                            tileLabel[i][j].setText("");
                            break;
                        case "<html><b>1</html></b>":
                            tileLabel[i][j].setForeground(Color.blue);
                            break;
                        case "<html><b>2</html></b>":
                            tileLabel[i][j].setForeground(new Color(0, 100, 0));
                            break;
                        case "<html><b>3</html></b>":
                            tileLabel[i][j].setForeground(Color.red);
                            break;
                        case "<html><b>4</html></b>":
                            tileLabel[i][j].setForeground(new Color(75, 0, 130));
                            break;
                        case "<html><b>5</html></b>":
                            tileLabel[i][j].setForeground(new Color(130, 0, 0));
                            break;
                        case "<html><b>6</html></b>":
                            tileLabel[i][j].setForeground(new Color(0, 153, 153));
                            break;
                        case "<html><b>7</html></b>":
                            tileLabel[i][j].setForeground(Color.black);
                            break;
                        case "<html><b>8</html></b>":
                            tileLabel[i][j].setForeground(Color.darkGray);
                            break;
                    }
                }
            }
        }
        frame.repaint();
    }

    /**
     * Starts the time
     */
    private void startTime() {

        timerRunning = true;
        TimerTask task = new TimerTask() {

            private int secs = -1;
            private int tens = 0;
            private int mins = 0;

            /**
             * Update the time display every second
             */
            @Override
            public void run() {

                secs++;
                if (secs == 10) {
                    tens++;
                    secs = 0;
                }
                if (tens == 6) {
                    mins++;
                    tens = 0;
                    secs = 0;
                }
                time = mins + ":" + tens + "" + secs;
                timePlayed.setText("Time: " + time);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * Stops the time
     */
    private void stopTime() {

        gameover = true;
        timerRunning = false;
        timer.cancel();
    }

    public static void main(String[] args) {

        new Minesweeper(9, 9, 10);
    }

}
