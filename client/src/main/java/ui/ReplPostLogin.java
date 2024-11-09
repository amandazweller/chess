package ui;

import client.ServerFacade;
import server.Server;

import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static java.awt.Color.*;


public class ReplPostLogin {
    public PostLoginClient client;

    public ReplPostLogin(ServerFacade server){
        //client = new PostLoginClient(server, this);
    }

    public void run(ServerFacade server) {
        client = new PostLoginClient(server, this);
        System.out.println("You have successfully logged in. Type Help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(GREEN + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + BLUE);
    }

}