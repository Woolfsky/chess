package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] spaces = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        var row = position.getRow();
        var col = position.getColumn();
        spaces[row-1][col-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        var row = position.getRow();
        var col = position.getColumn();
        try { return spaces[row-1][col-1]; } catch (ArrayIndexOutOfBoundsException a) { return null; }
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 8; i > 0; i--) {
            for (int j = 9; j > 0; j--) {
                output += "|";
                ChessPosition pos = new ChessPosition(i, j);
                try {output += getPiece(pos).toString();} catch (Exception e) { output += " "; }
                output += "|";
            }
            output += "\n";
        }
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(spaces, that.spaces);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(spaces);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // white pieces
        ChessPiece wPawn1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn5 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn6 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn7 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wPawn8 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece wRook1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece wRook2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece wKnight1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece wKnight2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece wBishop1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece wBishop2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece wQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece wKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);

        // black pieces
        ChessPiece bPawn1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn3 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn4 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn5 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn6 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn7 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bPawn8 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bRook1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece bRook2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece bKnight1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece bKnight2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece bBishop1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece bBishop2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece bQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece bKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        // place white pieces
        this.addPiece(new ChessPosition(2, 1), wPawn1);
        this.addPiece(new ChessPosition(2, 2), wPawn2);
        this.addPiece(new ChessPosition(2, 3), wPawn3);
        this.addPiece(new ChessPosition(2, 4), wPawn4);
        this.addPiece(new ChessPosition(2, 5), wPawn5);
        this.addPiece(new ChessPosition(2, 6), wPawn6);
        this.addPiece(new ChessPosition(2, 7), wPawn7);
        this.addPiece(new ChessPosition(2, 8), wPawn8);

        this.addPiece(new ChessPosition(1, 1), wRook1);
        this.addPiece(new ChessPosition(1, 8), wRook2);

        this.addPiece(new ChessPosition(1, 2), wKnight1);
        this.addPiece(new ChessPosition(1, 7), wKnight2);

        this.addPiece(new ChessPosition(1, 3), wBishop1);
        this.addPiece(new ChessPosition(1, 6), wBishop2);

        this.addPiece(new ChessPosition(1, 4), wQueen);
        this.addPiece(new ChessPosition(1, 5), wKing);

        // place black pieces
        this.addPiece(new ChessPosition(7, 1), bPawn1);
        this.addPiece(new ChessPosition(7, 2), bPawn2);
        this.addPiece(new ChessPosition(7, 3), bPawn3);
        this.addPiece(new ChessPosition(7, 4), bPawn4);
        this.addPiece(new ChessPosition(7, 5), bPawn5);
        this.addPiece(new ChessPosition(7, 6), bPawn6);
        this.addPiece(new ChessPosition(7, 7), bPawn7);
        this.addPiece(new ChessPosition(7, 8), bPawn8);

        this.addPiece(new ChessPosition(8, 1), bRook1);
        this.addPiece(new ChessPosition(8, 8), bRook2);

        this.addPiece(new ChessPosition(8, 2), bKnight1);
        this.addPiece(new ChessPosition(8, 7), bKnight2);

        this.addPiece(new ChessPosition(8, 3), bBishop1);
        this.addPiece(new ChessPosition(8, 6), bBishop2);

        this.addPiece(new ChessPosition(8, 4), bQueen);
        this.addPiece(new ChessPosition(8, 5), bKing);

    }
}
