package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;
    private int gameID;

    public MakeMoveCommand(String authToken, CommandType commandType, int gameID, ChessMove move) {
        super(authToken, commandType);
        this.move = move;
        this.gameID = gameID;
    }

    public CommandType getCommandType() { return this.commandType; }

    public ChessMove getMove() { return this.move; }

    public int getGameID() { return this.gameID; }
}
