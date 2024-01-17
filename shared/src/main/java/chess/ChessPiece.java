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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet moves = new HashSet<>();
        var my_row = myPosition.getRow();
        var my_col = myPosition.getColumn();

        if (this.type == PieceType.BISHOP) {
            int temp_row;
            int temp_col;

            // top left direction
            temp_row = my_row;
            temp_col = my_col;
            while (temp_row < 8 && temp_col > 1) {
                ChessPosition new_position = new ChessPosition(temp_row + 1, temp_col - 1);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row ++;
                temp_col --;
            }

            // top right direction
            temp_row = my_row;
            temp_col = my_col;
            while (temp_row < 8 && temp_col < 8) {
                ChessPosition new_position = new ChessPosition(temp_row + 1, temp_col + 1);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row ++;
                temp_col ++;
            }

            // bottom left direction
            temp_row = my_row;
            temp_col = my_col;
            while (temp_row > 1 && temp_col > 1) {
                ChessPosition new_position = new ChessPosition(temp_row - 1, temp_col - 1);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row --;
                temp_col --;
            }

            // bottom right direction
            temp_row = my_row;
            temp_col = my_col;
            while (temp_row > 1 && temp_col < 8) {
                ChessPosition new_position = new ChessPosition(temp_row - 1, temp_col + 1);
                ChessMove new_move = new ChessMove(myPosition, new_position, null);
                moves.add(new_move);
                temp_row --;
                temp_col ++;
            }

        }

        return moves;
    }
}
