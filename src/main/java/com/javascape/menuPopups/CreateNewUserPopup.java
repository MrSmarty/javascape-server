package com.javascape.menuPopups;

import com.javascape.Server;
import com.javascape.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

        Label adminLabel = new Label("Admin:");
        CheckBox adminCheckBox = new CheckBox();

        Button submit = new Button("Done");

        submit.setOnAction(e -> {
            if (usernameField.textProperty().getValue() != "" && passwordField.textProperty().getValue() != ""
                    && emailField.textProperty().getValue() != "") {
                if (Server.getDataHandler().getUserHandler()
                        .addUser(new User(usernameField.textProperty().getValue(),
                                passwordField.textProperty().getValue(), adminCheckBox.selectedProperty().getValue(),
                                emailField.textProperty().getValue()))) {

                    popupStage.close();
                } else {
                    // TODO: Inform user that email is in use
                }

            } else {
                // TODO: Inform the user that all field must be filled out
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
        g.add(adminCheckBox, 1, 3);
        g.add(submit, 0, 4);
        g.add(cancel, 1, 5);

        Scene s = new Scene(g);
        s.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        popupStage.setScene(s);

        popupStage.show();

    }
}
