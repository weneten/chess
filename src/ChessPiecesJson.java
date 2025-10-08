import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import jsonIO.JSONLoaderWriter;

public class ChessPiecesJson {

    static String turn = "WHITE";
    static int turnCounter;
    static Map<String, String> currentMap = new LinkedHashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        // ### VARS ###
        JSONLoaderWriter jsonLoader = new JSONLoaderWriter();
        GameLogic gameLogic = new GameLogic();
        Map<String, String> movesMap;
        boolean gameRunning = true;

        // User makes a Choice what to do
        String choice = makeChoice();

        // Switch case?
        switch (choice) {
            case "LOAD" -> {
                String loaded = Files.readString(Paths.get("moves.json"));
                movesMap = jsonLoader.parseJSON(loaded);
                currentMap = new LinkedHashMap<>(startPieces());
                if (movesMap.size() % 2 == 0) {
                    turn = "WHITE";
                } else {
                    turn = "BLACK";
                }   turnCounter = movesMap.size() + 1;
                System.out.println("Spiel geladen. Du bist am Zug.\n");
                movesMap.forEach((k, v) -> {
                    String piece = v.substring(0, v.length() - 2);
                    String field = v.length() >= 2 ? v.substring(v.length() - 2) : "";
                    
                    String pieceAtDestination = GameLogic.getKeyByValue(currentMap, field); // Check if capture
                    if (pieceAtDestination != null && !pieceAtDestination.equals(piece)) { // Avoid self-capture
                        System.out.println("Beim Laden: " + pieceAtDestination + " von " + field + " entfernt (Capture).");
                        currentMap.remove(pieceAtDestination);
                    }
                    
                    currentMap.put(piece, field);
                }); System.out.println("Aktuelle Position der Figuren:\n" + currentMap + "\n");
            }
            case "NEW" -> {
                // make empty movesmap and save start positions of the pieces
                movesMap = new LinkedHashMap<>();
                currentMap = new LinkedHashMap<>(startPieces());
                jsonLoader.writeToJSON(movesMap);
                System.out.println("Neues Spiel gestartet. Du bist am Zug.\n");
                turnCounter = 1;
            }
            case "EXIT" -> {
                System.out.println("Programm beendet.");
                scanner.close();
                return;
            }
            default -> {
                System.out.println("Ungültige Eingabe. Programm beendet.");
                scanner.close();
                return;
            }
        }

        while (gameRunning) {

            String move = getUserMove();

            if (move.equals("EXIT")) {
                System.out.println("Spiel beendet.");
                gameRunning = false;
                continue;
            }

            try {
                String piece = move.substring(0, move.length() - 2);
                String toField = move.length() >= 2 ? move.substring(move.length() - 2) : "";

                if (gameLogic.makeMove(piece, toField, currentMap, movesMap, turn)) {
                    movesMap.put(String.valueOf(turnCounter), move);
                    System.out.println("Zug " + turnCounter + ": " + move);
                    turnCounter++;
                    jsonLoader.writeToJSON(movesMap); // Speichern nach Validierung
                } else {
                    System.out.println("Ungültiger Zug. Versuche es erneut.");
                    continue;
                }
            } catch (IOException e) {
                System.out.println("Ungültige Eingabe. Versuche es erneut.");
                continue;
            }

            if (turn.equals("WHITE")) {
                turn = "BLACK";
            } else {
                turn = "WHITE";
            }
        }
        scanner.close();
    }

    private static String getUserMove() {
        if (turn.equals("BLACK")) {
            System.out.print("Schwarz zieht: ");
        } else {
            System.out.print("Weiß zieht: ");
        }
        String move = scanner.next().toUpperCase().trim();
        scanner.nextLine();
        return move;
    }

    private static Map<String, String> startPieces() { // refactored just for Bothe
        
        char[] files = "ABCDEFGH".toCharArray();

        // Weiße Figuren hinten
        String[] whiteBack = { "WR1", "WN1", "WB1", "WQ", "WK", "WB2", "WN2", "WR2" };
        for (int i = 0; i < files.length; i++) {
            currentMap.put(whiteBack[i], files[i] + "1");
        }

        // Weiße Bauern
        for (int i = 0; i < files.length; i++) {
            currentMap.put("WP" + (i + 1), files[i] + "2");
        }

        // Schwarze Bauern
        for (int i = 0; i < files.length; i++) {
            currentMap.put("BP" + (i + 1), files[i] + "7");
        }

        // Schwarze Figuren hinten
        String[] blackBack = { "BR1", "BN1", "BB1", "BQ", "BK", "BB2", "BN2", "BR2" };
        for (int i = 0; i < files.length; i++) {
            currentMap.put(blackBack[i], files[i] + "8");
        }

        System.out.println("Startaufstellung der Figuren:\n" + currentMap + "\n");
        return currentMap;
    }

    private static String makeChoice() {

        System.out.println("""
                           M\u00f6chtest du ein Spiel starten oder laden? (New/Load)
                           Gib 'exit' ein, um das Programm zu beenden.
                           """);
        return scanner.nextLine().toUpperCase().trim();
    }
}
