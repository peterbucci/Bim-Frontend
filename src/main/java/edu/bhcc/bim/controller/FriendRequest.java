package edu.bhcc.bim.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import edu.bhcc.bim.model.Friendship;
import edu.bhcc.bim.state.AppState;
import edu.bhcc.bim.websocket.WebSocketManager;

public class FriendRequest {

    private Stage dialogStage;
    private TextField usernameField;
    private AppState appState;

    public FriendRequest(AppState appState) {
        this.appState = appState;
        initializeView();
    }

    private void initializeView() {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Send Friend Request");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(20));

        Label usernameLabel = new Label("Enter Username:");
        usernameField = new TextField();

        Button sendRequestButton = new Button("Send Request");
        sendRequestButton.setOnAction(event -> sendFriendRequest());

        dialogVBox.getChildren().addAll(usernameLabel, usernameField, sendRequestButton);

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialogStage.setScene(dialogScene);
    }

    private void sendFriendRequest() {
        WebSocketManager webSocketManager = appState.getWebSocketManager();

        Friendship friendship = new Friendship();
        friendship.setFromUserId(appState.getUserId());
        friendship.setStatus(Friendship.Status.PENDING);

        webSocketManager.sendFriendRequest(friendship, usernameField.getText());
        dialogStage.close();
    }

    public void show() {
        dialogStage.showAndWait();
    }
}
