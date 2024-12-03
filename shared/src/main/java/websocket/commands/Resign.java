package websocket.commands;

public class Resign extends UserGameCommand {
    private final int gameID;

    public Resign(String authToken, int gameID) {
        super(CommandType.RESIGN, authToken, gameID);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
