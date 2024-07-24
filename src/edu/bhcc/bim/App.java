package edu.bhcc.bim;

import edu.bhcc.bim.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainController mainController = new MainController(primaryStage);

        // Set initial view
        mainController.setView("Buddy List");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
