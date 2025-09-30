# TODO

- Chess logic (how pieces move)
- current state of board as a map


### Pawn logic to implement

- can move 1-2 Forward (if first move in game), else it can move 1 forward if forward square is empty
- Pawns can never move backwards
- if there is a piece diagonally (1 forward and one sideways) the Pawn is able to capture the opponents piece 
- en passent
    - if a Pawn moves 2 forward and lands next to an opponent's Pawn, the opponent's Pawn can capture it as if the Pawn had only moved 1 forward (must capture immediately on the next move)
- promotion if Pawn got to the opponents last row
    - it needs to be promoted to another piece (Queen, Rook, Bishop, Knight)
    - when a Pawn gets promoted it gets renamend to: "promoted piece (e.g. Q for Queen)" + "W (White) / B (Black)" + "P (Pawn)" + "ID". For example: WP1 -> QWP1