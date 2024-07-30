package edu.bhcc.bim.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import edu.bhcc.bim.state.AppState;

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
        currentScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        currentScene.getStylesheets().add(getClass().getResource("/TabPaneStyle.css").toExternalForm());
        currentScene.getStylesheets().add(getClass().getResource("/AccordionStyle.css").toExternalForm());
        currentScene.getStylesheets().add(getClass().getResource("/ListViewStyle.css").toExternalForm());
        currentScene.getStylesheets().add(getClass().getResource("/VBoxStyle.css").toExternalForm());

        primaryStage.setScene(currentScene);
        primaryStage.setTitle(viewName);
        primaryStage.show();

    }

    private Pane createView(String viewName) {
        switch (viewName) {
            case "Buddy List":
                BuddyListController buddyListController = new BuddyListController(appState);
                return buddyListController.getView();
            case "Login":
                LoginController loginController = new LoginController(appState, this);
                return loginController.getView();
            default:
                throw new IllegalArgumentException("Unknown view: " + viewName);
        }
    }
}
