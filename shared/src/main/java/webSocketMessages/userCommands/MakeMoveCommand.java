package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private CommandType commandType;
    private ChessMove move;
    private String authToken;
    private int gameID;

    public MakeMoveCommand(String authToken, CommandType type, int gameID, ChessMove move) {
        super(authToken, type);
        this.commandType = type;
        this.move = move;
        this.gameID = gameID;
        this.authToken = authToken;
    }

    public CommandType getCommandType() { return this.commandType; }

    public ChessMove getMove() { return this.move; }

    public String getAuthToken() { return this.authToken; }

    public int getGameID() { return this.gameID; }
}
