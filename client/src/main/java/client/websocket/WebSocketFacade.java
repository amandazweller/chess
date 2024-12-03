package client.websocket;

import chess.ChessGame;
import client.ServerFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.PrintBoard;
import ui.ReplGame;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;


    public WebSocketFacade(String url, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            url = url + "/connect";
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

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

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }



}
