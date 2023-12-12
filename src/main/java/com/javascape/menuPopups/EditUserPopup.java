package com.javascape.menuPopups;

import com.javascape.Server;
import com.javascape.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditUserPopup {
    public EditUserPopup() {

        Stage popupStage = new Stage();
        popupStage.setTitle("Edit User");

        GridPane g = new GridPane();

        ChoiceBox<User> dropdown = new ChoiceBox<User>(Server.getDataHandler().getUserHandler().getAllUsers());

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label adminLabel = new Label("Admin:");
        CheckBox adminBox = new CheckBox();

        Button save = new Button("Save");

        Button cancel = new Button("Cancel");

        save.setOnAction(e -> {
            User u = dropdown.getValue();
            u.setUsername(usernameField.textProperty().getValue());
            u.setPassword(passwordField.textProperty().getValue());
            u.setEmail(emailField.textProperty().getValue());
            u.setAdmin(adminBox.selectedProperty().getValue());
            popupStage.close();
        });

        cancel.setOnAction(e -> {
            popupStage.close();
        });

        dropdown.setOnAction(e -> {
            usernameField.textProperty().set(dropdown.getValue().getUsername());
            passwordField.textProperty().set(dropdown.getValue().getPassword());
            emailField.textProperty().set(dropdown.getValue().getEmail());
            adminBox.selectedProperty().set(dropdown.getValue().isAdmin());
        });

        g.add(dropdown, 0, 0, 2, 1);
        g.add(usernameLabel, 0, 1);
        g.add (usernameField, 1, 1);
        g.add(passwordLabel, 0, 2);
        g.add(passwordField, 1, 2);
        g.add(emailLabel, 0, 3);
        g.add(emailField, 1, 3);
        g.add(adminLabel, 0, 4);
        g.add(adminBox, 1, 4);
        g.add(save, 0, 5);
        g.add(cancel, 1, 5);

        Scene scene = new Scene(g);

        popupStage.setScene(scene);
        popupStage.show();
    }
}
