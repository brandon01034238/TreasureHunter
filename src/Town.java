import java.sql.SQLOutput;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean searched = true;
    private String treasure;
    private boolean dug;
    private TreasureHunter th;
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, TreasureHunter th) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.th = th;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if(!th.isEasyMode()) {
                if (checkItemBreak()) {
                    hunter.removeItemFromKit(item);
                    printMessage += "\nUnfortunately, your " + item + " broke.";
                }
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + Colors.RESET + Colors.YELLOW + goldDiff + Colors.RESET + Colors.RED + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + Colors.RESET + Colors.YELLOW + Colors.RESET + goldDiff + Colors.RED + " gold." + Colors.RESET;
                hunter.changeGold(-goldDiff);

            }
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int) (Math.random()* 5) +1;
        if (rnd < 1) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < 2) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < 3) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < 4) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 5){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public void searchForTreasure() {
        if (searched) {
            if (searched) {
                String treasure = "";
                double noTreasureChance;
                double treasureChance = Math.random();
                if (toughTown) {
                    noTreasureChance = .66;
                } else {
                    noTreasureChance = .33;
                }
                if (treasureChance > noTreasureChance) {
                    System.out.println("\nYou found dust");
                } else {
                    System.out.println("\nYou found treasure");
                    if (treasureChance < .33) {
                        treasure = "crown";
                        System.out.println("\nYou found a crown!");
                    } else if (treasureChance > .33 && treasureChance < .66) {
                        treasure = "trophy";
                        System.out.println("\nYou found a trophy");
                    } else if (treasureChance > .66) {
                        treasure = "gem";
                        System.out.println("\nYou found a gem");
                    }
                } if (!hunter.hasTreasuresInKit(treasure)) {
                    hunter.addTreasure(treasure);
                }
            }
            searched = false;
        } System.out.println("\nThis town is searched");
    }

    public void digForTreasure() {
        if (!dug) {
            if (!hunter.hasItemInKit("shovel")) {
                System.out.println("\nYou must acquire a shovel to dig");
            } else if (hunter.hasItemInKit("shovel")) {
                double chance = Math.random();
                if (chance > .49) {
                    double gold = (int) (Math.random() * 20) + 1;
                    hunter.changeGold((int) + gold);
                    System.out.println("\nYou dug up " + gold + " gold");
                } else {
                    System.out.println("\nYou didn't dig up any gold");
                }
                dug = true;
            }
        } else {
            System.out.println("\nThis town is dug");
        }
    }
}



