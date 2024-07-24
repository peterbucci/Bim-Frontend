package edu.bhcc.bim.controller;

import edu.bhcc.bim.service.FriendService;
import edu.bhcc.bim.service.ConversationService;
import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.model.FriendListViewItem;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuddyListController {
    private VBox view;
    private ListView<FriendListViewItem> friendsListView;
    private Map<String, Conversation> conversationMap;

    public BuddyListController() {
        this.conversationMap = new HashMap<>();
        initializeView();
        loadFriendsAndConversations();
    }

    private void initializeView() {
        friendsListView = new ListView<>();
        view = new VBox();
        view.getChildren().add(friendsListView);

        // Set up event handler for friend selection
        friendsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                FriendListViewItem selectedFriendItem = friendsListView.getSelectionModel().getSelectedItem();
                if (selectedFriendItem != null) {
                    openChatWindow(selectedFriendItem.getFriend().getUsername());
                }
            }
        });
    }

    private void loadFriendsAndConversations() {
        new Thread(() -> {
            try {
                List<User> friends = FriendService.getFriendsWithStatus(1);
                List<Conversation> conversations = ConversationService.getConversations(1);

                Platform.runLater(() -> {
                    friendsListView.getItems().clear();
                    for (User friend : friends) {
                        friendsListView.getItems().add(new FriendListViewItem(friend));
                    }

                    for (Conversation conversation : conversations) {
                        conversationMap.put(conversation.getParticipant().getUsername(), conversation);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void openChatWindow(String friendName) {
        Conversation conversation = conversationMap.get(friendName);
        System.err.println("Conversation: " + conversation);
        if (conversation != null) {
            ChatWindowController chatWindowController = new ChatWindowController(conversation);
            chatWindowController.show();
        }
    }

    public VBox getView() {
        return view;
    }
}
