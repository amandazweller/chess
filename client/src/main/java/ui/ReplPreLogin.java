package ui;

import java.util.Scanner;

import static java.awt.Color.*;


public class ReplPreLogin {
    private final PreLoginClient client;

    public ReplPreLogin(String serverUrl){
        client = new PreLoginClient(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
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

    public void notify(Notification notification) {
        System.out.println(RED + notification.message());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + BLUE);
    }

}
