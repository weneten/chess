import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ChessPiecesJson {
    
    static String turn = "WHITE";
    static int turnCounter;
    static Map<String, String> currentMap = new LinkedHashMap<>();

    public static void main(String[] args) throws Exception {

        Map<String, String> movesMap;

        System.out.println("Möchtest du ein Spiel starten oder laden? (New/Load)\n" +
                "Gib 'exit' ein, um das Programm zu beenden.\n");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().toUpperCase().trim();

        if (choice.equals("LOAD")) {
            String loaded = Files.readString(Paths.get("moves.json"));
            movesMap = parseSimpleJson(loaded);
            currentMap = new LinkedHashMap<>(startPieces());
            if (movesMap.size() % 2 == 0) {
                turn = "WHITE";
            } else {
                turn = "BLACK";
            }
            turnCounter = movesMap.size() + 1;
            System.out.println("Spiel geladen. Du bist am Zug.\n");

            movesMap.forEach((k, v) -> {
                String piece = v.substring(0, 3);
                String field = v.substring(3, 5);
                currentMap.put(piece, field);
                currentMap.values().removeIf(f -> f.equals(field) && !currentMap.get(piece).equals(field));
            });
            System.out.println("Aktuelle Position der Figuren:\n" + currentMap + "\n");

        } else if (choice.equals("NEW")) {
            movesMap = new LinkedHashMap<>();
            currentMap = new LinkedHashMap<>(startPieces());
            String newGameJson = toJson(movesMap);
            Files.write(Paths.get("moves.json"), newGameJson.getBytes());
            System.out.println("Neues Spiel gestartet. Du bist am Zug.\n");
            turnCounter = 1;

        } else if (choice.equals("EXIT")) {
            System.out.println("Programm beendet.");
            return;
        } else {
            System.out.println("Ungültige Eingabe. Programm beendet.");
            return;
        }

        boolean gameRunning = true;

        while (gameRunning) {

            String move = getUserMove();

            if (move.equals("EXIT")) {
                System.out.println("Spiel beendet.");
                gameRunning = false;
                continue;
            }

            movesMap.put(String.valueOf(turnCounter), move);
            System.out.println("Zug " + turnCounter + ": " + move);
            turnCounter++;
            String newJSON = toJson(movesMap);
            Files.write(Paths.get("moves.json"), newJSON.getBytes());
            System.out.println("Zug in moves.json gespeichert.\n");

            if (turn.equals("WHITE")) {
                turn = "BLACK";
            } else {
                turn = "WHITE";
            }

        }

    }

    // simple JSON Serializer (nur Strings, keine Escapes)
    private static String toJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            sb.append("\"").append(e.getKey()).append("\":");
            sb.append("\"").append(e.getValue()).append("\"");
            if (it.hasNext())
                sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    // simple JSON Deserializer (nur Strings, keine Escapes)
    private static Map<String, String> parseSimpleJson(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        json = json.trim();
        if (json.startsWith("{"))
            json = json.substring(1);
        if (json.endsWith("}"))
            json = json.substring(0, json.length() - 1);

        if (json.isEmpty()) {
            return result;
        }

        for (String part : json.split(",")) {
            String[] kv = part.split(":", 2);
            String key = kv[0].replace("\"", "").trim();
            String value = kv[1].replace("\"", "").trim();
            result.put(key, value);
        }
        return result;

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
}
