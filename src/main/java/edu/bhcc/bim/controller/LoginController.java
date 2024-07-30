package edu.bhcc.bim.controller;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import edu.bhcc.bim.model.User;
import edu.bhcc.bim.service.AuthService;
import edu.bhcc.bim.state.AppState;
import edu.bhcc.bim.util.HashUtil;

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
        // usernameField.setText("TestUser"); // Set default username for testing

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        // passwordField.setText("password"); // Set default password for testing

        loginButton = new Button("Login");
        loginButton.setOnAction(event -> login());

        loginPane.getChildren().addAll(usernameField, passwordField, loginButton);
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashedPassword = HashUtil.hashPassword(password); // Hash the password

        new Thread(() -> {
            try {
                User user = AuthService.authenticateOrCreateUser(username, hashedPassword);

                Platform.runLater(() -> {
                    // Set user ID and add the user in AppState and start WebSocket connection
                    appState.setUserId(user.getUserId());
                    appState.addUser(user);
                    appState.getWebSocketManager().start();

                    // Transition to the Buddy List view
                    mainController.setView("Buddy List");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    // Display error message to the user
                    System.out.println("Login failed: " + e.getMessage());
                });
            }
        }).start();
    }

    public VBox getView() {
        return loginPane;
    }
}
