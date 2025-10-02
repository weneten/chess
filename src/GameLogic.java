import java.util.Map;

public class GameLogic {

    public boolean makeMove(String piece, String toField, Map<String, String> currentMap,
            Map<String, String> movesMap) {
        // TODO game Logic for moving pieces

        boolean isValidMove = false;

        // is valid move?

        String pieceAtDestination = getKeyByValue(currentMap, toField);

        // early return if same color piece at destination
        if (pieceAtDestination != null && piece.charAt(0) == pieceAtDestination.charAt(0)) {
            return false;
        }

        if (piece.startsWith("WP")) { // white pawn
            boolean hasPawnMoved = pawnHasMoved(piece, movesMap);

            if (pieceAtDestination != null) {
                // only diagonal capture allowed
                isValidMove = toField.equals(move(currentMap.get(piece), 1, 1)) ||
                        toField.equals(move(currentMap.get(piece), 1, -1));
            } else {
                // normal move forward
                isValidMove = toField.equals(move(currentMap.get(piece), 1, 0));
                if (!hasPawnMoved) {
                    // first move can be 2 fields
                    isValidMove = isValidMove || toField.equals(move(currentMap.get(piece), 2, 0));
                }
            }
        } else if (piece.startsWith("BP")) { // black pawn
            boolean hasPawnMoved = pawnHasMoved(piece, movesMap);

            if (pieceAtDestination != null) {
                // only diagonal capture allowed
                isValidMove = toField.equals(move(currentMap.get(piece), -1, 1)) ||
                        toField.equals(move(currentMap.get(piece), -1, -1));
            } else {
                // normal move forward)
                isValidMove = toField.equals(move(currentMap.get(piece), -1, 0));
                if (!hasPawnMoved) {
                    // first move can be 2 fields
                    isValidMove = isValidMove || toField.equals(move(currentMap.get(piece), -2, 0));
                }
            }
        }

        // TODO add other pieces

        if (isValidMove) {
            // If capture, remove the piece (empty targets need no action)
            if (pieceAtDestination != null) {
                System.out.println(pieceAtDestination + " geschlagen.");
                currentMap.remove(pieceAtDestination);
            }
        }

        return isValidMove;

    }

    private String move(String field, int rowDelta, int colDelta) {
        char row = field.charAt(1); // '1'..'8'
        char col = field.charAt(0); // 'A'..'H'

        char newRow = (char) (row + rowDelta);
        char newCol = (char) (col + colDelta);

        if (newRow < '1' || newRow > '8' || newCol < 'A' || newCol > 'H') {
            return null; // out of game board
        }
        return "" + newCol + newRow;
    }

    private static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean pawnHasMoved(String pawn, Map<String, String> movesMap) {
        for (String move : movesMap.values()) {
            if (move.startsWith(pawn)) {
                return true; // Der Pawn wurde schon bewegt
            }
        }
        return false; // Kein Move gefunden -> noch nicht bewegt
    }
}