package server;
import org.eclipse.jetty.websocket.api.Session;

public class WebsocketHandler {

        public void handleConnect(Session session) {
            Server.allSessions.put(session, 0);
        }

        public void handleClose(Session session, int code, String message) {
            Server.allSessions.remove(session);
        }



    }
