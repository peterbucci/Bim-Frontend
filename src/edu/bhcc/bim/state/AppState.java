package edu.bhcc.bim.state;

import edu.bhcc.bim.model.User;
import edu.bhcc.bim.websocket.WebSocketManager;
import edu.bhcc.bim.controller.BuddyListController;
import edu.bhcc.bim.controller.ChatWindowController;
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
    private Map<Integer, User> userMap;
    private Map<Integer, Conversation> conversationMap;
    private Map<Integer, ChatWindowController> openChatWindows;
    private WebSocketManager webSocketManager;
    private BuddyListController buddyListController;

    public AppState() {
        users = FXCollections.observableArrayList();
        conversations = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        userMap = new HashMap<>();
        conversationMap = new HashMap<>();
        openChatWindows = new HashMap<>();
        webSocketManager = new WebSocketManager(this);
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

    public Map<Integer, User> getUserMap() {
        return userMap;
    }

    public Map<Integer, Conversation> getConversationMap() {
        return conversationMap;
    }

    public Map<Integer, ChatWindowController> getOpenChatWindows() {
        return openChatWindows;
    }

    public WebSocketManager getWebSocketManager() {
        return webSocketManager;
    }

    public BuddyListController getBuddyListController() {
        return buddyListController;
    }

    public void setBuddyListController(BuddyListController buddyListController) {
        this.buddyListController = buddyListController;
    }

    public void addUser(User user) {
        users.add(user);
        userMap.put(user.getUserId(), user);
    }

    public User getCurrentUser() {
        return userMap.get(userId);
    }

    public void addConversation(Conversation conversation) {
        conversations.add(conversation);
        conversationMap.put(conversation.getParticipant().getUserId(), conversation);
    }

    public void addMessage(Message message, Integer userId) {
        messages.add(message);
        // Find the conversation and add the message to it
        Conversation conversation = conversationMap.get(userId);
        if (conversation != null) {
            conversation.getMessages().add(message);
        }
    }

    public void removeMessage(Message message, Integer userId) {
        messages.remove(message);
        // Find the conversation and remove the message from it
        Conversation conversation = conversationMap.get(userId);
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
