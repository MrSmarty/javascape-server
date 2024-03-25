package com.javascape.menuPopups;

import com.javascape.Server;
import com.javascape.user.Permissions;
import com.javascape.user.User;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateNewUserPopup {

    public CreateNewUserPopup() {

        Stage popupStage = new Stage();
        popupStage.setTitle("Create New User");

        GridPane g = new GridPane();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password");
        TextField passwordField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label adminLabel = new Label("Permissions Level:");
        ChoiceBox<String> adminDropdown = new ChoiceBox<String>();
        adminDropdown.getItems().addAll(Permissions.getPermissionsList());

        Label errorLabel = new Label();

        Button submit = new Button("Done");

        submit.setOnAction(e -> {
            if (usernameField.textProperty().getValue() != "" && passwordField.textProperty().getValue() != ""
                    && emailField.textProperty().getValue() != "") {
                        boolean result = Server.getDataHandler().getUserHandler()
                        .addUser(new User(usernameField.textProperty().getValue(),
                                passwordField.textProperty().getValue(), Permissions.toInt(adminDropdown.getValue()),
                                emailField.textProperty().getValue()));
                if (result) {
                    popupStage.close();
                } else {
                    errorLabel.textProperty().set("User with email " + emailField.textProperty().getValue() + " already exists");
                }

            } else {
                errorLabel.textProperty().set("Please fill out all fields");
            }
        });

        Button cancel = new Button("Cancel");

        cancel.setOnAction(e -> {
            popupStage.close();
        });

        g.add(usernameLabel, 0, 0);
        g.add(usernameField, 1, 0);
        g.add(passwordLabel, 0, 1);
        g.add(passwordField, 1, 1);
        g.add(emailLabel, 0, 2);
        g.add(emailField, 1, 2);
        g.add(adminLabel, 0, 3);
        g.add(adminDropdown, 1, 3);
        g.add(errorLabel, 0, 4, 2, 1);
        g.add(submit, 0, 5);
        g.add(cancel, 1, 5);

        Scene s = new Scene(g);
        s.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        popupStage.setScene(s);

        popupStage.show();

    }
}
