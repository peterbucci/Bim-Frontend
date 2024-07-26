package edu.bhcc.bim.controller;

import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.FriendListViewItem;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.service.ConversationService;
import edu.bhcc.bim.service.FriendService;
import edu.bhcc.bim.state.AppState;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class BuddyListController {
    private VBox view;
    private AppState appState;
    private ListView<FriendListViewItem> friendsListView;

    public BuddyListController(AppState appState) {
        this.appState = appState;
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
                    openChatWindow(selectedFriendItem.getFriend().getUserId());
                }
            }
        });
    }

    private void loadFriendsAndConversations() {
        new Thread(() -> {
            try {
                List<User> friends = FriendService.getFriendsWithStatus(appState.getUserId());
                List<Conversation> conversations = ConversationService.getConversations(appState.getUserId());

                Platform.runLater(() -> {
                    friendsListView.getItems().clear();
                    for (User friend : friends) {
                        friendsListView.getItems().add(new FriendListViewItem(friend));
                    }

                    for (Conversation conversation : conversations) {
                        int userId = conversation.getParticipant().getUserId();
                        appState.getConversationMap().put(userId, conversation);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void openChatWindow(Integer userId) {
        ChatWindowController chatWindowController = appState.getOpenChatWindows().get(userId);
        if (chatWindowController != null) {
            // If the chat window is already open, bring it to the front
            Stage chatWindow = chatWindowController.getStage();
            chatWindow.toFront();
            chatWindow.requestFocus();
        } else {
            Conversation conversation = appState.getConversationMap().get(userId);
            if (conversation != null) {
                chatWindowController = new ChatWindowController(conversation, appState);
                Stage chatWindow = chatWindowController.getStage();
                chatWindowController.show();

                // Add the new chat window controller to the map
                appState.getOpenChatWindows().put(userId, chatWindowController);

                // Remove the chat window from the map when it is closed
                chatWindow.setOnCloseRequest(event -> appState.getOpenChatWindows().remove(userId));
            }
        }

    }

    public VBox getView() {
        return view;
    }
}
