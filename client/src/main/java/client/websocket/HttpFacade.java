package client.websocket;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.ListGameResponse;
import ui.PrintBoard;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class HttpFacade {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) get(scanner.nextLine());
    }

    public static void get(String msg) {
        try {
            var url = new URL("http://localhost:8080/echo/" + msg);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();
            try (InputStream respBody = conn.getInputStream()) {
                byte[] bytes = new byte[respBody.available()];
                respBody.read(bytes);
                System.out.println(new String(bytes));
            }
        } catch (Exception ex) {
            System.out.printf("ERROR: %s\n", ex);
        }
    }
}
