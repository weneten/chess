import jsonIO.JSONLoaderWriter;
import java.nio.file.*;
import java.util.*;

public class ChessPiecesJson {

    static String turn = "WHITE";
    static int turnCounter;
    static Map<String, String> currentMap = new LinkedHashMap<>();

    public static void main(String[] args) throws Exception {

        // ### VARS ###
        JSONLoaderWriter jsonLoader = new JSONLoaderWriter();
        GameLogic gameLogic = new GameLogic();
        Map<String, String> movesMap;
        boolean gameRunning = true;

        // User makes a Choice what to do
        String choice = makeChoice();

        // Switch case?
        if (choice.equals("LOAD")) {

            String loaded = Files.readString(Paths.get("moves.json"));
            movesMap = jsonLoader.parseJSON(loaded);

            currentMap = new LinkedHashMap<>(startPieces());

            if (movesMap.size() % 2 == 0) {
                turn = "WHITE";
            } else {
                turn = "BLACK";
            }
            turnCounter = movesMap.size() + 1;
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
            });
            System.out.println("Aktuelle Position der Figuren:\n" + currentMap + "\n");

        } else if (choice.equals("NEW")) {

            // make empty movesmap and save start positions of the pieces
            movesMap = new LinkedHashMap<>();
            currentMap = new LinkedHashMap<>(startPieces());

            jsonLoader.writeToJSON(movesMap);
            System.out.println("Neues Spiel gestartet. Du bist am Zug.\n");

            turnCounter = 1;

        } else if (choice.equals("EXIT")) {
            System.out.println("Programm beendet.");
            return;
        } else {
            System.out.println("Ungültige Eingabe. Programm beendet.");
            return;
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

                if (gameLogic.makeMove(piece, toField, currentMap, movesMap)) {
                    movesMap.put(String.valueOf(turnCounter), move);
                    System.out.println("Zug " + turnCounter + ": " + move);
                    turnCounter++;
                    jsonLoader.writeToJSON(movesMap); // Speichern nach Validierung
                } else {
                    System.out.println("Ungültiger Zug. Versuche es erneut.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Ungültige Eingabe. Versuche es erneut.");
                continue;
            }

            if (turn.equals("WHITE")) {
                turn = "BLACK";
            } else {
                turn = "WHITE";
            }

        }

    }

    private static String getUserMove() {
        Scanner scanner = new Scanner(System.in);
        if (turn.equals("BLACK")) {
            System.out.print("Schwarz zieht: ");
        } else {
            System.out.print("Weiß zieht: ");
        }
        String move = scanner.next().toUpperCase().trim();
        scanner.nextLine();
        return move;
    }

    private static Map<String, String> startPieces() {
        currentMap.put("WR1", "A1");
        currentMap.put("WN1", "B1");
        currentMap.put("WB1", "C1");
        currentMap.put("WQ", "D1");
        currentMap.put("WK", "E1");
        currentMap.put("WB2", "F1");
        currentMap.put("WN2", "G1");
        currentMap.put("WR2", "H1");
        currentMap.put("WP1", "A2");
        currentMap.put("WP2", "B2");
        currentMap.put("WP3", "C2");
        currentMap.put("WP4", "D2");
        currentMap.put("WP5", "E2");
        currentMap.put("WP6", "F2");
        currentMap.put("WP7", "G2");
        currentMap.put("WP8", "H2");

        currentMap.put("BP1", "A7");
        currentMap.put("BP2", "B7");
        currentMap.put("BP3", "C7");
        currentMap.put("BP4", "D7");
        currentMap.put("BP5", "E7");
        currentMap.put("BP6", "F7");
        currentMap.put("BP7", "G7");
        currentMap.put("BP8", "H7");
        currentMap.put("BR1", "A8");
        currentMap.put("BN1", "B8");
        currentMap.put("BB1", "C8");
        currentMap.put("BQ", "D8");
        currentMap.put("BK", "E8");
        currentMap.put("BB2", "F8");
        currentMap.put("BN2", "G8");
        currentMap.put("BR2", "H8");

        System.out.println("Startaufstellung der Figuren:\n" + currentMap + "\n");

        return currentMap;
    }

    private static String makeChoice() {

        System.out.println("Möchtest du ein Spiel starten oder laden? (New/Load)\n" +
                "Gib 'exit' ein, um das Programm zu beenden.\n");
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine().toUpperCase().trim();
    }
}
