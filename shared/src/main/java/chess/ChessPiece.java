package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
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

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    public boolean inBounds(int row, int col) {
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            return true;
        }
        return false;
    }

    public boolean friendlyPiece(ChessPosition new_position, ChessBoard board) {
        if (board.getPiece(new_position) == null) {
            return false;
        } else {
            if (board.getPiece(new_position).getTeamColor() == this.getTeamColor()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPiece(ChessPosition new_position, ChessBoard board) {
        if (board.getPiece(new_position) == null) {
            return false;
        }
        return true;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet moves = new HashSet<>();
        var this_row = myPosition.getRow();
        var this_col = myPosition.getColumn();

        if (this.type == PieceType.BISHOP) {
            int temp_row;
            int temp_col;

            // top left direction
            temp_row = this_row;
            temp_col = this_col;
            while (temp_row < 8 && temp_col > 1) {
                ChessPosition new_position = new ChessPosition(temp_row + 1, temp_col - 1);
                // check if a piece is there, if so then see if it's on my team
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == this.getTeamColor()) {
                        break;
                    } else {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row ++;
                temp_col --;
            }

            // top right direction
            temp_row = this_row;
            temp_col = this_col;
            while (temp_row < 8 && temp_col < 8) {
                ChessPosition new_position = new ChessPosition(temp_row + 1, temp_col + 1);
                // check if a piece is there, if so then see if it's on my team
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == this.getTeamColor()) {
                        break;
                    } else {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row ++;
                temp_col ++;
            }

            // bottom left direction
            temp_row = this_row;
            temp_col = this_col;
            while (temp_row > 1 && temp_col > 1) {
                ChessPosition new_position = new ChessPosition(temp_row - 1, temp_col - 1);
                // check if a piece is there, if so then see if it's on my team
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == this.getTeamColor()) {
                        break;
                    } else {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row --;
                temp_col --;
            }

            // bottom right direction
            temp_row = this_row;
            temp_col = this_col;
            while (temp_row > 1 && temp_col < 8) {
                ChessPosition new_position = new ChessPosition(temp_row - 1, temp_col + 1);
                // check if a piece is there, if so then see if it's on my team
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == this.getTeamColor()) {
                        break;
                    } else {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row --;
                temp_col ++;
            }
        }

        if (this.type == PieceType.KING) {
            // move down
            if (this_row > 1) {
                ChessPosition new_position = new ChessPosition(this_row - 1, this_col);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move down and left
            if (this_row > 1 && this_col > 1) {
                ChessPosition new_position = new ChessPosition(this_row - 1, this_col - 1);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move down and right
            if (this_row > 1 && this_col < 8) {
                ChessPosition new_position = new ChessPosition(this_row - 1, this_col + 1);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move left
            if (this_col > 1) {
                ChessPosition new_position = new ChessPosition(this_row, this_col - 1);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move right
            if (this_col < 8) {
                ChessPosition new_position = new ChessPosition(this_row, this_col + 1);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move up and left
            if (this_row < 8 && this_col > 1) {
                ChessPosition new_position = new ChessPosition(this_row + 1, this_col - 1);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move up
            if (this_row < 8) {
                ChessPosition new_position = new ChessPosition(this_row + 1, this_col);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }
            // move up and right
            if (this_row < 8 && this_col < 8) {
                ChessPosition new_position = new ChessPosition(this_row + 1, this_col + 1);
                if (board.getPiece(new_position) == null) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                } else {
                    if (board.getPiece(new_position).getTeamColor() != this.getTeamColor()) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
            }

        }

        if (this.type == PieceType.KNIGHT) {
            // up two, one left
            if (inBounds(this_row + 2, this_col - 1)) {
                ChessPosition new_position = new ChessPosition(this_row + 2, this_col - 1);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // up two, one right
            if (inBounds(this_row + 2, this_col + 1)) {
                ChessPosition new_position = new ChessPosition(this_row + 2, this_col + 1);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // two left, one up
            if (inBounds(this_row + 1, this_col - 2)) {
                ChessPosition new_position = new ChessPosition(this_row + 1, this_col - 2);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // two left, one down
            if (inBounds(this_row - 1, this_col - 2)) {
                ChessPosition new_position = new ChessPosition(this_row - 1, this_col - 2);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // two down, one left
            if (inBounds(this_row - 2, this_col - 1)) {
                ChessPosition new_position = new ChessPosition(this_row - 2, this_col - 1);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // two down, one right
            if (inBounds(this_row - 2, this_col + 1)) {
                ChessPosition new_position = new ChessPosition(this_row - 2, this_col + 1);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // two right, one down
            if (inBounds(this_row - 1, this_col + 2)) {
                ChessPosition new_position = new ChessPosition(this_row - 1, this_col + 2);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }

            // two right, one up
            if (inBounds(this_row + 1, this_col + 2)) {
                ChessPosition new_position = new ChessPosition(this_row + 1, this_col + 2);
                if (!friendlyPiece(new_position, board)) {
                    ChessMove new_move = new ChessMove(myPosition, new_position, null);
                    moves.add(new_move);
                }
            }
        }

        if (this.type == PieceType.PAWN) {
            // white pawn moves
            if (this.pieceColor == ChessGame.TeamColor.WHITE) {
                // normal move forward
                if (inBounds(this_row + 1, this_col)) {
                    ChessPosition new_position = new ChessPosition(this_row + 1, this_col);
                    if (board.getPiece(new_position) == null) {
                        // there's no piece in front of the pawn
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
                // up left kill
                if (inBounds(this_row + 1, this_col - 1)) {
                    ChessPosition new_position = new ChessPosition(this_row + 1, this_col - 1);
                    if (board.getPiece(new_position) != null) {
                        // there is a piece there
                        if (!friendlyPiece(new_position, board)) {
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                }

                // up right kill
                if (inBounds(this_row + 1, this_col + 1)) {
                    ChessPosition new_position = new ChessPosition(this_row + 1, this_col + 1);
                    if (board.getPiece(new_position) != null) {
                        // there is a piece there
                        if (!friendlyPiece(new_position, board)) {
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                }

                // original pawn move (2 spaces)
                if (this_row == 2) {
                    ChessPosition new_position = new ChessPosition(this_row + 2, this_col);
                    if (board.getPiece(new_position) == null) {
                        // there's no piece 2 spaces in front of the pawn
                        ChessPosition block_position = new ChessPosition(this_row + 1, this_col);
                        if (board.getPiece(block_position) == null) {
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                }

            } else {
                // black pawn moves
                // normal move forward
                if (inBounds(this_row - 1, this_col)) {
                    ChessPosition new_position = new ChessPosition(this_row - 1, this_col);
                    if (board.getPiece(new_position) == null) {
                        // there's no piece in front of the pawn
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                    }
                }
                // down left kill
                if (inBounds(this_row - 1, this_col - 1)) {
                    ChessPosition new_position = new ChessPosition(this_row - 1, this_col - 1);
                    if (board.getPiece(new_position) != null) {
                        // there is a piece there
                        if (!friendlyPiece(new_position, board)) {
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                }

                // down right kill
                if (inBounds(this_row - 1, this_col + 1)) {
                    ChessPosition new_position = new ChessPosition(this_row - 1, this_col + 1);
                    if (board.getPiece(new_position) != null) {
                        // there is a piece there
                        if (!friendlyPiece(new_position, board)) {
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                }

                // original pawn move (2 spaces)
                if (this_row == 7) {
                    ChessPosition new_position = new ChessPosition(this_row - 2, this_col);
                    if (board.getPiece(new_position) == null) {
                        // there's no piece 2 spaces in front of the pawn
                        ChessPosition block_position = new ChessPosition(this_row - 1, this_col);
                        if (board.getPiece(block_position) == null) {
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                }
            }

        }

        if (this.type == PieceType.ROOK) {
            int temp_row;
            int temp_col;

            // move up
            temp_row = this_row + 1;
            temp_col = this_col;
            while (inBounds(temp_row, temp_col)) {
                ChessPosition new_position = new ChessPosition(temp_row, temp_col);
                if (isPiece(new_position, board)) {
                    // is the piece an enemy?
                    if (!friendlyPiece(new_position, board)) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    } else {
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row ++;
            }

            // move left
            temp_row = this_row;
            temp_col = this_col - 1;
            while (inBounds(temp_row, temp_col)) {
                ChessPosition new_position = new ChessPosition(temp_row, temp_col);
                if (isPiece(new_position, board)) {
                    // is the piece an enemy?
                    if (!friendlyPiece(new_position, board)) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    } else {
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_col --;
            }

            // move right
            temp_row = this_row;
            temp_col = this_col + 1;
            while (inBounds(temp_row, temp_col)) {
                ChessPosition new_position = new ChessPosition(temp_row, temp_col);
                if (isPiece(new_position, board)) {
                    // is the piece an enemy?
                    if (!friendlyPiece(new_position, board)) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    } else {
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_col ++;
            }

            // move down
            temp_row = this_row - 1;
            temp_col = this_col;
            while (inBounds(temp_row, temp_col)) {
                ChessPosition new_position = new ChessPosition(temp_row, temp_col);
                if (isPiece(new_position, board)) {
                    // is the piece an enemy?
                    if (!friendlyPiece(new_position, board)) {
                        ChessMove new_move = new ChessMove(myPosition, new_position, null);
                        moves.add(new_move);
                        break;
                    } else {
                        break;
                    }
                }
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row --;
            }


        }

        return moves;
    }
}
