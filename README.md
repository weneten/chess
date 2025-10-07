# TODO

### Pawn logic

- [x] can move 1-2 Forward (if first move in game), else it can move 1 forward if forward square is empty
- [x] Pawns can never move backwards
- [x] if there is a piece diagonally (1 forward and 1 sideways) the Pawn is able to capture the opponents piece 
- [x] en passent
    - if a Pawn moves 2 forward and lands next to an opponent's Pawn, the opponent's Pawn can capture it as if the Pawn had only moved 1 forward (must capture immediately on the next move)
- [x] Pawns can not jump over a piece when moving 2 steps forward
- [ ] promotion if Pawn got to the opponents last row
    - it needs to be promoted to another piece (Queen, Rook, Bishop, Knight)
    - when a Pawn gets promoted it gets renamend to: "promoted piece (e.g. Q for Queen)" + "W (White) / B (Black)" + "P (Pawn)" + "ID". For example: WP1 -> QWP1

### Rook logic

- [x] Rooks can move any number of squares horizontally or vertically (left, right, up, down) as long as the path is clear
- [X] Rooks can capture an opponent's piece by moving to its square, but only if the path to that square is empty


### Knight logic

- [ ] can jump over pieces
- [ ] can move in an "L" shape (e.g. 2 forward and 1 left)
- [ ] Knights can capture an opponent's piece by moving to its square