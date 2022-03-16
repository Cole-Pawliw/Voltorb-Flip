import java.lang.Math;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VoltorbFlip {
    
    static JFrame frame = new JFrame("Voltorb Flip V1.0");
    static JPanel gamePane, startPane, shopPane;
    public static JLabel info[], coins[], modeLabel, blanks[], shopLabels[];
    static JButton tiles[][], startButton[], back, abilities[], shopButtons[];
    static int size, mode = 0, win;
    static Level board = new Level();
    
    /**
     * Creates the first contentPane where the player selects what they want to do
     * Pre: none
     * Post: a 2x2 contentPane
     */
    public static void startScreen() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Making the contentPane
        startPane = new JPanel();
        startPane.setLayout(new GridLayout(2, 2, 25, 25));
        startPane.setBorder(BorderFactory.createEmptyBorder(400, 800, 400, 800));
        startPane.setBackground(Color.lightGray);
        
        
        //Adding the fours necessary buttons
        VoltorbFlip.startButton = new JButton[4];
        startButton[0] = new JButton("4x4");
        startButton[1] = new JButton("5x5");
        startButton[2] = new JButton("6x6");
        startButton[3] = new JButton("Shop");
        
        startButton[0].setActionCommand("Small");
        startButton[1].setActionCommand("Medium");
        startButton[2].setActionCommand("Large");
        startButton[3].setActionCommand("Shop");
        
        startButton[0].addActionListener(new startListener());
        startButton[1].addActionListener(new startListener());
        startButton[2].addActionListener(new startListener());
        startButton[3].addActionListener(new startListener());
        
        //Adding buttons to the contentPane
        startPane.add(startButton[0]);
        startPane.add(startButton[1]);
        startPane.add(startButton[2]);
        startPane.add(startButton[3]);
        
        //Adding the startPane to the frame
        frame.setContentPane(startPane);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1920, 1080);
    }
    
    /**
     * Makes the contentPane for the shop
     * Pre: none
     * Post: A large contentPane with all the purchasable items
     */
    public static void shopScreen() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Making the contentPane
        shopPane = new JPanel();
        shopPane.setLayout(new GridLayout(0, 2, 30, 40));
        shopPane.setBorder(BorderFactory.createEmptyBorder(150, 500, 150, 500));
        shopPane.setBackground(Color.lightGray);
        
        coins = new JLabel[2];
        for (int i = 0; i < coins.length; i++) {
            coins[i] = new JLabel();
            shopPane.add(coins[i]);
        }
        coins[0].setText("Coins: " + board.money);
        coins[0].setFont(new Font("ARIAL BLACK", Font.PLAIN, 25));
        
        shopLabels = new JLabel[5];
        shopButtons = new JButton[4];
        
        for (int i = 0; i < 4; i++) {
            shopLabels[i] = new JLabel();
            shopButtons[i] = new JButton();
            shopButtons[i].addActionListener(new shopListener());
            shopPane.add(shopButtons[i]);
            shopPane.add(shopLabels[i]);
        }
        back = new JButton("Back");
        back.setActionCommand("Back");
        back.addActionListener(new gameListener());
        shopPane.add(back);
        
        shopLabels[4] = new JLabel();
        shopPane.add(shopLabels[4]);
        
        shopButtons[0].setText("<html>Clears: 25000 coins<br><br>You currently have " + board.unlocked[0] + " clears</html>");
        shopButtons[0].setActionCommand("Clear");
        shopButtons[1].setText("<html>Redo: 50000 coins<br><br>You currently have " + board.unlocked[1] + " redos</html>");
        shopButtons[1].setActionCommand("Random");
        
        if (board.unlocked[2] == 0) { //Checks if the two modes have already been purchased
            shopButtons[2].setText("<html>Unlock 5x5 mode: 3000 coins<br><br>5x5 is currently locked</html>");
            shopButtons[2].setActionCommand("Five");
        } else {
            VoltorbFlip.shopButtons[2].setText("<html>5x5 is available</html>");
            VoltorbFlip.shopButtons[2].setActionCommand("");
        }
        if (board.unlocked[3] == 0) {
            shopButtons[3].setText("<html>Unlock 6x6 mode: 200000 coins<br><br>6x6 is currently locked</html>");
            shopButtons[3].setActionCommand("Six");
        } else {
            VoltorbFlip.shopButtons[3].setText("<html>6x6 is available</html>");
            VoltorbFlip.shopButtons[3].setActionCommand("");
        }
        
        shopLabels[0].setText("<html>Gain the ability to clear any single space without consequences while playing.<br>Just enable 'Clear' mode and click on any space you want to clear.<br>Be aware that if there is a 2 or a 3, you won't get any reward for it.</html>");
        shopLabels[1].setText("<html>Completely randomizes the entire board again.<br>Useful for when you don't like the look of the board.</html>");
        shopLabels[2].setText("<html>Permanently unlocks the ability to play 5x5 mode from the main menu.</html>");
        shopLabels[3].setText("<html>Permanently unlocks the ability to play 6x6 mode from the main menu.</html>");
        
        frame.setContentPane(shopPane);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1920, 1080);
    }
    
    /**
     * Makes the contentPane for the actual gameplay
     * Pre: none
     * Post: a large contentPane for the game
     */
    public VoltorbFlip() {
        mode = 0;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gamePane = new JPanel();
        gamePane.setLayout(new GridLayout(size + 3, size + 2, 25, 30));
        gamePane.setBorder(BorderFactory.createEmptyBorder(200, 600, 200, 600));
        gamePane.setBackground(Color.lightGray);
        
        //Three labels that display coins earned in that game, total coins earned, and the current level
        coins = new JLabel[5];
        coins[0] = new JLabel("<html>Coins earned:<br>" + board.gameMoney + "</html>");
        coins[1] = new JLabel("<html>Total Coins:<br>" + board.money + "</html>");
        coins[2] = new JLabel("<html>Level:<br>" + Level.stage + "</html>");
        coins[3] = new JLabel("<html>Clears: " + board.unlocked[0] + "<br>Redos: " + board.unlocked[1] + "</html>");
        if (win == 1) {
            coins[4] = new JLabel("Winner!");
            coins[4].setForeground(Color.GREEN);
        } else if (win == 2){
            coins[4] = new JLabel("Loser!");
            coins[4].setForeground(Color.RED);
        } else {
            coins[4] = new JLabel("");
        }
        
        for (int i = 0; i < coins.length; i++) {
            gamePane.add(coins[i]);
        }
        
        //Just a bunch of random blank labels to fill out the grid where there isn't anything else
        blanks = new JLabel[8];
        for (int i = 0; i < blanks.length; i++) {
            blanks[i] = new JLabel("");
        }
        
        int keepTrack = 0;
        for (int i = 0; i < size - 4; i++) {
            gamePane.add(blanks[keepTrack]);
            keepTrack++;
        }
        
        abilities = new JButton[4];
        abilities[0] = new JButton("Play");
        abilities[0].setActionCommand("Play");
        abilities[0].addActionListener(new abilityListener());
        abilities[1] = new JButton("Marker");
        abilities[1].setActionCommand("Mark");
        abilities[1].addActionListener(new abilityListener());
        abilities[2] = new JButton("<html>Clear<br>Space</html>");
        abilities[2].setActionCommand("Clear");
        abilities[2].addActionListener(new abilityListener());
        abilities[3] = new JButton("Redo");
        abilities[3].setActionCommand("Random");
        abilities[3].addActionListener(new abilityListener());
        
        for (int i = 0; i < abilities.length; i++) {
            gamePane.add(abilities[i]);
        }
        
        modeLabel = new JLabel("Mode: Play");
        gamePane.add(modeLabel);
        
        for (int i = 0; i < size - 4; i++) {
            gamePane.add(blanks[keepTrack]);
            keepTrack++;
        }
        
        //Makes a 2D array of buttons that can all be pressed and sync to the board int
        VoltorbFlip.tiles = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new JButton(" ");
                tiles[i][j].setActionCommand("Show" + i + j);
                tiles[i][j].addActionListener(new gameListener());
                gamePane.add(tiles[i][j]);
            }
            gamePane.add(info[i]); //Adds the information for the current row on the side
        }
        
        //Adds the information for each column underneath the board
        for (int i = 0; i < size; i++) {
            gamePane.add(info[i + board.grid.length]);
        }
        
        //A back button that returns the player to the start screen
        back = new JButton("Back");
        back.setActionCommand("Back");
        back.addActionListener(new gameListener());
        gamePane.add(back);
        
        //Adds the gamePane to the frame
        frame.setContentPane(gamePane);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1920, 1080);
    }
    
    /**
     * A short method that runs the gamePane
     * Pre: none
     * Post: none
     */
    public static void runGame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        VoltorbFlip run = new VoltorbFlip();
    }
    
    /**
     * A short method that runs the startPane
     * Pre: none
     * Post: none
     */
    public static void runStart() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        startScreen();
    }
    
    /**
     * A short method that runs the startPane
     * Pre: none
     * Post: none
     */
    public static void runShop() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        shopScreen();
    }
    
    /**
     * The main method that just begins the program
     * Pre: none
     * Post: none
     */
    public static void main(String args[]) {
        
        SaveFile.Read();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                runStart();
            }
        });
    }

}

class gameListener implements ActionListener {
    
    /**
     * Listens to all the button presses within the gamePane and performs the appropriate actions
     * Pre: The press of a button
     * Post: performs an action
     */
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        if (eventName.equals("Back")) { //Reads if the back button was pressed
            VoltorbFlip.board.stage = 1;
            VoltorbFlip.board.gameMoney = 0;
            VoltorbFlip.mode = 0;
            VoltorbFlip.win = 0;
            SaveFile.Save(VoltorbFlip.board.money, VoltorbFlip.board.unlocked);
            VoltorbFlip.runStart(); //Returns the game to the startPane
        } else { //If any other button is pressed
            int location1 = Integer.parseInt(eventName.substring(4, 5));
            int location2 = Integer.parseInt(eventName.substring(5, 6));
            
            if (eventName.substring(0, 4).equals("Show")) { //Only does something if the button hasn't been pressed before
                boolean startAgain = false;
                if (VoltorbFlip.mode == 1) {
                    if (VoltorbFlip.tiles[location1][location2].getText() == "X") {
                        VoltorbFlip.tiles[location1][location2].setText("");
                    } else {
                        VoltorbFlip.tiles[location1][location2].setText("X");
                        VoltorbFlip.tiles[location1][location2].setFont(new Font("ARIAL BLACK", Font.PLAIN, 50));
                    }
                } else if (VoltorbFlip.mode == 2) {
                    if (VoltorbFlip.board.unlocked[0] > 0) {
                        VoltorbFlip.board.unlocked[0]--;
                        VoltorbFlip.tiles[location1][location2].setText(Integer.toString(Level.grid[location1][location2]));
                        VoltorbFlip.tiles[location1][location2].setFont(new Font("ARIAL BLACK", Font.PLAIN, 12));
                        VoltorbFlip.tiles[location1][location2].setActionCommand("Wohs" + location1 + location2);
                        VoltorbFlip.coins[3].setText("<html>Clears: " + VoltorbFlip.board.unlocked[0] + "<br>Redos: " + VoltorbFlip.board.unlocked[1] + "</html>");
                        startAgain = VoltorbFlip.board.checkNumber(location1, location2); //Receives a boolean to decide if the game should continue
                        
                        if (startAgain) { //If the returned boolean is true, starts the game over
                            VoltorbFlip.board.makeBoard();
                            VoltorbFlip.board.numbers();
                            VoltorbFlip.board.bombs();
                            VoltorbFlip.board.makeLabels(false);
                            
                            SaveFile.Save(VoltorbFlip.board.money, VoltorbFlip.board.unlocked);
                            VoltorbFlip.runGame();
                        }
                    }
                    
                } else {
                    VoltorbFlip.tiles[location1][location2].setText(Integer.toString(Level.grid[location1][location2]));
                    VoltorbFlip.tiles[location1][location2].setFont(new Font("ARIAL BLACK", Font.PLAIN, 12));
                    VoltorbFlip.tiles[location1][location2].setActionCommand("Wohs" + location1 + location2);
                    startAgain = VoltorbFlip.board.checkNumber(location1, location2); //Receives a boolean to decide if the game should continue
                }
                
                if (startAgain) { //If the returned boolean is true, starts the game over
                    VoltorbFlip.board.makeBoard();
                    VoltorbFlip.board.numbers();
                    VoltorbFlip.board.bombs();
                    VoltorbFlip.board.makeLabels(false);
                    
                    SaveFile.Save(VoltorbFlip.board.money, VoltorbFlip.board.unlocked);
                    VoltorbFlip.runGame();
                }
            }
            
            
        }
    }
    
}

class startListener implements ActionListener {
    
    /**
     * Listens to all the button presses within the StartPane and performs the appropriate actions
     * Pre: The press of a button
     * Post: Sets the correct size of the board and starts the game, or opens up the shop
     */
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        
        if (eventName.equals("Shop")) {
            VoltorbFlip.runShop();
        } else {
            if (eventName.equals("Small")) {
                VoltorbFlip.board.gridSize(4);
                VoltorbFlip.board.makeBoard();
                VoltorbFlip.board.numbers();
                VoltorbFlip.board.bombs();
                VoltorbFlip.board.makeLabels(false);
                
                VoltorbFlip.runGame();
            } else if (eventName.equals("Medium")) {
                if (VoltorbFlip.board.unlocked[2] == 1) {
                    VoltorbFlip.board.gridSize(5);
                    VoltorbFlip.board.makeBoard();
                    VoltorbFlip.board.numbers();
                    VoltorbFlip.board.bombs();
                    VoltorbFlip.board.makeLabels(false);
                    
                    VoltorbFlip.runGame();
                }
                
            } else if (eventName.equals("Large")) {
                if (VoltorbFlip.board.unlocked[3] == 1) {
                    VoltorbFlip.board.gridSize(6);
                    VoltorbFlip.board.makeBoard();
                    VoltorbFlip.board.numbers();
                    VoltorbFlip.board.bombs();
                    VoltorbFlip.board.makeLabels(false);
                    
                    VoltorbFlip.runGame();
                }
                
            }
            
        }
    }
}

class abilityListener implements ActionListener {
    
    /**
     * Listens to all the ability and mode buttons and either changes the game mode or remakes the board
     * Pre: The press of a button
     * Post: changes the mode or provides a new board
     */
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        
        if (eventName.equals("Mark")) {
            VoltorbFlip.mode = 1;
            VoltorbFlip.modeLabel.setText("Mode: Marker");
        } else if (eventName.equals("Play")) {
            VoltorbFlip.mode = 0;
            VoltorbFlip.modeLabel.setText("Mode: Play");
        } else if (eventName.equals("Clear")) {
            VoltorbFlip.mode = 2;
            VoltorbFlip.modeLabel.setText("Mode: Clear");
        } else if (eventName.equals("Random")) {
            if (VoltorbFlip.board.unlocked[1] > 0) {
                VoltorbFlip.board.unlocked[1]--;
                VoltorbFlip.coins[3].setText("<html>Clears: " + VoltorbFlip.board.unlocked[0] + "<br>Redos: " + VoltorbFlip.board.unlocked[1] + "</html>");
                VoltorbFlip.board.gameMoney = 0;
                VoltorbFlip.coins[0] = new JLabel("<html>Coins earned:<br>" + VoltorbFlip.board.gameMoney + "</html>");
                
                VoltorbFlip.board.makeBoard();
                VoltorbFlip.board.numbers();
                VoltorbFlip.board.bombs();
                VoltorbFlip.board.makeLabels(true);
                
                VoltorbFlip.runGame();
            }
        }
    }
}

class shopListener implements ActionListener {
    
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        
        if (eventName.equals("Clear")) {
            VoltorbFlip.board.shop(0);
            
        } else if (eventName.equals("Random")) {
            VoltorbFlip.board.shop(1);
            
        } else if (eventName.equals("Five")) {
            VoltorbFlip.board.shop(2);
            
        } else if (eventName.equals("Six")) {
            VoltorbFlip.board.shop(3);
            
        }
    }
}
