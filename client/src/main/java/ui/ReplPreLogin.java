package ui;

import java.util.Scanner;


public class ReplPreLogin {
    private final PreLoginClient client;

    public ReplPreLogin(String serverUrl){
        client = new PreLoginClient(serverUrl, this);
    }


}
