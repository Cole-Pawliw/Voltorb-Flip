import java.awt.Color;
import java.lang.Math;
import java.util.Arrays;
import javax.swing.*;

public class Level {
    
    public static int[][] grid;
    public static int stage = 1;
    int money, gameMoney;
    int numOfVoltorb, numOfTwo, numOfThree;
    int bombColumn[], bombRow[], numColumn[], numRow[], unlocked[] = {0, 0, 0, 0};
    
    /**
     * Initializes the size of the board
     * Pre: none
     * Post: none
     */
    public void gridSize(int size) {
        VoltorbFlip.size = size;
        grid = new int[size][size];
    }
    
    /**
     * Creates the number of voltorbs, 2's, and 3's for every stage
     * Pre: none
     * Post: A completed board
     */
    public void makeBoard() {
        
        for (int[] row: grid) {
            Arrays.fill(row, 1);
        }
        
        
        double voltorbPercent = stage * 0.07; //What percent of tiles are voltorb in each stage (maximum of 35%)
        double twoPercent = stage * 0.06; //What percent of tiles are two in each stage (maximum of 30%)
        double threePercent = stage * 0.05; //What percent of tiles are three in each stage (maximum of 25%)
        
        int randomX;
        int randomY;
        
        
        /*Creates the number of each tile type based on stage and grid size*/
        if (grid.length == 4) {
            numOfVoltorb = (int) Math.round(16 * voltorbPercent);
            numOfTwo = (int) Math.round(16 * twoPercent);
            numOfThree = (int) Math.round(16 * threePercent);
            
        } else if (grid.length == 5) {
            numOfVoltorb = (int) Math.round(25 * voltorbPercent);
            numOfTwo = (int) Math.round(25 * twoPercent);
            numOfThree = (int) Math.round(25 * threePercent);
            
        } else {
            numOfVoltorb = (int) Math.round(36 * voltorbPercent);
            numOfTwo = (int) Math.round(36 * twoPercent);
            numOfThree = (int) Math.round(36 * threePercent);
        }
        
        //Randomizes voltorbs into the grid
        for (int i = 0; i < numOfVoltorb; i++) {
            
            //Repeats until it finds a value that is currently 1
            do {
                randomX = (int)(Math.random() * grid.length);
                randomY = (int)(Math.random() * grid.length);
            } while (grid[randomX][randomY] != 1);
            
            grid[randomX][randomY] = 0;
        }
        
        //Randomizes twos into the grid
        for (int i = 0; i < numOfTwo; i++) {
            
            //Repeats until it finds a value that is currently 1
            do {
                randomX = (int)(Math.random() * grid.length);
                randomY = (int)(Math.random() * grid.length);
            } while (grid[randomX][randomY] != 1);
            
            grid[randomX][randomY] = 2;
        }
        
        //Randomizes threes into the grid
        for (int i = 0; i < numOfThree; i++) {
            
            //Repeats until it finds a value that is currently 1
            do {
                randomX = (int)(Math.random() * grid.length);
                randomY = (int)(Math.random() * grid.length);
            } while (grid[randomX][randomY] != 1);
    
            grid[randomX][randomY] = 3;
        }
        
    }
    
    /**
     * Makes two arrays that add the numbers of each column and row up
     * Pre: none
     * Post: Arrays holding the information of the board
     */
    public void numbers() {
        numColumn = new int[grid.length];
        numRow = new int[grid.length];
        int number;
        
        for (int i = 0; i < numColumn.length; i++) {
            number = 0; //Sets the number to 0 so it can repeat for every column
            for (int j = 0; j < numRow.length; j++) {
                number = number + grid[j][i]; //Adds the number in the current location to the column
            }
            numColumn[i] = number;
        }
        
        for (int i = 0; i < numColumn.length; i++) {
            number = 0; //Sets the number to 0 so it can repeat for every row
            for (int j = 0; j < numRow.length; j++) {
                number = number + grid[i][j]; //Adds the number in the current location to the row
            }
            numRow[i] = number;
        }
    }
    
    /**
     * Makes two arrays that count the number of bombs in each row and column
     * Pre: none
     * Post: none
     */
    public void bombs() {
        bombColumn = new int[grid.length];
        bombRow = new int[grid.length];
        int number;
        //Everything here works the exact same as in the number() method, but only adds 0's
        for (int i = 0; i < bombColumn.length; i++) {
            number = 0;
            for (int j = 0; j < bombRow.length; j++) {
                if (grid[j][i] == 0) {
                    number++;
                }
            }
            bombColumn[i] = number;
        }
        
        for (int i = 0; i < bombRow.length; i++) {
            number = 0;
            for (int j = 0; j < bombColumn.length; j++) {
                if (grid[i][j] == 0) {
                    number++;
                }
            }
            bombRow[i] = number;
        }
    }
    
    /**
     * When called, checks the number at a given location and either adds to the score or ends the game
     * Pre: Two ints used as a location for the number
     * Post: Boolean stating whether or not the current game will continue
     */
    public boolean checkNumber(int x, int y) {
        int number = grid[x][y];
        boolean gameDone = false;
        
        if (number == 0) {
            if (VoltorbFlip.mode != 2) {
                gameMoney = 0;
                stage = stage - 1;
                VoltorbFlip.coins[0].setText("<html>Coins earned:<br>" + gameMoney + "</html>");
                VoltorbFlip.coins[2].setText("<html>Level:<br>" + stage + "</html>"); 
                gameDone = true;
                VoltorbFlip.win = 2;
            }
            
        } else if (number == 2) {
            
            if (VoltorbFlip.mode != 2) {
                if (gameMoney == 0) {
                    gameMoney = 2;
                } else {
                    gameMoney = gameMoney * 2;
                }
                VoltorbFlip.coins[0].setText("<html>Coins earned:<br>" + gameMoney + "</html>");
            }
            numOfTwo--;
            
        } else if (number == 3) {
            if (VoltorbFlip.mode != 2) {
                if (gameMoney == 0) {
                    gameMoney = 3;
                } else {
                    gameMoney = gameMoney * 3;
                }
                VoltorbFlip.coins[0].setText("<html>Coins earned:<br>" + gameMoney + "</html>");
            }
            numOfThree--;
            
        } else {
            if (gameMoney == 0) {
                gameMoney = 1;
            }
        }
        if (numOfTwo == 0 && numOfThree == 0) {
            if (stage < 5) {
                stage = stage + 1;
            }
            money = money + gameMoney;
            gameMoney = 0;
            gameDone = true;
            VoltorbFlip.win = 1;
        }
        VoltorbFlip.coins[0].setText("<html>Coins earned:<br>" + gameMoney + "</html>");
        VoltorbFlip.coins[1].setText("<html>Total Coins:<br>" + money + "</html>");
        VoltorbFlip.coins[2].setText("<html>Level:<br>" + stage + "</html>");
        return gameDone;
    }
    
    /**
     * Makes the labels in the main method so they display all the proper numbers
     * Pre: none
     * Post: two JLabel arrays
     */
    public void makeLabels(boolean redo) {
        String[] create = new String[grid.length * 2];
        
        for (int i = 0; i < grid.length; i++) {
            create[i] = "<html>Numbers: " + numRow[i] + "<br>Bombs: " + bombRow[i] + "</html>";
        }
        for (int i = 0; i < grid.length; i++) {
            create[i + grid.length] = "<html>Numbers: " + numColumn[i] + "<br>Bombs: " + bombColumn[i] + "</html>";
        }
        
        if (redo) {
            for (int i = 0; i < create.length; i++) {
                VoltorbFlip.info[i].setText(create[i]);
            }
        } else {
            VoltorbFlip.info = new JLabel[create.length];
            for (int i = 0; i < create.length; i++) {
                VoltorbFlip.info[i] = new JLabel(create[i]);
            }    
        }
        
        
    }
    
    /**
     * Determines if the player can purchase something from the shop, and unlocks it if they can
     * Pre: the item attempting to be purchased
     * Post: gives the player what they bought
     */
    public void shop(int product) {
        if (product == 0) {
            if (money >= 25000) {
                unlocked[0]++;
                money = money - 25000;
                VoltorbFlip.coins[0].setText("Coins: " + money);
                VoltorbFlip.shopLabels[4].setText("Item purchased successfully.");
                VoltorbFlip.shopLabels[4].setForeground(Color.GREEN);
                VoltorbFlip.shopButtons[0].setText("<html>Clears: 25000 coins<br><br>You currently have " + unlocked[0] + " clears</html>");
            } else {
                VoltorbFlip.shopLabels[4].setText("<html>You do not have enough coins to purchase this item,<br>please save up and try again.</html>");
                VoltorbFlip.shopLabels[4].setForeground(Color.RED);
            }
        } else if (product == 1) {
            if (money >= 50000) {
                unlocked[1]++;
                money = money - 50000;
                VoltorbFlip.coins[0].setText("Coins: " + money);
                VoltorbFlip.shopLabels[4].setText("Item purchased successfully.");
                VoltorbFlip.shopLabels[4].setForeground(Color.GREEN);
                VoltorbFlip.shopButtons[1].setText("<html>Redo: 50000 coins<br><br>You currently have " + unlocked[1] + " redos</html>");
            } else {
                VoltorbFlip.shopLabels[4].setText("<html>You do not have enough coins to purchase this item,<br>please save up and try again.</html>");
                VoltorbFlip.shopLabels[4].setForeground(Color.RED);
            }
        } else if (product == 2) {
            if (money >= 3000 && unlocked[2] == 0) {
                money = money - 3000;
                unlocked[2] = 1;
                VoltorbFlip.coins[0].setText("Coins: " + money);
                VoltorbFlip.shopLabels[4].setText("Item purchased successfully.");
                VoltorbFlip.shopLabels[4].setForeground(Color.GREEN);
                VoltorbFlip.shopButtons[2].setText("<html>5x5 is available</html>");
                VoltorbFlip.shopButtons[2].setActionCommand("Done");
            } else {
                VoltorbFlip.shopLabels[4].setText("<html>You do not have enough coins to purchase this item,<br>please save up and try again.</html>");
                VoltorbFlip.shopLabels[4].setForeground(Color.RED);
            }
        } else if (product == 3) {
            if (money >= 200000 && unlocked[3] == 0) {
                money = money - 200000;
                unlocked[3] = 1;
                VoltorbFlip.coins[0].setText("Coins: " + money);
                VoltorbFlip.shopLabels[4].setText("Item purchased successfully.");
                VoltorbFlip.shopLabels[4].setForeground(Color.GREEN);
                VoltorbFlip.shopButtons[3].setText("<html>6x6 is available</html>");
                VoltorbFlip.shopButtons[3].setActionCommand("");
            } else {
                VoltorbFlip.shopLabels[4].setText("<html>You do not have enough coins to purchase this item,<br>please save up and try again.</html>");
                VoltorbFlip.shopLabels[4].setForeground(Color.RED);
            }
        }
        SaveFile.Save(money, unlocked);
    }
    
    
}
