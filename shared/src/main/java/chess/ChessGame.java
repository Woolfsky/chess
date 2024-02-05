package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    TeamColor teamTurn;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.board.getPiece(startPosition);
        Collection<ChessMove> allMoves = piece.pieceMoves(this.board, startPosition);
        findKing(TeamColor.WHITE);
        return allMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();

        // get the piece at the start position
        ChessPiece piece = this.board.getPiece(startPos);
        if (!piece.pieceMoves(this.board, startPos).contains(move)) { throw new InvalidMoveException("Invalid move!!!!!"); }
        this.board.removePiece(startPos);
        this.board.removePiece(endPos);
        this.board.addPiece(endPos, piece);
    }

    public ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (this.board.getPiece(pos) != null) {
                    ChessPiece p = this.board.getPiece(pos);
                    if (p.getPieceType() == ChessPiece.PieceType.KING && p.getTeamColor() == teamColor) {
                        return pos;
                    }
                }
            }
        }
        throw new RuntimeException("could't find the king and the code got here (findKing method)");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // find the king
        ChessPosition kingPos = findKing(teamColor);

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (this.board.getPiece(pos) != null && this.board.getPiece(pos).getTeamColor() != teamColor) {
                    ChessPiece enemy_p = this.board.getPiece(pos);
                    Collection<ChessMove> enemy_p_moves = enemy_p.pieceMoves(this.board, pos);
                    for (ChessMove m : enemy_p_moves) {
                        if (m.getEndPosition().getRow() == kingPos.getRow() && m.getEndPosition().getColumn() == kingPos.getColumn()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean canKingMoveAndBeSafe(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        ChessPiece king = this.board.getPiece(kingPos);
        Collection<ChessMove> kingMoves = king.pieceMoves(this.board, kingPos);
        for (ChessMove m : kingMoves) {
            // go through the King's moves to see if any will NOT result in a check
            if (this.board.getPiece(m.getEndPosition()) != null) {
                // there is a piece there to kill
                ChessPiece killedPiece = this.board.getPiece(m.getEndPosition());
                try {this.makeMove(m);} catch (InvalidMoveException ex) {System.out.println(ex);};
                if (!isInCheck(teamColor)) {
                    // it can move and be safe! but better reset the hypothetical move back to how it was before
                    this.board.addPiece(kingPos, king);
                    this.board.removePiece(m.getEndPosition());
                    this.board.addPiece(m.getEndPosition(), killedPiece);
                    return true;
                } else {
                    // dang it, still stuck
                    this.board.addPiece(kingPos, king);
                    this.board.removePiece(m.getEndPosition());
                    this.board.addPiece(m.getEndPosition(), killedPiece);
                }
            } else {
                // that was an empty spot
                try {this.makeMove(m);} catch (InvalidMoveException ex) {System.out.println(ex);};
                if (!isInCheck(teamColor)) {
                    // it can move and be safe! but better reset the hypothetical move back to how it was before
                    this.board.addPiece(kingPos, king);
                    this.board.removePiece(m.getEndPosition());
                    return true;
                } else {
                    // dang it, still stuck
                    this.board.addPiece(kingPos, king);
                    this.board.removePiece(m.getEndPosition());
                }
            }
        }
        return false;
    }

    boolean canFriendlyPieceBlockCheck(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (this.board.getPiece(pos) != null && this.board.getPiece(pos).getTeamColor() == teamColor) {
                    ChessPiece friendly_p = this.board.getPiece(pos);
                    Collection<ChessMove> friendly_p_moves = friendly_p.pieceMoves(this.board, pos);
                    for (ChessMove m : friendly_p_moves) {
                        // go through the friendly piece's moves to see if any will take the king out of check
                        if (this.board.getPiece(m.getEndPosition()) != null) {
                            // there is a piece there to kill
                            ChessPiece killedPiece = this.board.getPiece(m.getEndPosition());
                            try {this.makeMove(m);} catch (InvalidMoveException ex) {System.out.println(ex);};
                            if (!isInCheck(teamColor)) {
                                // not in checkmate! but better reset the hypothetical move back to how it was before
                                this.board.addPiece(pos, friendly_p);
                                this.board.removePiece(m.getEndPosition());
                                this.board.addPiece(m.getEndPosition(), killedPiece);
                                return true;
                            } else {
                                // dang it, still in checkmate
                                this.board.addPiece(pos, friendly_p);
                                this.board.removePiece(m.getEndPosition());
                                this.board.addPiece(m.getEndPosition(), killedPiece);
                            }
                        } else {
                            // that was an empty spot
                            try {this.makeMove(m);} catch (InvalidMoveException ex) {System.out.println(ex);};
                            if (!isInCheck(teamColor)) {
                                // not in checkmate! but better reset the hypothetical move back to how it was before
                                this.board.addPiece(pos, friendly_p);
                                this.board.removePiece(m.getEndPosition());
                                return true;
                            } else {
                                // dang it, still in checkmate
                                this.board.addPiece(pos, friendly_p);
                                this.board.removePiece(m.getEndPosition());
                            }
                        }

                    }
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // is the king in check?
        if (!isInCheck(teamColor)) { return false; }

        // check whether the king can simply move out of check
        if (canKingMoveAndBeSafe(teamColor)) { return false; }

        // check if other pieces can block the check
        if (canFriendlyPieceBlockCheck(teamColor)) { return false; }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // first check that the king is not in check
        if (isInCheck(teamColor)) { return false; }

        // check if any of the king's move will put it in check
        if (canKingMoveAndBeSafe(teamColor)) { return false; }

        // check if any of the other pieces can block the check
        if (canFriendlyPieceBlockCheck(teamColor)) { return false; }

        return true;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
