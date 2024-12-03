package websocket.commands;

public class JoinObserver extends UserGameCommand {

    public JoinObserver(String authToken, int gameID) {
        super(CommandType.JOIN_OBSERVER, authToken, gameID);
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public Integer getGameID() {
        return super.getGameID();
    }
}
