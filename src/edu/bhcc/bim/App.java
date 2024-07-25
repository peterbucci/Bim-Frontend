package edu.bhcc.bim;

import edu.bhcc.bim.controller.MainController;
import edu.bhcc.bim.state.AppState;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    // get appstate
    private AppState appState;

    @Override
    public void start(Stage primaryStage) {
        appState = new AppState(1);
        MainController mainController = new MainController(primaryStage, appState);

        // Set initial view
        mainController.setView("Buddy List");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
