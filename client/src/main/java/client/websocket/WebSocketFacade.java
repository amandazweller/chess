package client.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.PrintBoard;
import websocket.messages.LoadGame;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    public Session session;


    public WebSocketFacade(String url, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            url = "ws://localhost:8181/connect";
            URI socketURI = new URI(url);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            System.out.println(this.session.toString());

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")){
                        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                        System.out.print("\r\nA move has been made\n");
                        new PrintBoard(loadGame.getGame()).printBoard(teamColor, null);
                    }
                    else{
                        System.out.print( '\r');
                        System.out.printf("\n%s\n[IN-GAME] >>> ", message);
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
