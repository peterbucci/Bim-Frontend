package edu.bhcc.bim.controller;

import edu.bhcc.bim.websocket.WebSocketManager;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.Message;
import edu.bhcc.bim.state.AppState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ChatWindowController {
    private AppState appState;
    private Conversation conversation;
    private Stage stage;
    private ListView<Message> messagesListView;
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

        // Set custom cell factory for text wrapping
        messagesListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<Message>() {
                    private Text text;

                    @Override
                    protected void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            text = new Text(item.getSender() + ": " + item.getContent());
                            text.wrappingWidthProperty().bind(param.widthProperty().subtract(20)); // Set wrapping width
                            setGraphic(text);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        loadMessages();
    }

    public void loadMessages() {
        Platform.runLater(() -> {
            messagesListView.getItems().clear();
            messagesListView.getItems().addAll(conversation.getMessages());
            // Scroll to the bottom of the ListView
            messagesListView.scrollTo(messagesListView.getItems().size() - 1);
        });
    }

    private void sendMessage() {
        String content = messageInput.getText();
        if (content.isEmpty()) {
            return;
        }

        // Create the message object
        Message newMessage = new Message();
        User sender = appState.getUserMap().get(appState.getUserId());
        newMessage.setContent(content);
        newMessage.setSender(sender.getUsername());
        newMessage.setSenderId(sender.getUserId());
        newMessage.setConversationId(conversation.getConversationId());

        // Send the message via WebSocket
        WebSocketManager webSocketManager = appState.getWebSocketManager();
        webSocketManager.sendMessage(newMessage, conversation.getParticipant().getUserId());

        addMessage(newMessage);

        // Clear the input field
        messageInput.clear();

        // Scroll to the bottom of the ListView after sending the message
        Platform.runLater(() -> messagesListView.scrollTo(messagesListView.getItems().size() - 1));
    }

    public void show() {
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void addMessage(Message message) {
        Platform.runLater(() -> {
            appState.addMessage(message, conversation.getParticipant().getUserId());
            messagesListView.getItems().add(message);
            messagesListView.scrollTo(messagesListView.getItems().size() - 1);
        });
    }
}
