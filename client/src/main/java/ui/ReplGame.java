package ui;

import client.ServerFacade;
import exception.ResponseException;

import java.util.Scanner;
import static ui.EscapeSequences.*;


import static java.awt.Color.*;


public class ReplGame {
    public GameClient client;

    public ReplGame(ServerFacade server){
        client = new GameClient(server, this);
    }

    public void run(ServerFacade server) throws ResponseException {
        client.serverFacade.printBoard(null);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("leave")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            System.out.println();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + RESET_BG_COLOR);
    }


}
