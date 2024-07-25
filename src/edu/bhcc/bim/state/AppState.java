package edu.bhcc.bim.state;

import edu.bhcc.bim.model.User;
import edu.bhcc.bim.websocket.WebSocketManager;
import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class AppState {
    private int userId;
    private ObservableList<User> users;
    private ObservableList<Conversation> conversations;
    private ObservableList<Message> messages;
    private Map<Integer, Conversation> conversationMap;
    private WebSocketManager webSocketManager;

    public AppState(int userId) {
        this.userId = userId;
        users = FXCollections.observableArrayList();
        conversations = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        conversationMap = new HashMap<>();
        webSocketManager = new WebSocketManager(this);
        webSocketManager.start();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<Conversation> getConversations() {
        return conversations;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    public Map<Integer, Conversation> getConversationMap() {
        return conversationMap;
    }

    public WebSocketManager getWebSocketManager() {
        return webSocketManager;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addConversation(Conversation conversation) {
        conversations.add(conversation);
        conversationMap.put(conversation.getConversationId(), conversation);
    }

    public void addMessage(Message message) {
        messages.add(message);
        // Find the conversation and add the message to it
        Conversation conversation = conversationMap.get(message.getConversationId());
        if (conversation != null) {
            conversation.getMessages().add(message);
        }
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        // Find the conversation and remove the message from it
        Conversation conversation = conversationMap.get(message.getConversationId());
        if (conversation != null) {
            conversation.getMessages().remove(message);
        }
    }

    public void updateUserStatus(Integer userId, String status) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                user.setStatus(status);
                break;
            }
        }
    }

    // Other state management methods
}
