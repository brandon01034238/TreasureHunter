import java.awt.*;
import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    OutputWindow window = new OutputWindow();
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
    }

    public boolean isEasyMode() {
        return easyMode;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        window.addTextToWindow("\nWelcome to ", Color.black);
        window.addTextToWindow("TREASURE HUNTER", Color.ORANGE);
        window.addTextToWindow("\nGoing hunting for the big ", Color.black);
        window.addTextToWindow("treasure ", Color.ORANGE);
        window.addTextToWindow("eh?", Color.black);
        window.addTextToWindow("\nWhat's your name, ", Color.black);
        window.addTextToWindow("Hunter", Color.red);
        window.addTextToWindow("?", Color.black);
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 20);


        window.clear();
        window.addTextToWindow("Which mode? (e/n/h): ", Color.black);
        String hard = SCANNER.nextLine().toLowerCase();
        window.clear();
        if (hard.equals("y")) {
            hardMode = true;
        } else if (hard.equals("test")) {
            hunter.changeGold(124);
            hunter.buyItem("horse",12 );
            hunter.buyItem("water",2 );
            hunter.buyItem("rope", 4 );
            hunter.buyItem("machete",6 );
            hunter.buyItem("boat",20 );
        } else if (hard.equals("e")){
            hunter.changeGold(20);
            easyMode = true;
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode){
            toughness = 0.02;
            markdown = 1;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, window);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow(currentTown.getLatestNews(), Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("***", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow(hunter.infoString(), Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow(currentTown.infoString(), Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(B)uy something at the shop.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(S)ell something at the shop.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(E)xplore surrounding terrain.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(M)ove on to a different town.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(L)ook for trouble!", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(H)unt for treasure.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("(D)ig for treasure", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("Give up the hunt and e(X)it.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("What's your next move? ", Color.black);
            choice = SCANNER.nextLine().toLowerCase();
            window.clear();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("e")) {
            window.addTextToWindow(currentTown.getTerrain().infoString(), Color.black);
            window.addTextToWindow("\n", Color.black);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                window.addTextToWindow(currentTown.getLatestNews(), Color.black);
                window.addTextToWindow("\n", Color.black);
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")) {
            currentTown.searchForTreasure();
        }  else if (choice.equals("x")) {
            window.addTextToWindow("Fare thee well, " + hunter.getHunterName() + "!", Color.black);
            window.addTextToWindow("\n", Color.black);
        } else {
            window.addTextToWindow("Yikes! That's an invalid option! Try again.", Color.black);
            window.addTextToWindow("\n", Color.black);
        }
    }
}