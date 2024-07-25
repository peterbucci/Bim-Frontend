package edu.bhcc.bim.websocket;

import edu.bhcc.bim.model.Message;
import edu.bhcc.bim.state.AppState;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

public class WebSocketManager {
    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private AppState appState;

    public WebSocketManager(AppState appState) {
        this.appState = appState;
    }

    public void start() {
        System.out.println("Starting WebSocket connection");
        StandardWebSocketClient client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        System.out.println("Connecting to WebSocket server");
        stompClient.connect("ws://localhost:8080/ws", new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                System.out.println("WebSocket connected");

                // Subscribe to the user-specific topic
                stompSession.subscribe("/topic/messages/" + appState.getUserId(), new StompSessionHandlerAdapter() {
                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        handleMessage((Message) payload);
                    }

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Message.class;
                    }
                });
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                exception.printStackTrace();
            }
        });
    }

    private void handleMessage(Message message) {
        Platform.runLater(() -> {
            appState.addMessage(message);
            System.out.println("Handled message: " + message);
        });
    }

    public void sendMessage(Message message) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                System.out.println("Sending message: " + message);
                stompSession.send("/app/message/" + 1, message); // Adjust this line
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (stompSession != null) {
            stompSession.disconnect();
        }
    }
}
