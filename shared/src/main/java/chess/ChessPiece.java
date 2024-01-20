package chess;

import java.util.ArrayList;
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
    ChessGame.TeamColor pieceColor;
    PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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
    public String toString() {
        if (this.type == PieceType.PAWN && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "P";
        }
        if (this.type == PieceType.PAWN && this.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return "p";
        }
        if (this.type == PieceType.ROOK && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "R";
        }
        if (this.type == PieceType.ROOK && this.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return "r";
        }
        if (this.type == PieceType.KNIGHT && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "N";
        }
        if (this.type == PieceType.KNIGHT && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "n";
        }
        if (this.type == PieceType.BISHOP && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "B";
        }
        if (this.type == PieceType.BISHOP && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "b";
        }
        if (this.type == PieceType.QUEEN && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "Q";
        }
        if (this.type == PieceType.QUEEN && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "q";
        }
        if (this.type == PieceType.KING && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "K";
        }
        if (this.type == PieceType.KING && this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "k";
        }

        return " ";
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

        if (this.type == PieceType.BISHOP || this.type == PieceType.QUEEN) {
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
                // moves that promote the pawn
                if (this_row == 7) {
                    // normal move forward (with promotion)
                    if (inBounds(this_row + 1, this_col)) {
                        ChessPosition new_position = new ChessPosition(this_row + 1, this_col);
                        if (board.getPiece(new_position) == null) {
                            // there's no piece in front of the pawn
                            ChessMove new_move = new ChessMove(myPosition, new_position, PieceType.QUEEN);
                            ChessMove new_move2 = new ChessMove(myPosition, new_position, PieceType.BISHOP);
                            ChessMove new_move3 = new ChessMove(myPosition, new_position, PieceType.KNIGHT);
                            ChessMove new_move4 = new ChessMove(myPosition, new_position, PieceType.ROOK);
                            moves.add(new_move);
                            moves.add(new_move2);
                            moves.add(new_move3);
                            moves.add(new_move4);
                        }
                    }
                    // up left kill (with promotion)
                    if (inBounds(this_row + 1, this_col - 1)) {
                        ChessPosition new_position = new ChessPosition(this_row + 1, this_col - 1);
                        if (board.getPiece(new_position) != null) {
                            // there is a piece there
                            if (!friendlyPiece(new_position, board)) {
                                ChessMove new_move = new ChessMove(myPosition, new_position, PieceType.QUEEN);
                                ChessMove new_move2 = new ChessMove(myPosition, new_position, PieceType.BISHOP);
                                ChessMove new_move3 = new ChessMove(myPosition, new_position, PieceType.KNIGHT);
                                ChessMove new_move4 = new ChessMove(myPosition, new_position, PieceType.ROOK);
                                moves.add(new_move);
                                moves.add(new_move2);
                                moves.add(new_move3);
                                moves.add(new_move4);
                            }
                        }
                    }

                    // up right kill (with promotion)
                    if (inBounds(this_row + 1, this_col + 1)) {
                        ChessPosition new_position = new ChessPosition(this_row + 1, this_col + 1);
                        if (board.getPiece(new_position) != null) {
                            // there is a piece there
                            if (!friendlyPiece(new_position, board)) {
                                ChessMove new_move = new ChessMove(myPosition, new_position, PieceType.QUEEN);
                                ChessMove new_move2 = new ChessMove(myPosition, new_position, PieceType.BISHOP);
                                ChessMove new_move3 = new ChessMove(myPosition, new_position, PieceType.KNIGHT);
                                ChessMove new_move4 = new ChessMove(myPosition, new_position, PieceType.ROOK);
                                moves.add(new_move);
                                moves.add(new_move2);
                                moves.add(new_move3);
                                moves.add(new_move4);
                            }
                        }
                    }

                } else { // moves that DON'T promote the pawn
                    // normal move forward (w/o promotion)
                    if (inBounds(this_row + 1, this_col)) {
                        ChessPosition new_position = new ChessPosition(this_row + 1, this_col);
                        if (board.getPiece(new_position) == null) {
                            // there's no piece in front of the pawn
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                    // up left kill (w/o promotion)
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

                    // up right kill (w/o promotion)
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
                }


            } else {
                // black pawn moves
                // moves that promote the pawn
                if (this_row == 2) {
                    // normal move forward (with pawn promotion)
                    if (inBounds(this_row - 1, this_col)) {
                        ChessPosition new_position = new ChessPosition(this_row - 1, this_col);
                        if (board.getPiece(new_position) == null) {
                            // there's no piece in front of the pawn
                            ChessMove new_move = new ChessMove(myPosition, new_position, PieceType.QUEEN);
                            ChessMove new_move2 = new ChessMove(myPosition, new_position, PieceType.BISHOP);
                            ChessMove new_move3 = new ChessMove(myPosition, new_position, PieceType.KNIGHT);
                            ChessMove new_move4 = new ChessMove(myPosition, new_position, PieceType.ROOK);
                            moves.add(new_move);
                            moves.add(new_move2);
                            moves.add(new_move3);
                            moves.add(new_move4);
                        }
                    }
                    // down left kill (with pawn promotion)
                    if (inBounds(this_row - 1, this_col - 1)) {
                        ChessPosition new_position = new ChessPosition(this_row - 1, this_col - 1);
                        if (board.getPiece(new_position) != null) {
                            // there is a piece there
                            if (!friendlyPiece(new_position, board)) {
                                ChessMove new_move = new ChessMove(myPosition, new_position, PieceType.QUEEN);
                                ChessMove new_move2 = new ChessMove(myPosition, new_position, PieceType.BISHOP);
                                ChessMove new_move3 = new ChessMove(myPosition, new_position, PieceType.KNIGHT);
                                ChessMove new_move4 = new ChessMove(myPosition, new_position, PieceType.ROOK);
                                moves.add(new_move);
                                moves.add(new_move2);
                                moves.add(new_move3);
                                moves.add(new_move4);
                            }
                        }
                    }

                    // down right kill (with pawn promotion)
                    if (inBounds(this_row - 1, this_col + 1)) {
                        ChessPosition new_position = new ChessPosition(this_row - 1, this_col + 1);
                        if (board.getPiece(new_position) != null) {
                            // there is a piece there
                            if (!friendlyPiece(new_position, board)) {
                                ChessMove new_move = new ChessMove(myPosition, new_position, PieceType.QUEEN);
                                ChessMove new_move2 = new ChessMove(myPosition, new_position, PieceType.BISHOP);
                                ChessMove new_move3 = new ChessMove(myPosition, new_position, PieceType.KNIGHT);
                                ChessMove new_move4 = new ChessMove(myPosition, new_position, PieceType.ROOK);
                                moves.add(new_move);
                                moves.add(new_move2);
                                moves.add(new_move3);
                                moves.add(new_move4);
                            }
                        }
                    }
                } else { // moves that DON'T promote the pawn
                    // normal move forward (w/o promotion)
                    if (inBounds(this_row - 1, this_col)) {
                        ChessPosition new_position = new ChessPosition(this_row - 1, this_col);
                        if (board.getPiece(new_position) == null) {
                            // there's no piece in front of the pawn
                            ChessMove new_move = new ChessMove(myPosition, new_position, null);
                            moves.add(new_move);
                        }
                    }
                    // down left kill (w/o promotion)
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

                    // down right kill (w/o promotion)
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

        }

        if (this.type == PieceType.ROOK || this.type == PieceType.QUEEN) {
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
