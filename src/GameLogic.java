import java.util.Map;

public class GameLogic {

    public boolean makeMove(String piece, String toField, Map<String, String> currentMap,
            Map<String, String> movesMap) {
        // TODO game Logic for moving pieces

        boolean isValidMove = false;

        // is valid move?

        String pieceAtDestination = getKeyByValue(currentMap, toField);

        if (piece.startsWith("WP")) { // white pawn
            // valid moves for white pawn
            boolean hasPawnMoved = pawnHasMoved(piece, movesMap);
            if (hasPawnMoved) {
                if (pieceAtDestination != null) {
                    isValidMove = toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                            toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, -1));
                } else {
                    isValidMove = toField.equals(incrementRow(currentMap.get(piece), 1));
                }
                isValidMove = toField.equals(incrementRow(currentMap.get(piece), 1)) ||
                        toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                        toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, -1));
            } else {
                if (pieceAtDestination != null) {
                    isValidMove = toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                            toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, -1));
                } else {
                    isValidMove = toField.equals(incrementRow(currentMap.get(piece), 1)) ||
                            toField.equals(incrementRow(currentMap.get(piece), 2));
                    
                }
                isValidMove = toField.equals(incrementRow(currentMap.get(piece), 1)) ||
                        toField.equals(incrementRow(currentMap.get(piece), 2)) ||
                        toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                        toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, -1));
            }
        } else if (piece.startsWith("BP")) { // black pawn
            // valid moves for black pawn
            boolean hasPawnMoved = pawnHasMoved(piece, movesMap);
            if (hasPawnMoved) {
                if (pieceAtDestination != null) {
                    isValidMove = toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                            toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, -1));
                } else {
                    isValidMove = toField.equals(decrementRow(currentMap.get(piece), 1));
                }
                isValidMove = toField.equals(decrementRow(currentMap.get(piece), 1)) ||
                        toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                        toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, -1));
            } else {
                if (pieceAtDestination != null) {
                    isValidMove = toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                            toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, -1));
                } else {
                    isValidMove = toField.equals(decrementRow(currentMap.get(piece), 1)) ||
                            toField.equals(decrementRow(currentMap.get(piece), 2));
                }
                isValidMove = toField.equals(decrementRow(currentMap.get(piece), 1)) ||
                        toField.equals(decrementRow(currentMap.get(piece), 2)) ||
                        toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                        toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, -1));
            }
        }

        if (isValidMove) {
            if (pieceAtDestination != null) { // there is a piece at the destination
                if (piece.charAt(0) != pieceAtDestination.charAt(0)) { // different color pieces
                    System.out.println(pieceAtDestination + " geschlagen.");
                    currentMap.remove(pieceAtDestination);
                } else { // same color piece at destination
                    isValidMove = false;
                }
            } else {
                // no piece at the destination
                System.out.println("Keine Figur am Zielort.");
            }
        }

        return isValidMove;

    }

    private String incrementRow(String field, int increment) {
        char row = field.charAt(1);
        char newRow = (char) (row + increment);
        return field.charAt(0) + String.valueOf(newRow);
    }

    private String decrementRow(String field, int decrement) {
        char row = field.charAt(1);
        char newRow = (char) (row - decrement);
        return field.charAt(0) + String.valueOf(newRow);
    }

    private String incrementRowAndColumn(String field, int incrementRow, int incrementCol) {
        char row = field.charAt(1);
        char col = field.charAt(0);
        char newRow = (char) (row + incrementRow);
        char newCol = (char) (col + incrementCol);
        return newCol + String.valueOf(newRow);
    }

    private String decrementRowAndColumn(String field, int decrementRow, int decrementCol) {
        char row = field.charAt(1);
        char col = field.charAt(0);
        char newRow = (char) (row - decrementRow);
        char newCol = (char) (col - decrementCol);
        return newCol + String.valueOf(newRow);
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
            return true;  // Der Pawn wurde schon bewegt
        }
    }
    return false;  // Kein Move gefunden -> noch nicht bewegt
}

}