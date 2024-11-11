package ui;

import client.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;


import static java.awt.Color.*;


public class ReplPostLogin {
    public PostLoginClient client;

    public ReplPostLogin(ServerFacade server){
        client = new PostLoginClient(server, this);

    }

    public void run(ServerFacade server) {
        client = new PostLoginClient(server, this);
        System.out.println("Welcome to Chess240.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
                if (result.contains("logged out")){
                    break;
                }
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