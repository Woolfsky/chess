package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
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
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessPiece that = (ChessPiece) o;

        if (team != that.team) return false;
        if (type != that.type) return false;
        return Objects.equals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        int result = team != null ? team.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (moves != null ? moves.hashCode() : 0);
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
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

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

    public boolean isFriendly(int row, int col, ChessBoard board) {
        ChessPosition pos = new ChessPosition(row, col);
        if (board.getPiece(pos).getTeamColor() == this.team) {
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
        int temp_row = row;
        int temp_col = col;

        // up left
        temp_row = row + 1;
        temp_col = col - 1;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_row++;
            temp_col--;
        }

        // up right
        temp_row = row + 1;
        temp_col = col + 1;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_row++;
            temp_col++;
        }

        // down left
        temp_row = row - 1;
        temp_col = col - 1;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_row--;
            temp_col--;
        }

        // down right
        temp_row = row - 1;
        temp_col = col + 1;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_row--;
            temp_col++;
        }
    }

    public void addKingMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int temp_row = row;
        int temp_col = col;

        // up
        temp_row = row + 1;
        temp_col = col;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // up, left
        temp_row = row + 1;
        temp_col = col - 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // left
        temp_row = row;
        temp_col = col - 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // left down
        temp_row = row - 1;
        temp_col = col - 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // down
        temp_row = row - 1;
        temp_col = col;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // down right
        temp_row = row - 1;
        temp_col = col + 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // right
        temp_row = row;
        temp_col = col + 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // right up
        temp_row = row + 1;
        temp_col = col + 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }
    }

    public void addKnightMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int temp_row = row;
        int temp_col = col;

        // two up, left
        temp_row = row + 2;
        temp_col = col - 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two left, up
        temp_row = row + 1;
        temp_col = col - 2;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two left, down
        temp_row = row - 1;
        temp_col = col - 2;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two down, left
        temp_row = row - 2;
        temp_col = col - 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two down, right
        temp_row = row - 2;
        temp_col = col + 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two right, down
        temp_row = row - 1;
        temp_col = col + 2;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two right, up
        temp_row = row + 1;
        temp_col = col + 2;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }

        // two up, right
        temp_row = row + 2;
        temp_col = col + 1;
        if (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }
    }

    public void addPawnMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int temp_row = row;
        int temp_col = col;

        if (this.team == ChessGame.TeamColor.WHITE) { // white pawns
            // normal move forward
            temp_row = row + 1;
            temp_col = col;
            if (inBounds(temp_row, temp_col) && isBlank(temp_row, temp_col, board)) {
                if (temp_row == 8) {
                    ChessPosition pos = new ChessPosition(temp_row, temp_col);
                    ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                    ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                    ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                    ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                    this.moves.add(move1);
                    this.moves.add(move2);
                    this.moves.add(move3);
                    this.moves.add(move4);
                } else {
                    addMove(temp_row, temp_col, myPosition);
                }
            }

            // special double move on first turn
            temp_row = row + 2;
            temp_col = col;
            if (inBounds(temp_row, temp_col)
                    && isBlank(temp_row, temp_col, board)
                    && isBlank(temp_row - 1, temp_col, board)
                    && row == 2) {
                addMove(temp_row, temp_col, myPosition);
            }

            // diagonal kill left
            temp_row = row + 1;
            temp_col = col - 1;
            if (inBounds(temp_row, temp_col) && !isBlank(temp_row, temp_col, board) && isEnemy(temp_row, temp_col, board)) {
                if (temp_row == 8) {
                    ChessPosition pos = new ChessPosition(temp_row, temp_col);
                    ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                    ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                    ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                    ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                    this.moves.add(move1);
                    this.moves.add(move2);
                    this.moves.add(move3);
                    this.moves.add(move4);
                } else {
                    addMove(temp_row, temp_col, myPosition);
                }
            }

            // diagonal kill right
            temp_row = row + 1;
            temp_col = col + 1;
            if (inBounds(temp_row, temp_col) && !isBlank(temp_row, temp_col, board) && isEnemy(temp_row, temp_col, board)) {
                if (temp_row == 8) {
                    ChessPosition pos = new ChessPosition(temp_row, temp_col);
                    ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                    ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                    ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                    ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                    this.moves.add(move1);
                    this.moves.add(move2);
                    this.moves.add(move3);
                    this.moves.add(move4);
                } else {
                    addMove(temp_row, temp_col, myPosition);
                }
            }

        } else { // black pawns
            // normal move forward
            temp_row = row - 1;
            temp_col = col;
            if (inBounds(temp_row, temp_col) && isBlank(temp_row, temp_col, board)) {
                if (temp_row == 1) {
                    ChessPosition pos = new ChessPosition(temp_row, temp_col);
                    ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                    ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                    ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                    ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                    this.moves.add(move1);
                    this.moves.add(move2);
                    this.moves.add(move3);
                    this.moves.add(move4);
                } else {
                    addMove(temp_row, temp_col, myPosition);
                }
            }

            // special double move on first turn
            temp_row = row - 2;
            temp_col = col;
            if (inBounds(temp_row, temp_col)
                    && isBlank(temp_row, temp_col, board)
                    && isBlank(temp_row + 1, temp_col, board)
                    && row == 7) {
                addMove(temp_row, temp_col, myPosition);
            }

            // diagonal kill left
            temp_row = row - 1;
            temp_col = col - 1;
            if (inBounds(temp_row, temp_col) && !isBlank(temp_row, temp_col, board) && isEnemy(temp_row, temp_col, board)) {
                if (temp_row == 1) {
                    ChessPosition pos = new ChessPosition(temp_row, temp_col);
                    ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                    ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                    ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                    ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                    this.moves.add(move1);
                    this.moves.add(move2);
                    this.moves.add(move3);
                    this.moves.add(move4);
                } else {
                    addMove(temp_row, temp_col, myPosition);
                }
            }

            // diagonal kill right
            temp_row = row - 1;
            temp_col = col + 1;
            if (inBounds(temp_row, temp_col) && !isBlank(temp_row, temp_col, board) && isEnemy(temp_row, temp_col, board)) {
                if (temp_row == 1) {
                    ChessPosition pos = new ChessPosition(temp_row, temp_col);
                    ChessMove move1 = new ChessMove(myPosition, pos, PieceType.QUEEN);
                    ChessMove move2 = new ChessMove(myPosition, pos, PieceType.BISHOP);
                    ChessMove move3 = new ChessMove(myPosition, pos, PieceType.KNIGHT);
                    ChessMove move4 = new ChessMove(myPosition, pos, PieceType.ROOK);
                    this.moves.add(move1);
                    this.moves.add(move2);
                    this.moves.add(move3);
                    this.moves.add(move4);
                } else {
                    addMove(temp_row, temp_col, myPosition);
                }
            }
        }
    }

    public void addRookMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int temp_row = row;
        int temp_col = col;

        // up
        temp_row = row + 1;
        temp_col = col;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_row++;
        }

        // down
        temp_row = row - 1;
        temp_col = col;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_row--;
        }

        // left
        temp_row = row;
        temp_col = col - 1;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_col--;
        }

        // right
        temp_row = row;
        temp_col = col + 1;
        while (inBounds(temp_row, temp_col)) {
            if (isBlank(temp_row, temp_col, board)) {
                addMove(temp_row, temp_col, myPosition);
            } else {
                if (isEnemy(temp_row, temp_col, board)) {
                    addMove(temp_row, temp_col, myPosition);
                    break;
                } else {
                    break;
                }
            }
            temp_col++;
        }
    }





}
