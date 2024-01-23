import java.util.Scanner;

public class BattleShips {
    public static int numRows = 6;
    public static int numCols = 6;
    public static int playerShips;
    public static int computerShips;
    public static String[][] playerGrid = new String[numRows][numCols];
    public static String[][] computerGrid = new String[numRows][numCols];
    public static int[][] playerMissedGuesses = new int[numRows][numCols];
    public static int[][] computerMissedGuesses = new int[numRows][numCols];

    public static void main(String[] args) {
        System.out.println("**** Welcome to Battle Ships game ****");
        System.out.println("Right now, sea is empty\n");

        // Step 1 – Create the ocean maps
        initializeGrids();
        createOceanMaps();

        // Step 2 – Deploy player’s ships
        deployPlayerShips();

        // Step 3 - Deploy computer's ships
        deployComputerShips();

        // Step 4 Battle
        do {
            Battle();
        } while (playerShips != 0 && computerShips != 0);

        // Step 5 - Game over
        gameOver();
    }

    public static void initializeGrids() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                playerGrid[i][j] = "$";
                computerGrid[i][j] = "$";
            }
        }
    }

    public static void createOceanMaps() {
        System.out.println("Player's Ocean Map:");
        printOceanMap(playerGrid);

        System.out.println("\nComputer's Ocean Map:");
        printOceanMap(getHiddenComputerGrid());
    }

    public static String[][] getHiddenComputerGrid() {
        // Creates a copy of the computer grid where ships are hidden
        String[][] hiddenGrid = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(computerGrid[i], 0, hiddenGrid[i], 0, numCols);
            for (int j = 0; j < numCols; j++) {
                if (hiddenGrid[i][j].equals("x")) {
                    hiddenGrid[i][j] = "$";
                }
            }
        }
        return hiddenGrid;
    }

    public static void printOceanMap(String[][] grid) {
        System.out.println();
        // First section of Ocean Map
        System.out.print("  ");
        for (int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        // Middle section of Ocean Map
        for (int x = 0; x < grid.length; x++) {
            System.out.print(x + "|");

            for (int y = 0; y < grid[x].length; y++) {
                System.out.print(grid[x][y]);
            }

            System.out.println("|" + x);
        }

        // Last section of Ocean Map
        System.out.print("  ");
        for (int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }

    public static void deployPlayerShips() {
        Scanner input = new Scanner(System.in);

        System.out.println("\nDeploy your ships:");
        // Deploying five ships for player
        playerShips = 5;
        for (int i = 1; i <= playerShips; ) {
            System.out.print("Enter X coordinate for your " + i + " ship: ");
            int x = input.nextInt();
            System.out.print("Enter Y coordinate for your " + i + " ship: ");
            int y = input.nextInt();

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (playerGrid[x][y].equals("$"))) {
                playerGrid[x][y] = "@";
                i++;
            } else if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && playerGrid[x][y].equals("@"))
                System.out.println("You can't place two or more ships on the same location");
            else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols))
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        }
        printOceanMap(playerGrid);
    }

    public static void deployComputerShips() {
        System.out.println("\nComputer is deploying ships");
        // Deploying five ships for computer
        computerShips = 5;
        for (int i = 1; i <= computerShips; ) {
            int x = (int) (Math.random() * numRows);
            int y = (int) (Math.random() * numCols);

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (computerGrid[x][y].equals("$"))) {
                computerGrid[x][y] = "x";
                System.out.println(i + ". ship DEPLOYED");
                i++;
            }
        }
    }

    public static void Battle() {
        playerTurn();
        printOceanMap(playerGrid); // Print player's grid after player's turn

        computerTurn();
        printOceanMap(getHiddenComputerGrid()); // Print computer's grid after computer's turn

        System.out.println();
        System.out.println("Your ships: " + playerShips + " | Computer ships: " + computerShips);
        System.out.println();
    }

    public static void playerTurn() {
        System.out.println("\nYOUR TURN");
        int x = -1, y = -1;
        do {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter X coordinate: ");
            x = input.nextInt();
            System.out.print("Enter Y coordinate: ");
            y = input.nextInt();

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) // valid guess
            {
                if (computerGrid[x][y].equals("x")) // if computer ship is already there computer loses ship
                {
                    System.out.println("Boom! You sunk the ship!");
                    computerGrid[x][y] = "|"; // Hit mark
                    --computerShips;
                } else if (computerGrid[x][y].equals("@")) {
                    System.out.println("Oh no, you sunk your own ship :(");
                    computerGrid[x][y] = "";
                    --playerShips;
                } else if (computerGrid[x][y].equals("$")) {
                    System.out.println("Sorry, you missed");
                    computerGrid[x][y] = "X"; // Mark miss
                } else if (computerGrid[x][y].equals("X")) {
                    System.out.println("You attacked the same spot, which is already attacked");

                }
            } else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)) // invalid guess
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        } while ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)); // keep re-prompting till valid guess
    }

    public static void computerTurn() {
        System.out.println("\nCOMPUTER'S TURN");
        // Guess co-ordinates
        int x = -1, y = -1;
        do {
            x = (int) (Math.random() * numRows);
            y = (int) (Math.random() * numCols);

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) // valid guess
            {
                if (playerGrid[x][y].equals("@")) // if player ship is already there; player loses ship
                {
                    System.out.println("The Computer hit one of your ships!");
                    playerGrid[x][y] = "x";
                    --playerShips;
                } else if (playerGrid[x][y].equals("x")) {
                    System.out.println("The Computer hit its own undisclosed ship!");
                } else if (playerGrid[x][y].equals("$")) {
                    System.out.println("Computer missed");
                    // Saving missed guesses for computer
                    if (computerMissedGuesses[x][y] != 1) {
                        computerMissedGuesses[x][y] = 1;
                    }
                }
            }
        } while ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)); // keep re-prompting till valid guess
    }

    public static void gameOver() {
        System.out.println("Your ships: " + playerShips + " | Computer ships: " + computerShips);
        if (playerShips > 0 && computerShips <= 0)
            System.out.println("Hooray! You won the battle :)");
        else
            System.out.println("Sorry, you lost the battle");
        System.out.println();
    }
}
