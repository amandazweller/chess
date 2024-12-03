package websocket.commands;

public class Leave extends UserGameCommand {
    private final int gameID;

    public Leave(String authToken, int gameID) {
        super(CommandType.LEAVE, authToken, gameID);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
