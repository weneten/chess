import java.util.Map;

public class GameLogic {

    public boolean makeMove(String piece, String toField, Map<String, String> currentMap,
            Map<String, String> movesMap) {
        // TODO game Logic for moving pieces

        boolean isValidMove = false;
        String capturedPiece = null;

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
                if (isValidMove) {
                    capturedPiece = pieceAtDestination; // capture
                }
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
                if (isValidMove) {
                    capturedPiece = pieceAtDestination; // capture
                }
            } else {
                // normal move forward)
                isValidMove = toField.equals(move(currentMap.get(piece), -1, 0));
                if (!hasPawnMoved) {
                    // first move can be 2 fields
                    isValidMove = isValidMove || toField.equals(move(currentMap.get(piece), -2, 0));
                }
            }
        } else if (piece.contains("R")) { //Rook
            String fromField = currentMap.get(piece);
            if (fromField != null) {
                char fromCol = fromField.charAt(0);
                char fromRow = fromField.charAt(1);
                char toCol = toField.charAt(0);
                char toRow = toField.charAt(1);

                // Check if moving in a straight line (same row or same column)
                if (fromCol == toCol || fromRow == toRow) {
                    int[][] rookDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

                    if (checkPathClear(fromField, toField, rookDirections, currentMap)) {
                        if (pieceAtDestination != null) {
                            capturedPiece = pieceAtDestination; // capture
                        }
                        isValidMove = true; // Valid rook move
                    }
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
                    capturedPiece = lastMove.substring(0, 3); // z.B. "BP1" als capturedPiece
                }
            }
        }
        // TODO add other pieces

        if (isValidMove) {
            // If capture, remove the piece (empty targets need no action)
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

    private boolean pawnHasMoved(String pawn, Map<String, String> movesMap) {
        for (String move : movesMap.values()) {
            if (move.startsWith(pawn)) {
                return true; // Der Pawn wurde schon bewegt
            }
        }
        return false; // Kein Move gefunden -> noch nicht bewegt
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

        if (lastMove == null || lastMove.length() < 4)
            return false;

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

        if (!twoStepMove)
            return false;

        String myPos = currentMap.get(pawn);

        if (myPos == null) {
            return false; // Pawn not found on the board
        }

        char myCol = myPos.charAt(0);
        char myRow = myPos.charAt(1);

        boolean adjacentFile = Math.abs(myCol - lastCol) == 1; // Check if pawns are on adjacent files
        if (!adjacentFile)
            return false;

        // Must be on the same row as the pawn that just moved e.g 5
        if (myRow != lastRow)
            return false;

        // Now check if our move (toField) matches the en passant capture square
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
    
    private boolean checkPathClear(String fromField, String toField, int[][] directions, Map<String, String> currentMap) {
        // Check if the path is clear for Pawn, Rook, Bishop, Queen
        char fromCol = fromField.charAt(0);
        char fromRow = fromField.charAt(1);
        char toCol = toField.charAt(0);
        char toRow = toField.charAt(1);

        int colDiff = toCol - fromCol;
        int rowDiff = toRow - fromRow;

        int dCol = Integer.signum(colDiff); // -1 , 0 , 1
        int dRow = Integer.signum(rowDiff); // -1 , 0 , 1

        boolean validDirection = false;
        for (int[] dir : directions) {
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

        while (col != toCol || row != toRow) {
            String square = "" + (char) col + (char) row;

            if (currentMap.containsValue(square)) {
                return false; // Path is blocked
            }

            col += dCol;
            row += dRow;
        }

        if (col < 'A' || col > 'H' || row < '1' || row > '8') {
            return false; // Out of board
        }

        return true;
    }
}