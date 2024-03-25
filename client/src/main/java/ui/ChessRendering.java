package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class ChessRendering {

    static ChessBoard chessBoard;
    ChessGame.TeamColor perspective;

    public ChessRendering(ChessBoard board) {
        chessBoard = board;
    }

    public ChessRendering(ChessBoard board, ChessGame.TeamColor perspective) {
        chessBoard = board;
        this.perspective = perspective;
    }

    public void renderBoth() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        resetColors(out);
        drawChessBoardBlack(out);
        out.print("\n");
        drawChessBoardWhite(out);
        resetColors(out);
    }

    public void renderPerspective() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        resetColors(out);
        if (perspective == ChessGame.TeamColor.BLACK) { drawChessBoardBlack(out); }
        else { drawChessBoardWhite(out); }
        resetColors(out);
    }

    public void highlight(String location) {
        if (validPosition(location)) {
            // print with the highlights

        }
    }

    public boolean validPosition(String input) {
        List<String> validLetters = new ArrayList<>();
        List<String> validNumbers = new ArrayList<>();
        validLetters.add("a");
        validLetters.add("b");
        validLetters.add("c");
        validLetters.add("d");
        validLetters.add("e");
        validLetters.add("f");
        validLetters.add("g");
        validLetters.add("h");
        validNumbers.add("1");
        validNumbers.add("2");
        validNumbers.add("3");
        validNumbers.add("4");
        validNumbers.add("5");
        validNumbers.add("6");
        validNumbers.add("7");
        validNumbers.add("8");
        return (input.length() == 2
                && validLetters.contains(String.valueOf(input.charAt(0)))
                && validNumbers.contains(String.valueOf(input.charAt(1))));
    }

    private static void drawChessBoardWhite(PrintStream out) {
        topRowWhite(out);
        middleRowsWhite(out);
        topRowWhite(out);
        resetColors(out);
    }

    private static void drawChessBoardBlack(PrintStream out) {
        topRowBlack(out);
        middleRowsBlack(out);
        topRowBlack(out);
        resetColors(out);
    }


    private static void topRowWhite(PrintStream out) {
        String headers = "   " + " a " + " b " + " c " + " d " + " e " + " f " + " g " + " h " + "   ";
        setOutside(out);
        out.print(headers);
        setWhiteSquare(out);
        resetColors(out);
        out.print("\n");
    }

    private static void topRowBlack(PrintStream out) {
        String headers = "   " + " h " + " g " + " f " + " e " + " d " + " c " + " b " + " a " + "   ";
        setOutside(out);
        out.print(headers);
        setWhiteSquare(out);
        resetColors(out);
        out.print("\n");
    }

    private static void middleRowsWhite(PrintStream out) {
        String[] sideLabels = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        boolean backgroundWhite = true;
        for (int r = 8; r > 0; r--) {
            setOutside(out);
            out.print(sideLabels[r-1]);
            for (int c = 1; c < 9; c++) {
                if (backgroundWhite) { setWhiteSquare(out); } else { setBlackSquare(out); }
                String slotValue = "   ";
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = chessBoard.getPiece(pos);
                if (piece != null) {
                    ChessPiece.PieceType type = piece.getPieceType();
                    ChessGame.TeamColor color = piece.getTeamColor();
                    slotValue = getSlotValue(type, color);
                }
                out.print(slotValue);
                backgroundWhite = !backgroundWhite;
            }
            setOutside(out);
            out.print(sideLabels[r-1]);
            resetColors(out);
            out.print("\n");
            setWhiteSquare(out);
            backgroundWhite = !backgroundWhite;
        }
    }

    private static void middleRowsBlack(PrintStream out) {
        String[] sideLabels = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        boolean backgroundWhite = true;
        for (int r = 1; r < 9; r++) {
            setOutside(out);
            out.print(sideLabels[r-1]);
            for (int c = 1; c < 9; c++) {
                if (backgroundWhite) { setWhiteSquare(out); } else { setBlackSquare(out); }
                String slotValue = "   ";
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = chessBoard.getPiece(pos);
                if (piece != null) {
                    ChessPiece.PieceType type = piece.getPieceType();
                    ChessGame.TeamColor color = piece.getTeamColor();
                    slotValue = getSlotValue(type, color);
                }
                out.print(slotValue);
                backgroundWhite = !backgroundWhite;
            }
            setOutside(out);
            out.print(sideLabels[r-1]);
            resetColors(out);
            out.print("\n");
            setWhiteSquare(out);
            backgroundWhite = !backgroundWhite;
        }
    }

    private static String getSlotValue(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            if (type == ChessPiece.PieceType.KING) { return " K "; }
            if (type == ChessPiece.PieceType.QUEEN) { return " Q "; }
            if (type == ChessPiece.PieceType.BISHOP) { return " B "; }
            if (type == ChessPiece.PieceType.KNIGHT) { return " N "; }
            if (type == ChessPiece.PieceType.ROOK) { return " R "; }
            if (type == ChessPiece.PieceType.PAWN) { return " P "; }
        } else {
            if (type == ChessPiece.PieceType.KING) { return " k "; }
            if (type == ChessPiece.PieceType.QUEEN) { return " q "; }
            if (type == ChessPiece.PieceType.BISHOP) { return " b "; }
            if (type == ChessPiece.PieceType.KNIGHT) { return " n "; }
            if (type == ChessPiece.PieceType.ROOK) { return " r "; }
            if (type == ChessPiece.PieceType.PAWN) { return " p "; }
        }
        return "   ";
    }

    private static void setOutside(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setWhiteSquare(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void resetColors(PrintStream out) {
        out.print("\u001B[0m");
    }

}
