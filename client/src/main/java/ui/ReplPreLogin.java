package ui;

import client.ServerFacade;
import server.Server;

import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static java.awt.Color.*;


public class ReplPreLogin {
    private final PreLoginClient client;
    private State state = State.LOGGEDOUT;
    ReplPostLogin replPostLogin;
    public ServerFacade server;



    public ReplPreLogin(String serverUrl){
        server = new ServerFacade(serverUrl);
        replPostLogin = new ReplPostLogin(server);
        client = new PreLoginClient(server, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(GREEN + result);
                if (result.contains("logged in")){
                    replPostLogin.run(server);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            System.out.println();
        }
    }


    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + BLUE);
    }

}
