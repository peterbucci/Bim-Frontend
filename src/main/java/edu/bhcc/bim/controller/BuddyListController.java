package edu.bhcc.bim.controller;

import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.model.FriendListViewItem;
import edu.bhcc.bim.model.Friendship;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.service.ConversationService;
import edu.bhcc.bim.service.FriendService;
import edu.bhcc.bim.state.AppState;

import java.util.List;

public class BuddyListController {
    private VBox view;
    private AppState appState;
    private ListView<FriendListViewItem> acceptedFriendsOnlineListView;
    private ListView<FriendListViewItem> acceptedFriendsOfflineListView;
    private ListView<FriendListViewItem> pendingFriendsReceivedListView;
    private ListView<FriendListViewItem> pendingFriendsSentListView;
    private ListView<FriendListViewItem> blockedFriendsListView;

    public BuddyListController(AppState appState) {
        this.appState = appState;
        appState.setBuddyListController(this);
        initializeView();
        loadFriendsAndConversations();
    }

    private void initializeView() {
        acceptedFriendsOnlineListView = new ListView<>();
        acceptedFriendsOfflineListView = new ListView<>();
        pendingFriendsReceivedListView = new ListView<>();
        pendingFriendsSentListView = new ListView<>();
        blockedFriendsListView = new ListView<>();

        // Create TitledPanes
        TitledPane onlineFriends = new TitledPane("Friends", acceptedFriendsOnlineListView);
        TitledPane offlineFriends = new TitledPane("Offline", acceptedFriendsOfflineListView);
        // Create an Accordion and add TitledPanes to it
        Accordion acceptedFriendsAccordion = new Accordion();
        acceptedFriendsAccordion.getPanes().addAll(onlineFriends, offlineFriends);

        // Create TitledPanes
        TitledPane receivedRequests = new TitledPane("Received", pendingFriendsReceivedListView);
        TitledPane sentRequests = new TitledPane("Sent", pendingFriendsSentListView);
        // Create an Accordion and add TitledPanes to it
        Accordion pendingFriendsAccordion = new Accordion();
        pendingFriendsAccordion.getPanes().addAll(receivedRequests, sentRequests);

        view = new VBox();

        Button addFriendButton = new Button("Add Friend");
        addFriendButton.setOnAction(event -> openFriendRequestDialog());

        TabPane tabPane = new TabPane();

        Tab acceptedTab = new Tab("Friends", acceptedFriendsAccordion);
        acceptedTab.setClosable(false);
        Tab pendingTab = new Tab("Requests", pendingFriendsAccordion);
        pendingTab.setClosable(false);
        Tab blockedTab = new Tab("Blocked", blockedFriendsListView);
        blockedTab.setClosable(false);

        tabPane.getTabs().addAll(acceptedTab, pendingTab, blockedTab);

        view.getChildren().addAll(addFriendButton, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        view.getStyleClass().add("buddylist-vbox");

        // Set up context menus
        setContextMenu(acceptedFriendsOnlineListView, "Accepted Friends");
        setContextMenu(acceptedFriendsOfflineListView, "Accepted Friends");
        setContextMenu(pendingFriendsReceivedListView, "Pending Friends Received");
        setContextMenu(pendingFriendsSentListView, "Pending Friends Sent");
        setContextMenu(blockedFriendsListView, "Blocked Friends");
    }

    private void loadFriendsAndConversations() {
        new Thread(() -> {
            try {
                List<User> friends = FriendService.getFriendsWithStatus(appState.getUserId());
                List<Conversation> conversations = ConversationService.getConversations(appState.getUserId());

                Platform.runLater(() -> {
                    acceptedFriendsOnlineListView.getItems().clear();
                    acceptedFriendsOfflineListView.getItems().clear();
                    pendingFriendsReceivedListView.getItems().clear();
                    pendingFriendsSentListView.getItems().clear();
                    blockedFriendsListView.getItems().clear();
                    for (User friend : friends)
                        addFriendToBuddyList(friend);

                    for (Conversation conversation : conversations) {
                        int userId = conversation.getParticipant().getUserId();
                        System.out.println("Adding conversation for user: " + userId);
                        if (conversation.getParticipant() != null) {
                            appState.getConversationMap().put(userId, conversation);
                        } else {
                            // Log an error or handle the case where participant is null
                            System.err.println(
                                    "Participant not found for conversation: " + conversation.getConversationId());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void openChatWindow(Integer userId) {
        ChatWindowController existingChatWindowController = appState.getOpenChatWindows().get(userId);
        if (existingChatWindowController != null) {
            // If the chat window is already open, bring it to the front
            Stage chatWindow = existingChatWindowController.getStage();
            Platform.runLater(() -> {
                chatWindow.toFront();
                chatWindow.requestFocus();
            });
        } else {
            Conversation conversation = appState.getConversationMap().get(userId);
            System.out.println("Opening chat window for " + conversation.getParticipant().getUsername());
            if (conversation != null) {
                final ChatWindowController[] chatWindowController = new ChatWindowController[1];
                chatWindowController[0] = new ChatWindowController(conversation, appState);
                Stage chatWindow = chatWindowController[0].getStage();
                Platform.runLater(() -> {
                    chatWindowController[0].show();

                    // Add the new chat window controller to the map
                    appState.getOpenChatWindows().put(userId, chatWindowController[0]);

                    // Remove the chat window from the map when it is closed
                    chatWindow.setOnCloseRequest(event -> appState.getOpenChatWindows().remove(userId));
                });
            }
        }
    }

    private void openFriendRequestDialog() {
        Platform.runLater(() -> {
            FriendRequest friendRequestDialog = new FriendRequest(appState);
            friendRequestDialog.show();
        });
    }

    public void addFriendToBuddyList(User friend) {
        Platform.runLater(() -> {
            Friendship.Status friendStatus = friend.getFriendship().getStatus();
            if (friendStatus.equals(Friendship.Status.ACCEPTED)) {
                if (friend.getStatus().equals("OFFLINE"))
                    acceptedFriendsOfflineListView.getItems().add(new FriendListViewItem(friend));
                else
                    acceptedFriendsOnlineListView.getItems().add(new FriendListViewItem(friend));
            } else if (friendStatus.equals(Friendship.Status.PENDING)) {
                if (friend.getFriendship().getFromUserId() == appState.getUserId())
                    pendingFriendsSentListView.getItems().add(new FriendListViewItem(friend));
                else
                    pendingFriendsReceivedListView.getItems().add(new FriendListViewItem(friend));
            } else if (friendStatus.equals(Friendship.Status.BLOCKED)) {
                blockedFriendsListView.getItems().add(new FriendListViewItem(friend));
            }
        });
    }

    private void setContextMenu(ListView<FriendListViewItem> listView, String type) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem viewProfile = new MenuItem("View Profile");
        MenuItem sendMessage = new MenuItem("Send Message");
        MenuItem acceptFriend = new MenuItem("Accept Request");
        MenuItem cancelFriend = new MenuItem("Cancel Request");
        MenuItem removeFriend = new MenuItem("Remove Friend");
        MenuItem blockFriend = new MenuItem("Block User");
        MenuItem unblockFriend = new MenuItem("Unblock User");

        // on acceFriend click
        acceptFriend.setOnAction(event -> {
            FriendListViewItem selectedFriendItem = listView.getSelectionModel().getSelectedItem();
            if (selectedFriendItem != null) {
                Friendship friendship = selectedFriendItem.getFriend().getFriendship();
                friendship.setStatus(Friendship.Status.ACCEPTED);
                String username = selectedFriendItem.getFriend().getUsername();
                appState.getWebSocketManager().sendFriendRequest(friendship, username);
            }
        });

        if (type.equals("Blocked Friends")) {
            // Customize context menu for blocked friends
            contextMenu.getItems().addAll(viewProfile, unblockFriend);
        } else if (type.equals("Pending Friends Received")) {
            // Customize context menu for pending friends received
            contextMenu.getItems().addAll(viewProfile, acceptFriend, blockFriend);
        } else if (type.equals("Pending Friends Sent")) {
            // Customize context menu for pending friends sent
            contextMenu.getItems().addAll(viewProfile, cancelFriend, blockFriend);
        } else {
            // Default context menu
            contextMenu.getItems().addAll(viewProfile, sendMessage, removeFriend, blockFriend);
        }

        listView.setCellFactory(lv -> {
            ListCell<FriendListViewItem> cell = new ListCell<>() {
                @Override
                protected void updateItem(FriendListViewItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle(""); // Clear existing styles
                    } else {
                        setText(item.getDisplayName());
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && type.equals("Accepted Friends")) {
                    FriendListViewItem selectedFriendItem = listView.getSelectionModel().getSelectedItem();
                    if (selectedFriendItem != null) {
                        openChatWindow(selectedFriendItem.getFriend().getUserId());
                    }
                } else if (event.getButton() == MouseButton.SECONDARY && !cell.isEmpty()) {
                    contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });

            return cell;
        });

        // Hide context menu when clicking outside
        listView.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.SECONDARY && contextMenu.isShowing()) {
                contextMenu.hide();
            }
        });
    }

    public void removePendingFriend(User friend, Boolean isSender) {
        Platform.runLater(() -> {
            if (isSender) {
                pendingFriendsSentListView.getItems()
                        .removeIf(friendItem -> friendItem.getFriend().getUserId().equals(friend.getUserId()));
            } else {
                pendingFriendsReceivedListView.getItems()
                        .removeIf(friendItem -> friendItem.getFriend().getUserId().equals(friend.getUserId()));
            }
        });
    }

    public VBox getView() {
        return view;
    }
}
