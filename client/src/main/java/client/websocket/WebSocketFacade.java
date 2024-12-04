package client.websocket;

import chess.ChessGame;
import client.ServerFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.GameClient;
import ui.PrintBoard;
import ui.ReplGame;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.Error;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.ERASE_LINE;

public class WebSocketFacade extends Endpoint {

    public Session session;


    public WebSocketFacade(String url, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            url = url + "/ws";
            URI socketURI = new URI(url);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //figure out why its printing something here
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")){
                        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                        PrintBoard printBoard = new PrintBoard(loadGame.getGame());
                        printBoard.printBoard(teamColor, null);
                    }
                    else if (message.contains("\"serverMessageType\":\"ERROR\"")){
                        Error error = new Gson().fromJson(message, Error.class);
                        System.out.printf("\n%s\n", error.getMessage());
                    }
                    else {
                        Notification notification = new Gson().fromJson(message, Notification.class);
                        System.out.printf("\n%s\n ", notification.getMessage());
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        System.out.println("WebSocket connection opened: " + session.getId());
    }

    public void sendMessage(String message) throws ResponseException {
        if (this.session == null) {
            throw new ResponseException(401, "WebSocket session is not initialized.");
        }
        this.session.getAsyncRemote().sendText(message);
    }



}
