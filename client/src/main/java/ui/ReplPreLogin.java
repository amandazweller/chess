package ui;

import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static java.awt.Color.*;


public class ReplPreLogin {
    private final PreLoginClient client;
    private State state = State.LOGGEDOUT;
    ReplPostLogin replPostLogin;


    public ReplPreLogin(String serverUrl){
        client = new PreLoginClient(serverUrl, this);
        ReplPostLogin ReplPostLogin = new ReplPostLogin(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        while (state.equals(State.LOGGEDOUT)){
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
        replPostLogin.run();
    }


    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + BLUE);
    }

}
