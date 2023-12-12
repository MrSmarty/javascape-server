package com.javascape;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginWindow {
    Server server;

    Stage primaryStage;
    DataHandler dataHandler;

    GridPane g;

    Text title;

    Label emailLabel;
    TextField emailField;

    Label passwordLabel;
    TextField passwordField;

    Button loginButton;

    /** Initializes the login window */
    public LoginWindow(Stage primaryStage, DataHandler dataHandler, Server server) {
        this.primaryStage = primaryStage;
        this.dataHandler = dataHandler;
        this.server = server;
    }

    /**
     * Builds the login window
     * 
     * @return The scene for the login window
     */
    private Scene setupLoginScreen() {
        g = new GridPane();

        title = new Text("JavaScape Server");

        emailLabel = new Label("Email:");
        emailField = new TextField();

        passwordLabel = new Label("Password:");
        passwordField = new TextField();

        loginButton = new Button("Login");

        loginButton.setOnAction(e -> loginButtonClicked(emailField.getText(), passwordField.getText()));

        g.add(title, 0, 0);
        g.add(emailLabel, 0, 1);
        g.add(emailField, 1, 1);
        g.add(passwordLabel, 0, 2);
        g.add(passwordField, 1, 2);
        g.add(loginButton, 0, 3);

        Scene scene = new Scene(g, 600, 400);

        // Allows the user to press enter to login
        scene.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER"))
                loginButtonClicked(emailField.getText(), passwordField.getText());
        });

        return scene;
    }

    /** Processes the login info */
    private void loginButtonClicked(String email, String password) {
        if (dataHandler.serverLogin(email, password)) {
            Logger.print("Login successful");

            server.loggedInUser = dataHandler.getUserHandler().getUser(email);

            server.loggedIn();
        } else {
            Logger.print("Login failed");
            passwordField.textProperty().set("");
        }
    }

    /** Sets the primaryStage to the login screen */
    public void show() {
        primaryStage.setScene(setupLoginScreen());
    }

}
