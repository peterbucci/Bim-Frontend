package edu.bhcc.bim.controller;

import edu.bhcc.bim.state.AppState;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {
    private Stage primaryStage;
    private Scene currentScene;
    private AppState appState;

    public MainController(Stage primaryStage, AppState appState) {
        this.primaryStage = primaryStage;
        this.appState = appState;
    }

    public void setView(String viewName) {
        Pane view = createView(viewName);
        currentScene = new Scene(view, 300, 600);
        primaryStage.setScene(currentScene);
        primaryStage.setTitle(viewName);
        primaryStage.show();
    }

    private Pane createView(String viewName) {
        switch (viewName) {
            case "Buddy List":
                BuddyListController buddyListController = new BuddyListController(appState);
                return buddyListController.getView();
            case "Chat":
                ChatController chatController = new ChatController(this);
                return chatController.getView();
            default:
                throw new IllegalArgumentException("Unknown view: " + viewName);
        }
    }
}
