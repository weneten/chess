import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ChessPiecesJson {
    static String turn = "WHITE";
    static int turnCounter;

    public static void main(String[] args) throws Exception {

        Map<String, String> movesMap;

        System.out.println("Möchtest du ein Spiel starten oder laden? (New/Load)\n" +
                "Gib 'exit' ein, um das Programm zu beenden.\n");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().toUpperCase().trim();

        if (choice.equals("LOAD")) {
            String loaded = Files.readString(Paths.get("moves.json"));
            movesMap = parseSimpleJson(loaded);
            if (movesMap.size() % 2 == 0) {
                turn = "WHITE";
            } else {
                turn = "BLACK";
            }
            turnCounter = movesMap.size() + 1;
            System.out.println("Spiel geladen. Du bist am Zug.\n");

        } else if (choice.equals("NEW")) {
            movesMap = new LinkedHashMap<>();
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
}
