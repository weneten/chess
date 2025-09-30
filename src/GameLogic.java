import java.util.Map;

public class GameLogic {

    public boolean makeMove(String piece, String toField, Map<String, String> currentMap) {
        //TODO game Logic for moving pieces

        boolean isValidMove = false;

        //is valid move?

        if (piece.startsWith("WP")) { //white pawn
            //valid moves for white pawn
            isValidMove = toField.equals(incrementRow(currentMap.get(piece), 1)) ||
                    toField.equals(incrementRow(currentMap.get(piece), 2)) ||
                    toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                    toField.equals(incrementRowAndColumn(currentMap.get(piece), 1, -1));
        } else if (piece.startsWith("BP")) { //black pawn
            //valid moves for black pawn
            isValidMove = toField.equals(decrementRow(currentMap.get(piece), 1)) ||
                    toField.equals(decrementRow(currentMap.get(piece), 2)) ||
                    toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, 1)) ||
                    toField.equals(decrementRowAndColumn(currentMap.get(piece), 1, -1));
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
}