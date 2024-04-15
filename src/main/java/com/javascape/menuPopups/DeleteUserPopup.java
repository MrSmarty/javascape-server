package com.javascape.menuPopups;

import com.javascape.Server;
import com.javascape.user.User;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DeleteUserPopup {
    public static void showDeleteUserPopup(Server server) {

        Stage popupStage = new Stage();
        popupStage.setTitle("Delete User");


        GridPane g = new GridPane();

        ObservableList<User> userList = Server.getDataHandler().getUserHandler().getAllUsers();
        userList.remove(server.loggedInUser);

        ChoiceBox<User> dropdown = new ChoiceBox<>(userList);


        Button delete = new Button("Delete User");
        Button close = new Button("Close");

        delete.setOnAction(e -> {
            Server.getDataHandler().getUserHandler().removeUser(dropdown.getValue());
            popupStage.close();
        });

        close.setOnAction(e -> {
            popupStage.close();
        });


        g.add(dropdown, 0, 0, 1, 1);
        g.add(delete, 0, 1);
        g.add(close, 1, 1);

        Scene scene = new Scene(g);
        scene.getStylesheets().add(DeleteUserPopup.class.getResource("/stylesheets/main.css").toExternalForm());

        popupStage.setScene(scene);

        popupStage.show();
    }
}
