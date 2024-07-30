package edu.bhcc.bim;

import javafx.application.Application;
import javafx.stage.Stage;
import edu.bhcc.bim.controller.MainController;
import edu.bhcc.bim.state.AppState;
import edu.bhcc.bim.websocket.WebSocketManager;

public class App extends Application {
    private AppState appState;

    @Override
    public void start(Stage primaryStage) {
        appState = new AppState();
        MainController mainController = new MainController(primaryStage, appState);

        // Set initial view
        mainController.setView("Login");
    }

    @Override
    public void stop() {
        handleAppClose();
    }

    private void handleAppClose() {
        WebSocketManager webSocketManager = appState.getWebSocketManager();
        if (webSocketManager != null) {
            webSocketManager.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
