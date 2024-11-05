package server;

import com.google.gson.Gson;
import model.GameData;
import model.AuthData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public UserData login(UserData userData) throws ResponseException {

    }

}
