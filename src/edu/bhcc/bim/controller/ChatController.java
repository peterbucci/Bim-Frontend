package edu.bhcc.bim.controller;

import edu.bhcc.bim.service.ConversationService;
import edu.bhcc.bim.model.Conversation;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChatController {
    private MainController mainController;
    private VBox view;
    private ListView<Integer> conversationsListView;

    public ChatController(MainController mainController) {
        this.mainController = mainController;
        initializeView();
        loadConversations();
    }

    private void initializeView() {
        conversationsListView = new ListView<>();
        Button backButton = new Button("Back to Buddy List");

        backButton.setOnAction(event -> {
            mainController.setView("Buddy List");
        });

        view = new VBox();
        view.getChildren().addAll(backButton, conversationsListView);
    }

    private void loadConversations() {
        new Thread(() -> {
            try {
                List<Conversation> conversations = ConversationService.getConversations(1);
                Platform.runLater(() -> {
                    conversationsListView.getItems().clear();
                    for (Conversation conversation : conversations) {
                        conversationsListView.getItems().add(conversation.getConversationId());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public VBox getView() {
        return view;
    }
}
