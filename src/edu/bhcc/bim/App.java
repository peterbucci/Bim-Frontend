package edu.bhcc.bim;

import edu.bhcc.bim.controller.MainController;
import edu.bhcc.bim.state.AppState;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    private AppState appState;

    @Override
    public void start(Stage primaryStage) {
        appState = new AppState();
        MainController mainController = new MainController(primaryStage, appState);

        // Set initial view
        mainController.setView("Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
