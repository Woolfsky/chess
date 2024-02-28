package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    public ChessGame.TeamColor team;
    public ChessPiece.PieceType type;
    public Collection<ChessMove> moves = new HashSet<>();

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.team = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType { KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        if (team != that.team) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = team != null ? team.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        this.moves = new HashSet<>();
        if (this.type == PieceType.BISHOP) {
            addBishopMoves(board, myPosition);
        }
        if (this.type == PieceType.KING) {
            addKingMoves(board, myPosition);
        }
        if (this.type == PieceType.KNIGHT) {
            addKnightMoves(board, myPosition);
        }
        if (this.type == PieceType.PAWN) {
            addPawnMoves(board, myPosition);
        }
        if (this.type == PieceType.ROOK) {
            addRookMoves(board, myPosition);
        }
        if (this.type == PieceType.QUEEN) {
            addRookMoves(board, myPosition);
            addBishopMoves(board, myPosition);
        }
        return moves;
    }

    @Override
    public String toString() {
        return String.valueOf(this.team.name().charAt(0)) + String.valueOf(this.type.name().charAt(0));
    }

    public boolean inBounds(int row, int col) {
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            return true;
        }
        return false;
    }

    public boolean isBlank(int row, int col, ChessBoard board) {
        ChessPosition pos = new ChessPosition(row, col);
        if (board.getPiece(pos) == null) {
            return true;
        }
        return false;
    }

    public boolean isEnemy(int row, int col, ChessBoard board) {
        ChessPosition pos = new ChessPosition(row, col);
        if (board.getPiece(pos).getTeamColor() != this.team) {
            return true;
        }
        return false;
    }

    public void addMove(int row, int col, ChessPosition myPosition) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessMove move = new ChessMove(myPosition, pos, null);
        this.moves.add(move);
    }

    public void addBishopMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        // up left
        tempRow = row + 1;
        tempCol = col - 1;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempRow++;
            tempCol--;
        }
        // up right
        tempRow = row + 1;
        tempCol = col + 1;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempRow++;
            tempCol++;
        }
        // down left
        tempRow = row - 1;
        tempCol = col - 1;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempRow--;
            tempCol--;
        }
        // down right
        tempRow = row - 1;
        tempCol = col + 1;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempRow--;
            tempCol++;
        }
    }

    public void addKingMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        // up
        tempRow = row + 1;
        tempCol = col;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // up, left
        tempRow = row + 1;
        tempCol = col - 1;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // left
        tempRow = row;
        tempCol = col - 1;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // left down
        tempRow = row - 1;
        tempCol = col - 1;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // down
        tempRow = row - 1;
        tempCol = col;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // down right
        tempRow = row - 1;
        tempCol = col + 1;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // right
        tempRow = row;
        tempCol = col + 1;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
        // right up
        tempRow = row + 1;
        tempCol = col + 1;
        checkMakeKingMoves(tempRow, tempCol, board, myPosition);
    }

    public void checkMakeKingMoves(int tempRow, int tempCol, ChessBoard board, ChessPosition myPosition) {
        if (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                }
            }
        }
    }

    public void checkMakeKnightMoves(int tempRow, int tempCol, ChessBoard board, ChessPosition myPosition) {
        if (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                }
            }
        }
    }

    public void addKnightMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        // two up, left
        tempRow = row + 2;
        tempCol = col - 1;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two left, up
        tempRow = row + 1;
        tempCol = col - 2;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two left, down
        tempRow = row - 1;
        tempCol = col - 2;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two down, left
        tempRow = row - 2;
        tempCol = col - 1;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two down, right
        tempRow = row - 2;
        tempCol = col + 1;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two right, down
        tempRow = row - 1;
        tempCol = col + 2;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two right, up
        tempRow = row + 1;
        tempCol = col + 2;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
        // two up, right
        tempRow = row + 2;
        tempCol = col + 1;
        checkMakeKnightMoves(tempRow, tempCol, board, myPosition);
    }

    public void pawnInBoundsMovesWhite(int tempRow, int tempCol, ChessBoard board, ChessPosition myPosition) {
        if (inBounds(tempRow, tempCol) && isBlank(tempRow, tempCol, board)) {
            if (tempRow == 8) {
                ChessPosition pos = new ChessPosition(tempRow, tempCol);
                ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                this.moves.add(move1);
                this.moves.add(move2);
                this.moves.add(move3);
                this.moves.add(move4);
            } else {
                addMove(tempRow, tempCol, myPosition);
            }
        }
    }

    public void pawnInBoundsMovesBlack(int tempRow, int tempCol, ChessBoard board, ChessPosition myPosition) {
        if (inBounds(tempRow, tempCol) && isBlank(tempRow, tempCol, board)) {
            if (tempRow == 1) {
                ChessPosition pos = new ChessPosition(tempRow, tempCol);
                ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                this.moves.add(move1);
                this.moves.add(move2);
                this.moves.add(move3);
                this.moves.add(move4);
            } else {
                addMove(tempRow, tempCol, myPosition);
            }
        }
    }

    public void pawnInBoundsMovesDiagonalWhite(int tempRow, int tempCol, ChessBoard board, ChessPosition myPosition) {
        if (inBounds(tempRow, tempCol) && !isBlank(tempRow, tempCol, board) && isEnemy(tempRow, tempCol, board)) {
            if (tempRow == 8) {
                ChessPosition pos = new ChessPosition(tempRow, tempCol);
                ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                this.moves.add(move1);
                this.moves.add(move2);
                this.moves.add(move3);
                this.moves.add(move4);
            } else {
                addMove(tempRow, tempCol, myPosition);
            }
        }
    }

    public void pawnInBoundsMovesDiagonalBlack(int tempRow, int tempCol, ChessBoard board, ChessPosition myPosition) {
        if (inBounds(tempRow, tempCol) && !isBlank(tempRow, tempCol, board) && isEnemy(tempRow, tempCol, board)) {
            if (tempRow == 1) {
                ChessPosition pos = new ChessPosition(tempRow, tempCol);
                ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                this.moves.add(move1);
                this.moves.add(move2);
                this.moves.add(move3);
                this.moves.add(move4);
            } else {
                addMove(tempRow, tempCol, myPosition);
            }
        }
    }

    public void addPawnMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        if (this.team == ChessGame.TeamColor.WHITE) { // white pawns
            // normal move forward
            tempRow = row + 1;
            tempCol = col;
            pawnInBoundsMovesWhite(tempRow, tempCol, board, myPosition);
            // special double move on first turn
            tempRow = row + 2;
            tempCol = col;
            if (inBounds(tempRow, tempCol)
                    && isBlank(tempRow, tempCol, board)
                    && isBlank(tempRow - 1, tempCol, board)
                    && row == 2) {
                addMove(tempRow, tempCol, myPosition);
            }
            // diagonal kill left
            tempRow = row + 1;
            tempCol = col - 1;
            pawnInBoundsMovesDiagonalWhite(tempRow, tempCol, board, myPosition);
            // diagonal kill right
            tempRow = row + 1;
            tempCol = col + 1;
            pawnInBoundsMovesDiagonalWhite(tempRow, tempCol, board, myPosition);
        } else { // black pawns
            // normal move forward
            tempRow = row - 1;
            tempCol = col;
            pawnInBoundsMovesBlack(tempRow, tempCol, board, myPosition);
            // special double move on first turn
            tempRow = row - 2;
            tempCol = col;
            if (inBounds(tempRow, tempCol)
                    && isBlank(tempRow, tempCol, board)
                    && isBlank(tempRow + 1, tempCol, board)
                    && row == 7) {
                addMove(tempRow, tempCol, myPosition);
            }
            // diagonal kill left
            tempRow = row - 1;
            tempCol = col - 1;
            pawnInBoundsMovesDiagonalBlack(tempRow, tempCol, board, myPosition);
            // diagonal kill right
            tempRow = row - 1;
            tempCol = col + 1;
            pawnInBoundsMovesDiagonalBlack(tempRow, tempCol, board, myPosition);
        }
    }

    public void addRookMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        // up
        tempRow = row + 1;
        tempCol = col;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempRow++;
        }
        // down
        tempRow = row - 1;
        tempCol = col;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempRow--;
        }
        // left
        tempRow = row;
        tempCol = col - 1;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempCol--;
        }
        // right
        tempRow = row;
        tempCol = col + 1;
        while (inBounds(tempRow, tempCol)) {
            if (isBlank(tempRow, tempCol, board)) {
                addMove(tempRow, tempCol, myPosition);
            } else {
                if (isEnemy(tempRow, tempCol, board)) {
                    addMove(tempRow, tempCol, myPosition);
                    break;
                } else {
                    break;
                }
            }
            tempCol++;
        }
    }
}