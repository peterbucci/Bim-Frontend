package edu.bhcc.bim.controller;

import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.Message;
import edu.bhcc.bim.state.AppState;
import edu.bhcc.bim.websocket.WebSocketManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatWindowController {
    private AppState appState;
    private Conversation conversation;
    private Stage stage;
    private ListView<String> messagesListView;
    private TextArea messageInput;

    public ChatWindowController(Conversation conversation, AppState appState) {
        this.appState = appState;
        this.conversation = conversation;
        initializeView();
    }

    private void initializeView() {
        stage = new Stage();
        VBox root = new VBox();

        messagesListView = new ListView<>();
        messageInput = new TextArea();
        Button sendButton = new Button("Send");

        root.getChildren().addAll(messagesListView, messageInput, sendButton);

        sendButton.setOnAction(event -> sendMessage());

        stage.setScene(new Scene(root, 500, 400));
        stage.setTitle("Chat with " + conversation.getParticipant().getUsername());

        loadMessages();
    }

    private void loadMessages() {
        Platform.runLater(() -> {
            messagesListView.getItems().clear();
            for (Message message : conversation.getMessages()) {
                messagesListView.getItems().add(message.getSender() + ": " + message.getContent());
            }
        });
    }

    private void sendMessage() {
        String content = messageInput.getText();
        if (content.isEmpty()) {
            return;
        }

        // Create the message object
        Message newMessage = new Message();
        newMessage.setContent(content);
        newMessage.setSender("You");
        newMessage.setConversationId(conversation.getConversationId());

        // Send the message via WebSocket
        WebSocketManager webSocketManager = appState.getWebSocketManager();
        webSocketManager.sendMessage(newMessage);

        // Add the new message to the conversation locally
        conversation.getMessages().add(newMessage);
        loadMessages();

        // Clear the input field
        messageInput.clear();
    }

    public void show() {
        stage.show();
    }
}
