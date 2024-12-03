package websocket.commands;

public class JoinObserver extends UserGameCommand {
    private final int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(CommandType.JOIN_OBSERVER, authToken, gameID);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
