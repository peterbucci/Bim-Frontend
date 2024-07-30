package edu.bhcc.bim.websocket;

import javafx.application.Platform;
import javafx.stage.Stage;
import edu.bhcc.bim.controller.BuddyListController;
import edu.bhcc.bim.controller.ChatWindowController;
import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.Friendship;
import edu.bhcc.bim.model.Message;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.state.AppState;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
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
                sendStatus("ONLINE");
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

                stompSession.subscribe("/topic/conversations/" + appState.getUserId(), new StompFrameHandler() {
                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("Received conversation");
                        Conversation conversation = (Conversation) payload;
                        appState.getConversationMap().put(conversation.getParticipant().getUserId(), conversation);
                    }

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Conversation.class;
                    }
                });

                stompSession.subscribe(
                        "/topic/friend/requests/" + appState.getCurrentUser().getUsername(),
                        new StompSessionHandlerAdapter() {
                            @Override
                            public void handleFrame(StompHeaders headers, Object payload) {
                                User user = (User) payload;
                                Friendship friendship = user.getFriendship();
                                Friendship.Status status = friendship.getStatus();
                                BuddyListController buddyListController = appState.getBuddyListController();
                                buddyListController.addFriendToBuddyList(user);
                                if (status == Friendship.Status.ACCEPTED) {
                                    Boolean isSender = friendship.getFromUserId().equals(appState.getUserId());
                                    buddyListController.removePendingFriend(user, isSender);
                                }
                            }

                            @Override
                            public Type getPayloadType(StompHeaders headers) {
                                return User.class;
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
            ChatWindowController chatWindowController = appState.getOpenChatWindows().get(message.getSenderId());
            if (chatWindowController != null) {
                Stage chatWindow = chatWindowController.getStage();
                chatWindow.toFront();
                chatWindow.requestFocus();
            } else {
                Conversation conversation = appState.getConversationMap().get(message.getSenderId());
                chatWindowController = new ChatWindowController(conversation, appState);
                Stage chatWindow = chatWindowController.getStage();
                chatWindowController.show();

                // Add the new chat window controller to the map
                appState.getOpenChatWindows().put(message.getSenderId(), chatWindowController);

                // Remove the chat window from the map when it is closed
                chatWindow.setOnCloseRequest(event -> appState.getOpenChatWindows().remove(message.getSenderId()));
            }
            chatWindowController.addMessage(message);
            System.out.println("Handled message: " + appState.getConversationMap());
            System.out.println("Handled message: " + message.getContent());
        });
    }

    public void sendMessage(Message message, Integer recipientId) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                System.out.println("Sending message: " + message);
                stompSession.send("/app/message/" + recipientId, message); // Adjust this line
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFriendRequest(Friendship friendship, String username) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                System.out.println("Sending friend request: " + friendship);
                stompSession.send("/app/friend/request/" + username, friendship);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendStatus(String status) {
        if (stompSession != null && stompSession.isConnected()) {
            try {
                System.out.println("Sending status: " + status);
                stompSession.send("/app/user/status/" + appState.getUserId(), status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (stompSession != null) {
            try {
                sendStatus("OFFLINE");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stompSession.disconnect();
            }
        }
    }
}
