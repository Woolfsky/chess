package model;

import chess.ChessGame;

record gameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
