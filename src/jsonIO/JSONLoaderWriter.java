package jsonIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONLoaderWriter {


    /**
     * Parses a file (JSON format) into a Java usable Map
     * @param file the JSON file to format
     * @return a Map (key = piece; value = moves) that tracks the movements of the chess pieces
     */
    public Map<String, String> parseJSON(String json) throws IOException {

        //load file and write it into a String
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


    private String toJSON (Map<String, String> moves)
    {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Map.Entry<String, String>> itt = moves.entrySet().iterator();

        while (itt.hasNext()) {
            Map.Entry<String, String> e = itt.next();
            sb.append("\"").append(e.getKey()).append("\":");
            sb.append("\"").append(e.getValue()).append("\"");
            if (itt.hasNext())
                sb.append(",");
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * saves all chess pieces moves into the JSON file
     * @param movesMap - the map that saved all moves
     * @throws IOException - thrown if the file can't be opened
     */
    public void writeToJSON(Map<String, String> movesMap) throws IOException {
        String newJSON = toJSON(movesMap);
        Files.write(Paths.get("moves.json"), newJSON.getBytes());

        System.out.println("Zug in moves.json gespeichert.\n");
    }

}
