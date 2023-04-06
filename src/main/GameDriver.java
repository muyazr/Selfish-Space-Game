import java.util.Scanner;
import selfish.GameEngine;
import selfish.GameException;

public class GameDriver {

    /**
     * A helper function to centre text in a longer String.
     * @param width The length of the return String.
     * @param s The text to centre.
     * @return A longer string with the specified text centred.
     */
    public static String centreString (int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    public GameDriver() {
    }

    public static void main(String[] args) throws GameException {
        GameEngine game = new GameEngine(12345L, "../../io/ActionCards.txt", "../../io/SpaceCards.txt");
    
        AsciiArt();
        System.out.println("Welcome to the game!");
        AddPlayers(game);
        game.startGame();
    }

    private static String getPlayerName(int playerNumber, Scanner scanner) {
        String playerName = "";
        
        while (playerName.isEmpty()) {
            System.out.print(String.format("Player %d name? ", playerNumber));
            
            try {
                playerName = scanner.nextLine().trim();
                if (playerName.isEmpty()) {
                    System.out.println("Name cannot be empty. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        
        return playerName;
    }
    
    private static void AddPlayers(GameEngine game) {
        Scanner scanner = new Scanner(System.in);
        int playerCount = 0;
    
        // Get names for first two players
        while (playerCount < 2) {
            String playerName = getPlayerName(playerCount + 1, scanner);
            playerCount = game.addPlayer(playerName);
        }
    
        // Get names for additional players if any
        while (playerCount < 5) {
            System.out.print("Add Another? [Y]es/[N]o ");
    
            try {
                String continueAdd = scanner.nextLine().trim();
                if (continueAdd.equalsIgnoreCase("n") || continueAdd.equalsIgnoreCase("no")) break;
                else if (!continueAdd.equalsIgnoreCase("y") && !continueAdd.equalsIgnoreCase("yes")) continue;
            
                String playerName = getPlayerName(playerCount + 1, scanner);
                playerCount = game.addPlayer(playerName);
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    
        scanner.close();
    }
    

    private static void AsciiArt() {
        System.out.println("                    00    0000  00           00    ");
        System.out.println("                    00    00                 00     ");
        System.out.println("  000000   00000    00  000000  00   000000  00    ");
        System.out.println(" 00       00   00   00    00    00  00       000000 ");
        System.out.println("  00000   0000000   00    00    00   00000   00   00");
        System.out.println("      00  00        00    00    00       00  00   00");
        System.out.println(" 000000    000000  000    00    00  000000   00   00\n");
        System.out.println("                 .-.                   ");
        System.out.println("          ,o0000o, |     Space Edition  ");
        System.out.println("        ,000000000/,                     ");
        System.out.println("        000000000/00     Original game by Ridleys Games");
        System.out.println("        0000000/'000     This implementation is by Muyaz Rahman for COMP161412 (2022/23)");
        System.out.println("        `0000/'0000'                      ");
        System.out.println("        ( `,'000P'                       ");
        System.out.println("        `-'                               ");
        System.out.println("\n | \\");
        System.out.println("=[_|H)--._____");
        System.out.println("=[+--,-------'");
        System.out.println(" [|_/\n");
    }
}