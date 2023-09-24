package sink_dot_game;
import java.util.ArrayList;

public class DotComBurst {

    private GameHelper helper = new GameHelper();
    private ArrayList<DotCom> dotComList = new ArrayList<DotCom>();
    private int numOfGuess;

    private void setUpgame(){
        // first make some dot coms and give them locations
        DotCom one = new DotCom();
        one.setName("Pets.com");
        DotCom two = new DotCom();
        one.setName("eToys.com");
        DotCom three = new DotCom();
        one.setName("Go2.com");
        dotComList.add(one);
        dotComList.add(two);
        dotComList.add(three);

        System.out.println("Your goal is to sink three dot com.");
        System.out.println("Pets.com, eToys.com, Go2.com");
        System.out.println("Try to sink them all with the fewest number of guesses");

        for(DotCom dotComToSet: dotComList){
            ArrayList<String> newLocation = helper.placeDotCom(3);
            dotComToSet.setLocationCells(newLocation);
        }

    }

    private void startPlaying(){
        while(!dotComList.isEmpty()){
            String userGuess = helper.getUserInput("Enter a guess");
            checkUserGuess(userGuess);
        }
        finishGame();
    }
    private void checkUserGuess(String userGuess){
        numOfGuess++;
        String result = "miss";
        for( DotCom dotComToTest : dotComList){
            result = dotComToTest.checkYourself(userGuess);
            if(result.equals("hit")){
                break;
            }
            if(result.equals("kill")){
                dotComList.remove(dotComToTest);
                break;
            }
        }
        System.out.println(result);
    }

    private void finishGame(){
        System.out.println("All Dot Coms are dead! Your stock is now worthless");
        if(numOfGuess <= 18){
            System.out.println("It  only took you " + numOfGuess + " guesses.");
            System.out.println("you got out before your opinions sank");
        } else {
            System.out.println("Took you long enough "+ numOfGuess + " guesss." );
            System.out.println("Fish are dancing with your options");
        }
    }

    public static void main(String[] args){
        DotComBurst game = new DotComBurst();
        game.setUpgame();
        game.startPlaying();
    }
}