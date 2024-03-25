package web;

import chess.ChessGame;

public class WebSocketCommunicator {
    SocketListener listener;

    public WebSocketCommunicator(SocketListener socketListener) {
        this.listener = socketListener;
    }
    public interface SocketListener {
        void updateGame(ChessGame game);
    }
}
