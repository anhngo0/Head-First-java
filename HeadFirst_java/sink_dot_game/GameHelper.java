package sink_dot_game;
import java.io.*;
import java.util.*;
public class GameHelper {

    private static final String alphabet = "abcdefg";
    private int gridLength = 7;
    private int gridSize = 49;
    private int[] grid = new int[gridSize];
    private int comCount = 0;                 // number of dotcom 

    public GameHelper(){}

    public String getUserInput(String prompt){
        String inputLine = null;
        System.out.println(prompt + " ");
        try {
            BufferedReader is = new BufferedReader(
                new InputStreamReader(System.in)
                );
            inputLine = is.readLine();
            if( inputLine.length() == 0 ) return null;
        } catch (IOException e){
            System.out.println("IOExeption: " + e);
        }
        return inputLine.toLowerCase();
    }

    public ArrayList<String> placeDotCom(int comSize){
        // comSize is the number of dot that the dotCom fills in 
        // Each dot has a name and need to be sunk 
        ArrayList<String> alphaCells = new ArrayList<String>();
        String[] alphacoords = new String[comSize];
        String temp = null;                         //temporary String for concat
        int[] coords = new int[comSize];            //current canđiate coords 
        int attempts = 0;                           //current attempts counter
        boolean success = false;                    //flag = found a good location
        int location = 0;                           //current starting location
        
        comCount++;                                 //nth dot com to place
        int incr = 1;
        if( (comCount % 2) == 1) {
            incr = gridLength;
        } 
        while ( !success & attempts++ <= 200){
            location = (int) (Math.random() * gridSize);
            int x = 0; 
            success = true;
            // set dot's locations
            while(success && x < comSize){
                if(grid[location] == 0){
                    coords[x++] = location;
                    location += incr;
                    // out of number dot
                    if(location >= gridSize){
                        success = false;  
                    }
                    // out of line
                    if( x>0 && (location % gridLength == 0)){
                        success = false;
                    }
                } else {
                    // repeat position
                    success = false;
                }
            }
        }

        int x = 0; 
        int row = 0;
        int column = 0;
        while (x < comSize) {
            grid[coords[x]] = 1;
            row = (int) (coords[x] / gridLength);
            column = coords[x] % gridLength;
            temp = String.valueOf(alphabet.charAt(column));

            alphaCells.add(temp.concat(Integer.toString(row)));
            x++;
        }

        return alphaCells; 
    }

}
