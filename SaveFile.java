import java.io.*;
import java.util.ArrayList;

public class SaveFile {
    
    /**
     * Writes the higher score to the record file
     * Pre: higher score and winning player's name
     * Post: none
     */
    public static void Save(int coins, int unlocked[]) {
        ArrayList<String> info = new ArrayList<String>();
        info.add(Integer.toString(coins));
        info.add(Integer.toString(unlocked[0]));
        info.add(Integer.toString(unlocked[1]));
        info.add(Integer.toString(unlocked[2]));
        info.add(Integer.toString(unlocked[3]));
        
        File textFile = new File("H:\\VoltorbFile.txt");
        
         try {    
             FileWriter record = new FileWriter("H:\\VoltorbFile.txt");
             
               for (int i = 0; i < info.size(); i++) {
                   record.write(info.get(i) + "\n");
               }
            record.close();    
         } catch(Exception e) {
             System.out.println(e);
         }
    }
    
    /**
     * Reads the file at the launch of the game to get all the previous information
     * Pre: none
     * Post: Changes all the appropriate values in the level class
     */
    public static void Read() {
        int[] stuff = new int[5];
        int keepTrack = 0;
        String clearedRead;
        
        File textFile = new File("H:\\VoltorbFile.txt");
        FileReader in;
        BufferedReader readFile;
        String lineOfText;
        
        if (textFile.exists()) {
        } else {
            try {
                textFile.createNewFile();
            } catch (IOException e) {
                System.out.println("File could not be created.");
                System.err.println("OIException: " + e.getMessage());
            }
        }
        
        try {
            in = new FileReader(textFile);
            readFile = new BufferedReader(in);
            while ((lineOfText = readFile.readLine()) != null) {
                clearedRead = lineOfText.replaceAll("[^0-9]", "");
                stuff[keepTrack] = Integer.parseInt(clearedRead);
                keepTrack++;
            }
            readFile.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Problem reading file."); 
            System.err.println("IOException: " + e.getMessage());
        }
        
        VoltorbFlip.board.money = stuff[0];
        VoltorbFlip.board.unlocked[0] = stuff[1];
        VoltorbFlip.board.unlocked[1] = stuff[2];
        VoltorbFlip.board.unlocked[2] = stuff[3];
        VoltorbFlip.board.unlocked[3] = stuff[4];
    }
}
