package com.javascape.menuPopups;

import com.javascape.Server;
import com.javascape.chronjob.Chronjob;
import com.javascape.chronjob.Job;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DeleteChronjobPopup {
    public DeleteChronjobPopup() {
        Stage stage = new Stage();
        stage.setTitle("Delete Chronjob");

        GridPane g = new GridPane();

        ObservableList<Job> chronList = Server.getDataHandler().getChronManager().getAllJobs();

        ChoiceBox<Job> dropdown = new ChoiceBox<Job>(chronList);


        Button delete = new Button("Delete Chronjob");
        Button close = new Button("Close");

        delete.setOnAction(e -> {
            Server.getDataHandler().getChronManager().remove(dropdown.getValue());
            stage.close();
        });

        close.setOnAction(e -> {
            stage.close();
        });


        g.add(dropdown, 0, 0, 1, 1);
        g.add(delete, 0, 1);
        g.add(close, 1, 1);

        Scene scene = new Scene(g);
        scene.getStylesheets().add(getClass().getResource("/stylesheets/main.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
