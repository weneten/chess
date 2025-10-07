# Game Logic

## Classic Chess

### Pawn

- [x] can move 1-2 Forward (if first move in game), else it can move 1 forward if forward square is empty
- [x] Pawns can never move backwards
- [x] if there is a piece diagonally (1 forward and 1 sideways) the Pawn is able to capture the opponents piece
- [x] Pawns can not jump over a piece when moving 2 steps forward

### Rook

- [x] Rooks can move any number of squares horizontally or vertically (left, right, up, down) as long as the path is clear
- [X] Rooks can capture an opponent's piece by moving to its square, but only if the path to that square is empty

### Knight

- [x] can jump over pieces
- [x] can move in an "L" shape (e.g. 2 forward and 1 left)
- [x] Knights can capture an opponent's piece by moving to its square

### Bishop

- [x] Bishops can move any number of squares diagonally as long as the path is clear
- [x] Bishops can capture an opponent's piece by moving to its square, but only if the path to that square is empty

### Queen

- [x] Queens can move any number of squares horizontally, vertically, or diagonally (like a Rook and Bishop combined) as long as the path is clear
- [x] Queens can capture an opponent's piece by moving to its square, but only if the path to that square is empty

### King

- [ ] Kings can move one square in any direction (horizontally, vertically, or diagonally) as long as the destination square is not under attack by an opponent's piece
- [ ] Kings can capture an opponent's piece by moving to its square, but only if that square is not protected by another opponent's piece

### Special Rules

- [x] En Passent
  - if a Pawn moves 2 forward and lands next to an opponent's Pawn, the opponent's Pawn can capture it as if the Pawn had only moved 1 forward (must capture immediately on the next move)
- [ ] Promotion
  - when a pawn reaches the opponent's back rank (eighth for white, first for black), it must be promoted immediately
  - it needs to be promoted to another piece (Queen, Rook, Bishop, Knight)
  - when a Pawn gets promoted it gets renamend to: "promoted piece (e.g. Q for Queen)" + "W (White) / B (Black)" + "P (Pawn)" + "ID". For example: WP1 -> QWP1
- [ ] Castling
  - a special move involving the king and one rook: the king moves two squares toward the rook, and the rook jumps to the square the king passed over (ending adjacent to the king)
  - available only if: neither piece has moved before, no pieces between them on the back rank, the squares the king passes through are not under attack, and the king is not currently in check
  - 2 types: kingside (with the h-rook) and queenside (with the a-rook)
