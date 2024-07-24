package edu.bhcc.bim.websocket;

import edu.bhcc.bim.model.Message;
import edu.bhcc.bim.state.AppState;
import javafx.application.Platform;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketManager {
    private WebSocketClient webSocketClient;

    public void start() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/ws")) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("WebSocket opened");
                }

                @Override
                public void onMessage(String message) {
                    Platform.runLater(() -> handleMessage(message));
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("WebSocket closed");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String message) {
        // Parse the message and update the state
        // Assuming the message is a JSON string representing a Message object
        Message newMessage = parseMessage(message);
        AppState.getInstance().addMessage(newMessage);
    }

    private Message parseMessage(String message) {
        // Implement message parsing logic
        return new Message();
    }

    public void stop() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
