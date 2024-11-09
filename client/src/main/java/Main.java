import chess.*;
import client.ServerFacade;
import server.Server;
import ui.ReplPreLogin;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        ServerFacade serverFacade = new ServerFacade("http://localhost:8181");

        ReplPreLogin replPreLogin = new ReplPreLogin("http://localhost:8181");
        replPreLogin.run();
    }
}