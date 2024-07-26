package edu.bhcc.bim.controller;

import edu.bhcc.bim.state.AppState;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController {
    private VBox loginPane;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private AppState appState;
    private MainController mainController;

    public LoginController(AppState appState, MainController mainController) {
        this.appState = appState;
        this.mainController = mainController;
        initializeView();
    }

    private void initializeView() {
        loginPane = new VBox(10);
        loginPane.setPadding(new javafx.geometry.Insets(20));

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        loginButton = new Button("Login");
        loginButton.setOnAction(event -> login());

        loginPane.getChildren().addAll(usernameField, passwordField, loginButton);
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Add your authentication logic here
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // Simulate successful login
        appState.setUserId(Integer.valueOf(username));
        appState.getWebSocketManager().start();

        // Transition to the Buddy List view
        mainController.setView("Buddy List");
    }

    public VBox getView() {
        return loginPane;
    }
}
