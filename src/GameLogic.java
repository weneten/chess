
import java.util.Map;

public class GameLogic {

    public boolean makeMove(String piece, String toField, Map<String, String> currentMap,
            Map<String, String> movesMap, String turn) {
        // TODO game Logic for moving pieces

        boolean isValidMove = false;
        String capturedPiece = null;

        // is valid move?
        String pieceAtDestination = getKeyByValue(currentMap, toField);

        if (turn.equals("WHITE") && piece.contains("B")) { // return false if wrong color
            return false;
        } else if (turn.equals("BLACK") && piece.contains("W")) {
            return false;
        }

        // early return if same color piece at destination
        if (pieceAtDestination != null && piece.charAt(0) == pieceAtDestination.charAt(0)) {
            return false;
        }

        if (piece.startsWith("WP")) { // white pawn
            boolean hasPawnMoved = pieceHasMoved(piece, movesMap);

            if (pieceAtDestination != null) { // only diagonal capture allowed
                isValidMove = toField.equals(move(currentMap.get(piece), 1, 1))
                        || toField.equals(move(currentMap.get(piece), 1, -1));
                if (isValidMove) {
                    capturedPiece = pieceAtDestination; // capture
                }
            } else { // normal move forward
                isValidMove = toField.equals(move(currentMap.get(piece), 1, 0));
                if (!hasPawnMoved) { // first move can be 2 fields
                    int[][] directions = {{0, 1}};

                    if (checkPathClear(currentMap.get(piece), toField, directions, currentMap)) {
                        isValidMove = isValidMove || toField.equals(move(currentMap.get(piece), 2, 0));
                    }
                }
            }
        } else if (piece.startsWith("BP")) { // black pawn
            boolean hasPawnMoved = pieceHasMoved(piece, movesMap);

            if (pieceAtDestination != null) { // only diagonal capture allowed
                isValidMove = toField.equals(move(currentMap.get(piece), -1, 1))
                        || toField.equals(move(currentMap.get(piece), -1, -1));
                if (isValidMove) {
                    capturedPiece = pieceAtDestination; // capture
                }
            } else { // normal move forward
                isValidMove = toField.equals(move(currentMap.get(piece), -1, 0));
                if (!hasPawnMoved) { // first move can be 2 fields
                    int[][] directions = {{0, -1}};

                    if (checkPathClear(currentMap.get(piece), toField, directions, currentMap)) {
                        isValidMove = isValidMove || toField.equals(move(currentMap.get(piece), -2, 0));
                    }
                }
            }
        } else if (piece.contains("R")) { // Rook
            if (straightLineMove(piece, toField, currentMap)) {
                if (pieceAtDestination != null) {
                    capturedPiece = pieceAtDestination; // capture
                }
                isValidMove = true; // Valid rook move
            }
        } else if (piece.contains("N")) { // Knight
            String fromField = currentMap.get(piece);
            if (fromField != null) {
                char fromCol = fromField.charAt(0);
                char fromRow = fromField.charAt(1);
                char toCol = toField.charAt(0);
                char toRow = toField.charAt(1);

                int colDiff = Math.abs(toCol - fromCol);
                int rowDiff = Math.abs(toRow - fromRow);

                // Check for "L" shape move: 2 in one direction and 1 in the other
                if ((colDiff == 2 && rowDiff == 1) || (colDiff == 1 && rowDiff == 2)) {
                    if (pieceAtDestination != null) {
                        capturedPiece = pieceAtDestination; // capture
                    }
                    isValidMove = true; // Valid knight move
                }
            }
        } else if (piece.contains("B")) { // Bishop
            if (diagonalLineMove(piece, toField, currentMap)) {
                if (pieceAtDestination != null) {
                    capturedPiece = pieceAtDestination; // capture
                }
                isValidMove = true; // Valid bishop move
            }
        } else if (piece.contains("Q")) {
            if (straightLineMove(piece, toField, currentMap) || diagonalLineMove(piece, toField, currentMap)) {
                if (pieceAtDestination != null) {
                    capturedPiece = pieceAtDestination; // capture
                }
                isValidMove = true; // Valid queen move
            }
        } else if (piece.contains("K")) { // King
            String fromField = currentMap.get(piece);
            if (fromField != null) {
                char fromCol = fromField.charAt(0);
                char fromRow = fromField.charAt(1);
                char toCol = toField.charAt(0);
                char toRow = toField.charAt(1);

                int colDiff = Math.abs(toCol - fromCol);
                int rowDiff = Math.abs(toRow - fromRow);

                // King can move one square in any direction
                if (colDiff <= 1 && rowDiff <= 1 && (colDiff + rowDiff > 0)) {
                    if (pieceAtDestination != null) {
                        capturedPiece = pieceAtDestination; // capture
                    }
                    isValidMove = true; // Valid king move
                } else if (colDiff == 2 && rowDiff == 0) {
                    isValidMove = castlingPossible(piece, movesMap, currentMap, toField);
                }
            }
        }

        // Check for en passant
        if (!isValidMove && enPassantPossible(piece, toField, currentMap, movesMap)) {
            isValidMove = true;
            if (movesMap.isEmpty()) {
                // safety check, should not happen as enPassantPossible would return false
            } else {
                String lastMove = null;
                for (String move : movesMap.values()) {
                    lastMove = move; // Letztes Move holen
                }
                if (lastMove != null && lastMove.length() >= 4) {
                    capturedPiece = lastMove.substring(0, 3); // e.g. "WP1"
                }
            }
        }

        if (isValidMove) { // If capture, remove the piece (empty targets need no action)
            if (capturedPiece != null) {
                System.out.println(capturedPiece + " geschlagen.");
                currentMap.remove(capturedPiece);
            }
            currentMap.put(piece, toField);
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

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean pieceHasMoved(String piece, Map<String, String> movesMap) {
        for (String move : movesMap.values()) {
            if (move.startsWith(piece)) {
                return true; // piece was moved
            }
        }
        return false; // Pawn was never moved before
    }

    private boolean enPassantPossible(String pawn, String toField, Map<String, String> currentMap,
            Map<String, String> movesMap) {
        if (movesMap.isEmpty()) {
            return false; // No moves made yet
        }

        String lastMove = null;

        for (String move : movesMap.values()) {
            lastMove = move; // Get the last move
        }

        if (lastMove == null || lastMove.length() < 4) {
            return false;
        }

        String lastPiece = lastMove.substring(0, 3); // e.g. "WP1"
        String lastToField = lastMove.substring(3); // e.g. "A4"

        if (!(lastPiece.startsWith("WP") || lastPiece.startsWith("BP"))) {
            return false;
        }

        char lastCol = lastToField.charAt(0);
        char lastRow = lastToField.charAt(1);

        boolean twoStepMove = false;
        if (lastPiece.startsWith("WP") && lastRow == '4') {
            twoStepMove = true; // white pawn moved 2 squares (2 -> 4)
        } else if (lastPiece.startsWith("BP") && lastRow == '5') {
            twoStepMove = true; // black pawn moved 2 squares (7 -> 5)
        }

        if (!twoStepMove) {
            return false;
        }

        String myPos = currentMap.get(pawn);

        if (myPos == null) {
            return false; // Pawn not found on the board
        }

        char myCol = myPos.charAt(0);
        char myRow = myPos.charAt(1);

        boolean adjacentFile = Math.abs(myCol - lastCol) == 1; // Check if pawns are on adjacent files
        if (!adjacentFile) {
            return false;
        }

        // Must be on the same row as the pawn that just moved e.g 5
        if (myRow != lastRow) {
            return false;
        }

        // Check if (toField) matches the en passant capture square
        if (pawn.startsWith("WP")) {
            // White captures upwards
            String targetSquare = "" + lastCol + (char) (lastRow + 1);
            return toField.equals(targetSquare);
        } else if (pawn.startsWith("BP")) {
            // Black captures downwards
            String targetSquare = "" + lastCol + (char) (lastRow - 1);
            return toField.equals(targetSquare);
        }

        return false;
    }

    private boolean castlingPossible(String piece, Map<String, String> movesMap,
            Map<String, String> currentMap, String toField) {
        if (pieceHasMoved(piece, movesMap)) {
            return false; // King has moved
        }

        if (piece.equals("WK")) { // Queenside castling (O-O-O)
            if (toField.equals("C1")) {
                String rook = getKeyByValue(currentMap, "A1");
                if (rook != null && rook.equals("WR1") && !pieceHasMoved("WR1", movesMap)) {
                    
                    // Check if squares B1, C1, D1 are empty
                    if (!currentMap.containsValue("B1") && !currentMap.containsValue("C1") && !currentMap.containsValue("D1")) {
                        // TODO: Add check for king not being in check, not passing through check

                        currentMap.put("WR1", "D1"); // Move the rook from A1 to D1
                        return true;
                    }
                }
            } else if (toField.equals("G1")) { // Kingside castling (O-O)
                String rook = getKeyByValue(currentMap, "H1");
                if (rook != null && rook.equals("WR2") && !pieceHasMoved("WR2", movesMap)) {
                    
                    // Check if squares F1, G1 are empty
                    if (!currentMap.containsValue("F1") && !currentMap.containsValue("G1")) {
                        // TODO: Add check for king not being in check, not passing through check

                        currentMap.put("WR2", "F1"); // Move the rook from H1 to F1
                        return true;
                    }
                }
            }
        }

        if (piece.equals("BK")) { // Queenside castling (O-O-O)
            if (toField.equals("C8")) {
                String rook = getKeyByValue(currentMap, "A8");
                if (rook != null && rook.equals("BR1") && !pieceHasMoved("BR1", movesMap)) {

                    // Check if squares B8, C8, D8 are empty
                    if (!currentMap.containsValue("B8") && !currentMap.containsValue("C8") && !currentMap.containsValue("D8")) {
                        // TODO: Add check for king not being in check, not passing through check

                        currentMap.put("BR1", "D8"); // Move the rook from A8 to D8
                        return true;
                    }
                }
            } else if (toField.equals("G8")) { // Kingside castling (O-O)
                String rook = getKeyByValue(currentMap, "H8");
                if (rook != null && rook.equals("BR2") && !pieceHasMoved("BR2", movesMap)) {

                    // Check if squares F8, G8 are empty
                    if (!currentMap.containsValue("F8") && !currentMap.containsValue("G8")) {
                        // TODO: Add check for king not being in check, not passing through check

                        currentMap.put("BR2", "F8"); // Move the rook from H8 to F8
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean straightLineMove(String piece, String toField, Map<String, String> currentMap) {
        String fromField = currentMap.get(piece);
        if (fromField == null) {
            return false; // Piece not found on the board
        }
        char fromCol = fromField.charAt(0);
        char fromRow = fromField.charAt(1);
        char toCol = toField.charAt(0);
        char toRow = toField.charAt(1);

        // Check if moving in a straight line (same row or same column)
        if (fromCol == toCol || fromRow == toRow) {
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            if (checkPathClear(fromField, toField, directions, currentMap)) {
                return true; // Valid straight line move
            }
        }
        return false; // Not a straight line move
    }

    private boolean diagonalLineMove(String piece, String toField, Map<String, String> currentMap) {
        String fromField = currentMap.get(piece);
        if (fromField == null) {
            return false;
        }

        char fromCol = fromField.charAt(0);
        char fromRow = fromField.charAt(1);
        char toCol = toField.charAt(0);
        char toRow = toField.charAt(1);

        int colDiff = Math.abs(toCol - fromCol);
        int rowDiff = Math.abs(toRow - fromRow);

        // Check for diagonal move: colDiff must equal rowDiff
        if (colDiff == rowDiff && colDiff > 0) {
            int[][] diagonalDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

            if (checkPathClear(fromField, toField, diagonalDirections, currentMap)) {
                return true; // Valid diagonal move
            }
        }
        return false; // Not a diagonal move
    }

    private boolean checkPathClear(String fromField, String toField, int[][] directions, Map<String, String> currentMap) {
        // Check if the path is clear for Pawn, Rook, Bishop, Queen
        char fromCol = fromField.charAt(0);
        char fromRow = fromField.charAt(1);
        char toCol = toField.charAt(0);
        char toRow = toField.charAt(1);

        int colDiff = toCol - fromCol; // e.g. Rook A4 to A7 -> 0
        int rowDiff = toRow - fromRow; // e.g. Rook A4 to A7 -> 3

        int dCol = Integer.signum(colDiff); // -1 , 0 , 1
        int dRow = Integer.signum(rowDiff); // -1 , 0 , 1

        boolean validDirection = false;
        for (int[] dir : directions) { // Check if direction is valid
            if (dir[0] == dCol && dir[1] == dRow) {
                validDirection = true;
                break;
            }
        }

        if (!validDirection) {
            return false; // Invalid direction
        }

        int col = fromCol + dCol;
        int row = fromRow + dRow;

        while (col != toCol || row != toRow) { // Move step by step
            if (col < 'A' || col > 'H' || row < '1' || row > '8') {
                return false; // Out of board
            }
            String square = "" + (char) col + (char) row;

            if (currentMap.containsValue(square)) {
                return false; // Path is blocked
            }

            col += dCol;
            row += dRow;
        }

        return true;
    }
}
