package edu.bhcc.bim.controller;

import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.Friendship;
import edu.bhcc.bim.model.FriendListViewItem;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.service.ConversationService;
import edu.bhcc.bim.service.FriendService;
import edu.bhcc.bim.state.AppState;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class BuddyListController {
    private VBox view;
    private AppState appState;
    private ListView<FriendListViewItem> acceptedFriendsListView;
    private ListView<FriendListViewItem> pendingFriendsListView;
    private ListView<FriendListViewItem> blockedFriendsListView;

    public BuddyListController(AppState appState) {
        this.appState = appState;
        appState.setBuddyListController(this);
        initializeView();
        loadFriendsAndConversations();
    }

    private void initializeView() {
        acceptedFriendsListView = new ListView<>();
        pendingFriendsListView = new ListView<>();
        blockedFriendsListView = new ListView<>();
        view = new VBox();

        TabPane tabPane = new TabPane();

        Tab acceptedTab = new Tab("Friends", acceptedFriendsListView);
        acceptedTab.setClosable(false);
        Tab pendingTab = new Tab("Requests", pendingFriendsListView);
        pendingTab.setClosable(false);
        Tab blockedTab = new Tab("Blocked", blockedFriendsListView);
        blockedTab.setClosable(false);

        tabPane.getTabs().addAll(acceptedTab, pendingTab, blockedTab);

        Button addFriendButton = new Button("Add Friend");
        addFriendButton.setOnAction(event -> openFriendRequestDialog());

        view.getChildren().addAll(addFriendButton, tabPane);

        // Set up event handler for friend selection
        acceptedFriendsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                FriendListViewItem selectedFriendItem = acceptedFriendsListView.getSelectionModel().getSelectedItem();
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
                    acceptedFriendsListView.getItems().clear();
                    pendingFriendsListView.getItems().clear();
                    blockedFriendsListView.getItems().clear();
                    for (User friend : friends)
                        addFriendToBuddyList(friend);

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

    private void openFriendRequestDialog() {
        FriendRequest friendRequestDialog = new FriendRequest(appState);
        friendRequestDialog.show();
    }

    public void addFriendToBuddyList(User friend) {
        Friendship.Status friendStatus = friend.getFriendship().getStatus();
        if (friendStatus.equals(Friendship.Status.ACCEPTED)) {
            acceptedFriendsListView.getItems().add(new FriendListViewItem(friend));
        } else if (friendStatus.equals(Friendship.Status.PENDING)) {
            pendingFriendsListView.getItems().add(new FriendListViewItem(friend));
        } else if (friendStatus.equals(Friendship.Status.BLOCKED)) {
            blockedFriendsListView.getItems().add(new FriendListViewItem(friend));
        }
    }

    public VBox getView() {
        return view;
    }
}
