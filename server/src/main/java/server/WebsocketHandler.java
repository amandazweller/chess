package server;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;
import chess.ChessGame;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ServerMessage;
import websocket.messages.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketHandler {

        public void handleConnect(Session session) {
            Server.allSessions.put(session, 0);
        }

        public void handleClose(Session session, int code, String message) {
            Server.allSessions.remove(session);
        }

    public void handleMessage(Session session, String message) {
        System.out.printf("Received: %s%n", message);

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> handleJoinPlayer(session, new Gson().fromJson(message, JoinPlayer.class));
            case JOIN_OBSERVER -> handleJoinObserver(session, new Gson().fromJson(message, JoinObserver.class));
            case MAKE_MOVE -> handleMakeMove(session, new Gson().fromJson(message, MakeMove.class));
            case LEAVE -> handleLeave(session, new Gson().fromJson(message, Leave.class));
            case RESIGN -> handleResign(session, new Gson().fromJson(message, Resign.class));
            default -> sendError(session, new Error("Error: Unrecognized command type"));
        }
    }

    private void sendError(Session session, Error error) {
    }

    private void handleResign(Session session, Object o) {
    }

    private void handleLeave(Session session, Object o) {
    }

    private void handleMakeMove(Session session, Object o) {
    }

    private void handleJoinObserver(Session session, Object o) {
    }

    private void handleJoinPlayer(Session session, Object o) {
    }

}
